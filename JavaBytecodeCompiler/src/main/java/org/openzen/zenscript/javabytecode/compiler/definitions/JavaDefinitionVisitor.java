package org.openzen.zenscript.javabytecode.compiler.definitions;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.openzen.zenscript.codemodel.FunctionParameter;
import org.openzen.zenscript.codemodel.definition.*;
import org.openzen.zenscript.codemodel.expression.Expression;
import org.openzen.zenscript.codemodel.expression.ExpressionVisitor;
import org.openzen.zenscript.codemodel.member.ConstructorMember;
import org.openzen.zenscript.codemodel.member.EnumConstantMember;
import org.openzen.zenscript.codemodel.member.FieldMember;
import org.openzen.zenscript.codemodel.member.IDefinitionMember;
import org.openzen.zenscript.codemodel.statement.ExpressionStatement;
import org.openzen.zenscript.codemodel.statement.ReturnStatement;
import org.openzen.zenscript.codemodel.statement.Statement;
import org.openzen.zenscript.codemodel.type.BasicTypeID;
import org.openzen.zenscript.codemodel.type.ITypeID;
import org.openzen.zenscript.javabytecode.JavaClassInfo;
import org.openzen.zenscript.javabytecode.JavaEnumInfo;
import org.openzen.zenscript.javabytecode.JavaMethodInfo;
import org.openzen.zenscript.javabytecode.compiler.*;

import java.util.Iterator;

public class JavaDefinitionVisitor implements DefinitionVisitor<byte[]> {
	private static final JavaClassInfo T_CLASS = new JavaClassInfo("java/lang/Class");
	private static final JavaMethodInfo CLASS_FORNAME = new JavaMethodInfo(
			T_CLASS,
			"forName",
			"(Ljava/lang/String;)Ljava/lang/Class;",
			Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC);
	
	private static final JavaClassInfo T_ENUM = new JavaClassInfo("java/lang/Enum");
	private static final JavaMethodInfo ENUM_VALUEOF = new JavaMethodInfo(
			T_ENUM,
			"valueOf",
			"(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;",
			Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC);

    private final JavaClassWriter outerWriter;

    public JavaDefinitionVisitor(JavaClassWriter outerWriter) {

        this.outerWriter = outerWriter;
    }

    @Override
    public byte[] visitClass(ClassDefinition definition) {
        //Classes will always be created in a new File/Class

        final Type superType;
        if (definition.superType == null)
            superType = Type.getType(Object.class);
        else
            superType = Type.getType(definition.superType.accept(JavaTypeClassVisitor.INSTANCE));

		JavaClassInfo toClass = new JavaClassInfo(definition.name);
        JavaClassWriter writer = new JavaClassWriter(ClassWriter.COMPUTE_FRAMES);

        //TODO: Calculate signature from generic parameters
        //TODO: Interfaces?
        String signature = null;


        writer.visit(Opcodes.V1_8, definition.modifiers, definition.name, signature, superType.getInternalName(), null);
        for (IDefinitionMember member : definition.members) {
            if (member instanceof ConstructorMember) {
                final ConstructorMember constructorMember = (ConstructorMember) member;
                constructorMember.body.add(0, new ExpressionStatement(constructorMember.position, new Expression(constructorMember.position, BasicTypeID.VOID) {
                    @Override
                    public <T> T accept(ExpressionVisitor<T> visitor) {
                        if (visitor instanceof JavaExpressionVisitor) {
                            JavaWriter javaWriter = ((JavaExpressionVisitor) visitor).getJavaWriter();
                            javaWriter.loadObject(0);
                            //TODO Super constructor?
                            javaWriter.invokeSpecial(Type.getInternalName(Object.class), "<init>", "()V");
                            for (final IDefinitionMember definitionMember : definition.members) {
                                if (definitionMember instanceof FieldMember && ((FieldMember) definitionMember).initializer != null) {
                                    final FieldMember field = (FieldMember) definitionMember;
                                    javaWriter.loadObject(0);
                                    field.initializer.accept(visitor);
                                    javaWriter.putField(definition.name, field.name, Type.getDescriptor(field.type.accept(JavaTypeClassVisitor.INSTANCE)));
                                }
                            }
                        }
                        return null;
                    }
                }));
            }
			
            member.accept(new JavaMemberVisitor(writer, toClass));
        }
        writer.visitEnd();
        return writer.toByteArray();
    }

