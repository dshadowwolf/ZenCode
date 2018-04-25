/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.expression;

import java.util.List;
import org.openzen.zenscript.codemodel.FunctionHeader;
import org.openzen.zenscript.codemodel.statement.Statement;
import org.openzen.zenscript.codemodel.type.FunctionTypeID;
import org.openzen.zenscript.shared.CodePosition;

/**
 *
 * @author Hoofdgebruiker
 */
public class FunctionExpression extends Expression {
	public final FunctionHeader header;
	public final LambdaClosure closure;
	public final List<Statement> statements;
	
	public FunctionExpression(
			CodePosition position,
			FunctionTypeID type,
			LambdaClosure closure,
			List<Statement> statements) {
		super(position, type);
		
		this.header = type.header;
		this.closure = closure;
		this.statements = statements;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitFunction(this);
	}
}