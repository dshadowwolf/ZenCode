/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.expression;

import org.openzen.zencode.shared.CodePosition;
import org.openzen.zenscript.codemodel.member.EnumConstantMember;
import org.openzen.zenscript.codemodel.member.ref.ConstMemberRef;

/**
 *
 * @author Hoofdgebruiker
 */
public class ConstExpression extends Expression {
	public final ConstMemberRef constant;
	
	public ConstExpression(CodePosition position, ConstMemberRef constant) {
		super(position, constant.type, null);
		
		this.constant = constant;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitConst(this);
	}

	@Override
	public Expression transform(ExpressionTransformer transformer) {
		return this;
	}
	
	@Override
	public String evaluateStringConstant() {
		return constant.member.value.evaluateStringConstant();
	}
	
	@Override
	public EnumConstantMember evaluateEnumConstant() {
		return constant.member.value.evaluateEnumConstant();
	}
}