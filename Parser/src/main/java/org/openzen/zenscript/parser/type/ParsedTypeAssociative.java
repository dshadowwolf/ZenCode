/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.parser.type;

import org.openzen.zenscript.codemodel.context.TypeResolutionContext;
import org.openzen.zenscript.codemodel.type.GlobalTypeRegistry;
import org.openzen.zenscript.codemodel.type.ITypeID;
import org.openzen.zenscript.codemodel.type.member.TypeMembers;
import org.openzen.zenscript.codemodel.scope.BaseScope;
import org.openzen.zenscript.codemodel.type.ModifiedTypeID;

/**
 *
 * @author Hoofdgebruiker
 */
public class ParsedTypeAssociative implements IParsedType {
	public final IParsedType key;
	public final IParsedType value;
	public final int modifiers;
	
	public ParsedTypeAssociative(IParsedType key, IParsedType value) {
		this.key = key;
		this.value = value;
		this.modifiers = 0;
	}

	private ParsedTypeAssociative(IParsedType key, IParsedType value, int modifiers) {
		this.key = key;
		this.value = value;
		this.modifiers = modifiers;
	}
	
	@Override
	public IParsedType withOptional() {
		return new ParsedTypeAssociative(key, value, modifiers | ModifiedTypeID.MODIFIER_OPTIONAL);
	}

	@Override
	public IParsedType withModifiers(int modifiers) {
		return new ParsedTypeAssociative(key, value, this.modifiers | modifiers);
	}

	@Override
	public ITypeID compile(TypeResolutionContext context) {
		ITypeID key = this.key.compile(context);
		ITypeID value = this.value.compile(context);
		GlobalTypeRegistry registry = context.getTypeRegistry();
		return registry.getModified(modifiers, context.getTypeRegistry().getAssociative(key, value));
	}
}
