package org.openzen.zenscript.scriptingexample.tests.helpers;

import org.junit.jupiter.api.*;
import org.openzen.zencode.java.*;
import org.openzen.zencode.shared.*;
import org.openzen.zenscript.codemodel.*;
import org.openzen.zenscript.codemodel.type.*;
import org.openzen.zenscript.lexer.*;
import org.openzen.zenscript.parser.*;
import org.openzen.zenscript.scriptingexample.tests.*;

import java.util.*;


public abstract class ZenCodeTest {
    
    private final List<SourceFile> sourceFiles;
    protected ScriptingEngine engine;
    protected JavaNativeModule testModule;
    protected ZenCodeTestLogger logger;
    
    protected ZenCodeTest() {
        sourceFiles = new ArrayList<>();
    }
    
    
    @BeforeEach
    public void beforeEach() throws CompileException {
        this.logger = new ZenCodeTestLogger();
        this.engine = new ScriptingEngine(logger);
        engine.debug = true;
        this.testModule = engine.createNativeModule("test_module", "org.openzen.zenscript.scripting_tests");
        SharedGlobals.currentlyActiveLogger = logger;
        
        getRequiredClasses().stream().distinct().forEach(requiredClass -> {
            testModule.addGlobals(requiredClass);
            testModule.addClass(requiredClass);
        });
        engine.registerNativeProvided(testModule);
    }
    
    public void executeEngine() {
        executeEngine(false);
    }
    
    public void executeEngine(boolean allowError) {
        try {
            final FunctionParameterList parameters = getParameters();
            final SemanticModule script_tests = engine.createScriptedModule("script_tests", sourceFiles
                    .toArray(new SourceFile[0]), getBEP(), parameters.getParameters());
            final boolean scriptsValid = script_tests.isValid();
            if(allowError) {
                if(!scriptsValid) {
                    logger.setEngineComplete();
                    return;
                }
            } else {
                Assertions.assertTrue(scriptsValid, "Scripts are not valid!");
            }
            engine.registerCompiled(script_tests);
            engine.run(parameters.getParameterMap());
        } catch(ParseException e) {
            e.printStackTrace();
            Assertions.fail("Error in Engine execution", e);
        }
        logger.setEngineComplete();
    }
    
    public void addScript(String content) {
        addScript(content, "test_script_" + sourceFiles.size() + ".zs");
    }
    
    public void addScript(String content, String name) {
        sourceFiles.add(new LiteralSourceFile(name, content));
    }
    
    
    public BracketExpressionParser getBEP() {
        return null;
    }
    
    public FunctionParameterList getParameters() {
        final FunctionParameterList functionParameterList = new FunctionParameterList();
        final TypeID stringArrayType = engine.registry.getArray(BasicTypeID.STRING, 1);
        FunctionParameter args = new FunctionParameter(stringArrayType, "args");
        functionParameterList.addParameter(args, new String[]{"a", "b", "c"});
        return functionParameterList;
    }
    
    public List<Class<?>> getRequiredClasses() {
        final ArrayList<Class<?>> result = new ArrayList<>();
        result.add(SharedGlobals.class);
        return result;
    }
}
