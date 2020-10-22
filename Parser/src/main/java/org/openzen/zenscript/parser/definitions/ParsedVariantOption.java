/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.parser.definitions;

import java.util.List;
import org.openzen.zencode.shared.CodePosition;
import org.openzen.zenscript.codemodel.context.TypeResolutionContext;
import org.openzen.zenscript.codemodel.definition.VariantDefinition;
import org.openzen.zenscript.codemodel.type.TypeID;
import org.openzen.zenscript.parser.type.IParsedType;

/**
 *
 * @author Hoofdgebruiker
 */
public class ParsedVariantOption {
	public final CodePosition position;
	public final String name;
	public final int ordinal;
	public final List<IParsedType> types;
	
	public ParsedVariantOption(CodePosition position, String name, int ordinal, List<IParsedType> types) {
		this.position = position;
		this.name = name;
		this.ordinal = ordinal;
		this.types = types;
	}
	
	public VariantDefinition.Option compile(VariantDefinition variant, TypeResolutionContext context) {
		TypeID[] cTypes = new TypeID[types.size()];
		for (int i = 0; i < cTypes.length; i++)
			cTypes[i] = types.get(i).compile(context);
		
		return new VariantDefinition.Option(position, variant, name, ordinal, cTypes);
	}
}