    @Override
    public byte[] visitInterface(InterfaceDefinition definition) {
		JavaClassInfo toClass = new JavaClassInfo(definition.name);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //TODO: Calculate signature from generic parameters
        //TODO: Extending Interfaces?
        String signature = null;
        writer.visit(Opcodes.V1_8, definition.modifiers | Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT, definition.name, signature, Type.getInternalName(Object.class), null);
        for (IDefinitionMember member : definition.members) {
            member.accept(new JavaMemberVisitor(writer, toClass));
        }
        writer.visitEnd();
        return writer.toByteArray();
    }

    @Override
    public byte[] visitEnum(EnumDefinition definition) {
        final Type superType;
        if (definition.superType == null)
            superType = Type.getType(Object.class);
        else
            superType = Type.getType(definition.superType.accept(JavaTypeClassVisitor.INSTANCE));

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        writer.visit(Opcodes.V1_8, Opcodes.ACC_ENUM | Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER | Opcodes.ACC_FINAL, definition.name, "Ljava/lang/Enum<L" + definition.name + ";>;", superType.getInternalName(), null);

        for (IDefinitionMember member : definition.members) {
            if (member instanceof EnumConstantMember) {
                EnumConstantMember constantMember = (EnumConstantMember) member;
                writer.visitField(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_ENUM, constantMember.name, "L" + definition.name + ";", null, null).visitEnd();
            } else if (member instanceof ConstructorMember) {
                member.setTag(JavaEnumInfo.class, null);
            }
        }

		JavaClassInfo toClass = new JavaClassInfo(definition.name);
		JavaMethodInfo clinitInfo = new JavaMethodInfo(toClass, "<clinit>", "()V", Opcodes.ACC_STATIC);
        final JavaWriter clinitWriter = new JavaWriter(writer, clinitInfo, null, null);
        final JavaStatementVisitor clinitVisitor = new JavaStatementVisitor(clinitWriter);
        clinitVisitor.start();
        int constantCount = 0;
        for (IDefinitionMember member : definition.members) {
            if (member instanceof ConstructorMember) {
                final ConstructorMember constructorMember = (ConstructorMember) member;


                constructorMember.body.add(0, new ExpressionStatement(constructorMember.position, new Expression(constructorMember.position, BasicTypeID.VOID) {
                    @Override
                    public <T> T accept(ExpressionVisitor<T> visitor) {
                        if (visitor instanceof JavaExpressionVisitor) {
                            JavaWriter javaWriter = ((JavaExpressionVisitor) visitor).getJavaWriter();
                            javaWriter.getVisitor().newLocal(Type.getType(String.class));
                            javaWriter.getVisitor().newLocal(Type.getType(int.class));
                            javaWriter.loadObject(0);
                            javaWriter.loadObject(1);
                            javaWriter.loadInt(2);
                            //javaWriter.invokeSpecial(Type.getInternalName(Enum.class), "<init>", "(Ljava/lang/String;I)V");
                        }
                        return null;
                    }
                }));
            } else if (member instanceof EnumConstantMember) {
                ++constantCount;
                member.setTag(JavaEnumInfo.class, new JavaEnumInfo(clinitVisitor));
            }
            member.accept(new JavaMemberVisitor(writer, toClass));
        }

        clinitWriter.constant(constantCount);
        clinitWriter.newArray(Type.getType("L" + definition.name + ";"));

        for (IDefinitionMember member : definition.members) {
            if (!(member instanceof EnumConstantMember)) continue;
            final EnumConstantMember constantMember = (EnumConstantMember) member;
            clinitWriter.dup();
            clinitWriter.constant(constantMember.value);
            clinitWriter.getStaticField(definition.name, constantMember.name, "L" + definition.name + ";");
            clinitWriter.arrayStore(Type.getType("L" + definition.name + ";"));
        }
        clinitWriter.putStaticField(definition.name, "$VALUES", "[L" + definition.name + ";");
        clinitVisitor.end();

        //Enum Stuff(required!)
        writer.visitField(Opcodes.ACC_STATIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC, "$VALUES", "[L" + definition.name + ";", null, null).visitEnd();
		
		JavaClassInfo arrayClass = new JavaClassInfo("[L" + definition.name + ";");
		JavaMethodInfo arrayClone = new JavaMethodInfo(arrayClass, "clone", "()Ljava/lang/Object;", Opcodes.ACC_PUBLIC);

		JavaMethodInfo valuesMethodInfo = new JavaMethodInfo(toClass, "values", "()[L" + definition.name + ";", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC);
        JavaWriter valuesWriter = new JavaWriter(writer, true, valuesMethodInfo, null, null);
        valuesWriter.start();
        valuesWriter.getStaticField(definition.name, "$VALUES", "[L" + definition.name + ";");
        valuesWriter.invokeVirtual(arrayClone);
        valuesWriter.checkCast("[L" + definition.name + ";");
        valuesWriter.returnObject();
        valuesWriter.end();

		JavaMethodInfo valueOfMethodInfo = new JavaMethodInfo(toClass, "valueOf", "(Ljava/lang/String;)L" + definition.name + ";", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC);
        JavaWriter valueOfWriter = new JavaWriter(writer, true, valueOfMethodInfo, null, null);
        valueOfWriter.start();
        valueOfWriter.invokeStatic(CLASS_FORNAME);
        valueOfWriter.loadObject(0);
        valueOfWriter.invokeStatic(ENUM_VALUEOF);
        valueOfWriter.checkCast("L" + definition.name + ";");
        valueOfWriter.returnObject();
        valueOfWriter.end();


        writer.visitEnd();
        return writer.toByteArray();
    }

