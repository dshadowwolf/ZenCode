package org.openzen.zenscript.parser.statements;

import org.openzen.zencode.shared.CodePosition;
import org.openzen.zencode.shared.CompileException;
import org.openzen.zenscript.codemodel.WhitespaceInfo;
import org.openzen.zenscript.codemodel.scope.ExpressionScope;
import org.openzen.zenscript.codemodel.scope.StatementScope;
import org.openzen.zenscript.codemodel.statement.ExpressionStatement;
import org.openzen.zenscript.codemodel.statement.InvalidStatement;
import org.openzen.zenscript.codemodel.statement.Statement;
import org.openzen.zenscript.parser.ParsedAnnotation;
import org.openzen.zenscript.parser.expression.ParsedExpression;

public class ParsedStatementExpression extends ParsedStatement {
	private final ParsedExpression expression;

	public ParsedStatementExpression(CodePosition position, ParsedAnnotation[] annotations, WhitespaceInfo whitespace, ParsedExpression expression) {
		super(position, annotations, whitespace);

		this.expression = expression;
	}

	@Override
	public Statement compile(StatementScope scope) {
		try {
			return result(new ExpressionStatement(position, this.expression.compile(new ExpressionScope(scope)).eval()), scope);
		} catch (CompileException ex) {
			return result(new InvalidStatement(ex), scope);
		}
	}
}
