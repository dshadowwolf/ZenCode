package org.openzen.zenscript.codemodel.expression;

import org.openzen.zencode.shared.CodePosition;
import org.openzen.zenscript.codemodel.member.IDefinitionMember;
import org.openzen.zenscript.codemodel.member.ref.FieldMemberRef;
import org.openzen.zenscript.codemodel.scope.TypeScope;
import org.openzen.zenscript.codemodel.type.TypeID;

import java.util.Collections;
import java.util.List;

public class GetStaticFieldExpression extends Expression {
	public final FieldMemberRef field;

	public GetStaticFieldExpression(CodePosition position, FieldMemberRef field) {
		super(position, field.getType(), null);

		this.field = field;
	}

	@Override
	public List<TypeID> getAssignHints() {
		return Collections.singletonList(field.getType());
	}

	@Override
	public CapturedExpression capture(CodePosition position, LambdaClosure closure) {
		return new CapturedDirectExpression(position, closure, this);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitGetStaticField(this);
	}

	@Override
	public <C, R> R accept(C context, ExpressionVisitorWithContext<C, R> visitor) {
		return visitor.visitGetStaticField(context, this);
	}

	@Override
	public Expression transform(ExpressionTransformer transformer) {
		return this;
	}

	@Override
	public IDefinitionMember getMember() {
		return field.member;
	}

	@Override
	public Expression normalize(TypeScope scope) {
		return this;
	}
}
