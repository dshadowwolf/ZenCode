/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openzen.zenscript.codemodel.GenericMapper;
import org.openzen.zenscript.codemodel.generic.TypeParameter;
import org.openzen.zenscript.codemodel.type.StoredType;
import org.openzen.zenscript.codemodel.type.TypeArgument;
import org.openzen.zenscript.codemodel.type.member.LocalMemberCache;
import org.openzen.zenscript.codemodel.type.member.TypeMembers;

/**
 *
 * @author Hoofdgebruiker
 */
public class TypeContext {
	protected final TypeParameter[] typeParameters;
	public final StoredType thisType;
	private final LocalMemberCache memberCache;
	public final ModuleContext moduleContext;
	
	public TypeContext(ModuleContext context, TypeParameter[] parameters, StoredType thisType) {
		this.typeParameters = parameters;
		this.thisType = thisType;
		memberCache = new LocalMemberCache(context.registry, context.expansions);
		moduleContext = context;
	}
	
	public TypeContext(TypeContext outer, StoredType thisType, TypeParameter... inner) {
		typeParameters = concat(outer.typeParameters, inner);
		this.thisType = thisType;
		moduleContext = outer.moduleContext;
		memberCache = new LocalMemberCache(moduleContext.registry, moduleContext.expansions);
	}
	
	public TypeContext(TypeContext outer, StoredType thisType, List<TypeParameter> inner) {
		this(outer, thisType, inner.toArray(new TypeParameter[inner.size()]));
	}
	
	public int getId(TypeParameter parameter) {
		for (int i = 0; i < typeParameters.length; i++)
			if (typeParameters[i] == parameter)
				return i;
		
		return -1;
	}
	
	public TypeParameter getTypeParameter(int index) {
		return typeParameters[index];
	}
	
	public TypeMembers getTypeMembers(StoredType type) {
		return memberCache.get(type);
	}
	
	public GenericMapper getMapper() {
		Map<TypeParameter, TypeArgument> mapper = TypeArgument.getSelfMapping(moduleContext.registry, typeParameters);
		return new GenericMapper(moduleContext.registry, mapper);
	}
	
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}