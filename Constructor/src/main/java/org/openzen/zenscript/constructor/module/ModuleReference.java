/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.constructor.module;

import java.util.function.Consumer;

import org.openzen.zencode.shared.CompileException;
import org.openzen.zencode.shared.logging.*;
import org.openzen.zenscript.codemodel.SemanticModule;
import org.openzen.zenscript.codemodel.type.GlobalTypeRegistry;
import org.openzen.zenscript.constructor.ModuleLoader;
import org.openzen.zenscript.constructor.module.logging.*;

/**
 * @author Hoofdgebruiker
 */
public interface ModuleReference {
	public String getName();

	public SemanticModule load(ModuleLoader loader, GlobalTypeRegistry registry, ModuleLogger exceptionLogger);

	public SourcePackage getRootPackage();
}
