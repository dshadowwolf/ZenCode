/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.expression;

import org.openzen.zenscript.codemodel.member.FieldMember;
import org.openzen.zenscript.shared.CodePosition;

/**
 *
 * @author Hoofdgebruiker
 */
public class SetFieldExpression extends Expression {
	public final Expression target;
	public final FieldMember field;
	public final Expression value;
	
	public SetFieldExpression(CodePosition position, Expression target, FieldMember field, Expression value) {
		super(position, field.type);
		
		this.target = target;
		this.field = field;
		this.value = value;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSetField(this);
	}
}