/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.expression;

import org.openzen.zenscript.codemodel.FunctionParameter;
import org.openzen.zenscript.shared.CodePosition;

/**
 *
 * @author Hoofdgebruiker
 */
public class CapturedParameterExpression extends CapturedExpression {
	public final FunctionParameter parameter;
	
	public CapturedParameterExpression(CodePosition position, FunctionParameter parameter, LambdaClosure closure) {
		super(position, parameter.type, closure);
		
		this.parameter = parameter;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCapturedParameter(this);
	}
}