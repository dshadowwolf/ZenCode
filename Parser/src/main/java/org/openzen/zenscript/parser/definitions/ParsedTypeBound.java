/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.parser.definitions;

import org.openzen.zenscript.codemodel.generic.GenericParameterBound;
import org.openzen.zenscript.codemodel.generic.ParameterTypeBound;
import org.openzen.zenscript.linker.BaseScope;
import org.openzen.zenscript.parser.type.IParsedType;
import org.openzen.zenscript.shared.CodePosition;

/**
 *
 * @author Hoofdgebruiker
 */
public class ParsedTypeBound extends ParsedGenericBound {
	public final CodePosition position;
	public final IParsedType type;
	
	public ParsedTypeBound(CodePosition position, IParsedType type) {
		this.position = position;
		this.type = type;
	}

	@Override
	public GenericParameterBound compile(BaseScope scope) {
		return new ParameterTypeBound(position, type.compile(scope));
	}
}