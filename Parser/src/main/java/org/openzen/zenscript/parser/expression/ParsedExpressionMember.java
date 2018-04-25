/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zenscript.parser.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zenscript.codemodel.partial.IPartialExpression;
import org.openzen.zenscript.codemodel.type.GenericName;
import org.openzen.zenscript.codemodel.type.ITypeID;
import org.openzen.zenscript.linker.ExpressionScope;
import org.openzen.zenscript.parser.type.IParsedType;
import org.openzen.zenscript.shared.CodePosition;
import org.openzen.zenscript.shared.CompileException;
import org.openzen.zenscript.shared.CompileExceptionCode;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionMember extends ParsedExpression {
	private final ParsedExpression value;
	private final String member;
	private final List<IParsedType> genericParameters;

	public ParsedExpressionMember(CodePosition position, ParsedExpression value, String member, List<IParsedType> genericParameters) {
		super(position);

		this.value = value;
		this.member = member;
		this.genericParameters = genericParameters;
	}

	@Override
	public IPartialExpression compile(ExpressionScope scope) {
		List<ITypeID> genericArguments = Collections.emptyList();
		if (!genericParameters.isEmpty()) {
			genericArguments = new ArrayList<>();
			for (IParsedType type : this.genericParameters) {
				genericArguments.add(type.compile(scope));
			}
		}
		
		IPartialExpression cValue = value.compile(scope.withoutHints());
		IPartialExpression member = cValue.getMember(position, scope, scope.hints, new GenericName(this.member, genericArguments));
		if (member == null)
			throw new CompileException(position, CompileExceptionCode.NO_SUCH_MEMBER, "Member not found: " + this.member);
		return member;
	}

	@Override
	public boolean hasStrongType() {
		return true;
	}
}