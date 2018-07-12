/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.definition;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.shared.CodePosition;
import org.openzen.zencode.shared.Taggable;
import org.openzen.zenscript.codemodel.GenericMapper;
import org.openzen.zenscript.codemodel.HighLevelDefinition;
import org.openzen.zenscript.codemodel.member.ref.VariantOptionRef;
import org.openzen.zenscript.codemodel.type.ITypeID;

/**
 *
 * @author Hoofdgebruiker
 */
public class VariantDefinition extends HighLevelDefinition {
	public final List<Option> options = new ArrayList<>();
	
	public VariantDefinition(CodePosition position, ZSPackage pkg, String name, int modifiers, HighLevelDefinition outerDefinition) {
		super(position, pkg, name, modifiers, outerDefinition);
	}

	@Override
	public <T> T accept(DefinitionVisitor<T> visitor) {
		return visitor.visitVariant(this);
	}
	
	public static class Option extends Taggable {
		public final String name;
		public final ITypeID[] types;
		
		public Option(String name, ITypeID[] types) {
			this.name = name;
			this.types = types;
		}
		
		public VariantOptionRef instance(GenericMapper mapper) {
			return new VariantOptionRef(this, mapper.map(types));
		}
	}
}