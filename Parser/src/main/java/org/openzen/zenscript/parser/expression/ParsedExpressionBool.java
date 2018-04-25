/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zenscript.parser.expression;

import org.openzen.zenscript.codemodel.expression.ConstantBoolExpression;
import org.openzen.zenscript.codemodel.partial.IPartialExpression;
import org.openzen.zenscript.linker.ExpressionScope;
import org.openzen.zenscript.shared.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionBool extends ParsedExpression {
	private final boolean value;

	public ParsedExpressionBool(CodePosition position, boolean value) {
		super(position);

		this.value = value;
	}

	@Override
	public IPartialExpression compile(ExpressionScope scope) {
		return new ConstantBoolExpression(position, value);
	}

	@Override
	public boolean hasStrongType() {
		return true;
	}
}