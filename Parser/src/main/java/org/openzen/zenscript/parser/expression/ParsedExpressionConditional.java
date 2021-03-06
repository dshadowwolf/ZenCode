package org.openzen.zenscript.parser.expression;

import org.openzen.zencode.shared.CodePosition;
import org.openzen.zencode.shared.CompileException;
import org.openzen.zencode.shared.CompileExceptionCode;
import org.openzen.zenscript.codemodel.expression.ConditionalExpression;
import org.openzen.zenscript.codemodel.expression.Expression;
import org.openzen.zenscript.codemodel.partial.IPartialExpression;
import org.openzen.zenscript.codemodel.scope.ExpressionScope;
import org.openzen.zenscript.codemodel.type.BasicTypeID;
import org.openzen.zenscript.codemodel.type.TypeID;
import org.openzen.zenscript.codemodel.type.member.TypeMembers;

public class ParsedExpressionConditional extends ParsedExpression {
	private final ParsedExpression condition;
	private final ParsedExpression ifThen;
	private final ParsedExpression ifElse;

	public ParsedExpressionConditional(CodePosition position, ParsedExpression condition, ParsedExpression ifThen, ParsedExpression ifElse) {
		super(position);

		this.condition = condition;
		this.ifThen = ifThen;
		this.ifElse = ifElse;
	}

	@Override
	public IPartialExpression compile(ExpressionScope scope) throws CompileException {
		Expression cIfThen = ifThen.compile(scope).eval();
		Expression cIfElse = ifElse.compile(scope).eval();

		TypeMembers thenMembers = scope.getTypeMembers(cIfThen.type);
		TypeMembers elseMembers = scope.getTypeMembers(cIfElse.type);
		TypeID resultType = null;
		for (TypeID hint : scope.hints) {
			if (thenMembers.canCastImplicit(hint) && elseMembers.canCastImplicit(hint)) {
				if (resultType != null)
					throw new CompileException(position, CompileExceptionCode.MULTIPLE_MATCHING_HINTS, "Not sure which type to use");

				resultType = hint;
			}
		}

		if (resultType == null)
			resultType = thenMembers.union(cIfElse.type);

		if (resultType == null)
			throw new CompileException(position, CompileExceptionCode.TYPE_CANNOT_UNITE, "These types could not be unified: " + cIfThen.type + " and " + cIfElse.type);

		cIfThen = cIfThen.castImplicit(position, scope, resultType);
		cIfElse = cIfElse.castImplicit(position, scope, resultType);

		return new ConditionalExpression(
				position,
				condition.compile(scope.withHints(BasicTypeID.HINT_BOOL)).eval().castImplicit(position, scope, BasicTypeID.BOOL),
				cIfThen,
				cIfElse,
				resultType);
	}

	@Override
	public boolean hasStrongType() {
		return ifThen.hasStrongType() && ifElse.hasStrongType();
	}
}