    @Override
    public byte[] visitStruct(StructDefinition definition) {
        return null;
    }

    @Override
    public byte[] visitFunction(FunctionDefinition definition) {
        final String signature = CompilerUtils.calcSign(definition.header, false);
		
		final JavaClassInfo toClass = new JavaClassInfo(CompilerUtils.calcClasName(definition.position));
		final JavaMethodInfo methodInfo = new JavaMethodInfo(toClass, definition.name, CompilerUtils.calcDesc(definition.header, false), CompilerUtils.calcAccess(definition.modifiers) | Opcodes.ACC_STATIC);

		final JavaWriter writer = new JavaWriter(outerWriter, true, methodInfo, signature, null);
        final JavaStatementVisitor statementVisitor = new JavaStatementVisitor(writer);
        statementVisitor.start();
        final Iterator<Statement> statementIterator = definition.statements.iterator();

        //TODO this is dirty, fix when there is a way of knowing if a parameter is static or not
        for (FunctionParameter parameter : definition.header.parameters) {
            parameter.index -= 1;
        }
        while (statementIterator.hasNext()) {
            final Statement statement = statementIterator.next();
            statement.accept(statementVisitor);
            if (!statementIterator.hasNext() && !(statement instanceof ReturnStatement)) {
                ITypeID type = definition.header.returnType;
                if (CompilerUtils.isPrimitive(type))
                    writer.iConst0();
                else if (type != BasicTypeID.VOID)
                    writer.aConstNull();
                writer.returnType(type.accept(JavaTypeVisitor.INSTANCE));
            }
        }

        statementVisitor.end();
		
        definition.setTag(JavaMethodInfo.class, methodInfo);
        definition.caller.setTag(JavaMethodInfo.class, methodInfo);
        return null;
    }

    @Override
    public byte[] visitExpansion(ExpansionDefinition definition) {
        return null;
    }

    @Override
    public byte[] visitAlias(AliasDefinition definition) {
        return null;
    }
}