/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.scope;

import java.util.List;
import java.util.function.Function;
import org.openzen.zencode.shared.CodePosition;
import org.openzen.zenscript.codemodel.FunctionHeader;
import org.openzen.zenscript.codemodel.expression.Expression;
import org.openzen.zenscript.codemodel.partial.IPartialExpression;
import org.openzen.zenscript.codemodel.statement.LoopStatement;
import org.openzen.zenscript.codemodel.type.GenericName;
import org.openzen.zenscript.codemodel.type.GlobalTypeRegistry;
import org.openzen.zenscript.codemodel.type.ITypeID;
import org.openzen.zenscript.codemodel.type.member.LocalMemberCache;
import org.openzen.zenscript.codemodel.type.member.TypeMembers;

/**
 *
 * @author Hoofdgebruiker
 */
public abstract class BaseScope implements TypeScope {
	public abstract LocalMemberCache getMemberCache();
	
	public abstract IPartialExpression get(CodePosition position, GenericName name);
	
	public abstract ITypeID getType(CodePosition position, List<GenericName> name);
	
	public abstract LoopStatement getLoop(String name);
	
	public abstract FunctionHeader getFunctionHeader();
	
	public abstract ITypeID getThisType();
	
	@Override
	public TypeMembers getTypeMembers(ITypeID type) {
		return getMemberCache().get(type);
	}
	
	@Override
	public GlobalTypeRegistry getTypeRegistry() {
		return getMemberCache().getRegistry();
	}
	
	public abstract Function<CodePosition, Expression> getDollar();
	
	public abstract IPartialExpression getOuterInstance(CodePosition position);
}