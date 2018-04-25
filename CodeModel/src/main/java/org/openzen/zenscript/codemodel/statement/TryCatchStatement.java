/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.statement;

import java.util.List;
import org.openzen.zenscript.codemodel.expression.Expression;
import org.openzen.zenscript.shared.CodePosition;

/**
 *
 * @author Hoofdgebruiker
 */
public class TryCatchStatement extends Statement {
	public final String resourceName;
	public final Expression resourceInitializer;
	public final Statement content;
	public final List<CatchClause> catchClauses;
	public final Statement finallyClause;
	
	public TryCatchStatement(
			CodePosition position,
			String resourceName,
			Expression resourceInitializer,
			Statement content,
			List<CatchClause> catchClauses,
			Statement finallyClause) {
		super(position);
		
		this.resourceName = resourceName;
		this.resourceInitializer = resourceInitializer;
		this.content = content;
		this.catchClauses = catchClauses;
		this.finallyClause = finallyClause;
	}

	@Override
	public <T> T accept(StatementVisitor<T> visitor) {
		return visitor.visitTryCatch(this);
	}
}