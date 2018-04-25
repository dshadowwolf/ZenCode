/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.parser.statements;

import org.openzen.zenscript.codemodel.statement.BreakStatement;
import org.openzen.zenscript.codemodel.statement.Statement;
import org.openzen.zenscript.linker.StatementScope;
import org.openzen.zenscript.shared.CodePosition;
import org.openzen.zenscript.shared.CompileException;
import org.openzen.zenscript.shared.CompileExceptionCode;

/**
 *
 * @author Hoofdgebruiker
 */
public class ParsedStatementBreak extends ParsedStatement {
	public final String name;
	
	public ParsedStatementBreak(CodePosition position, String name) {
		super(position);
		
		this.name = name;
	}

	@Override
	public Statement compile(StatementScope scope) {
		Statement target = scope.getLoop(name);
		if (target == null)
			throw new CompileException(position, CompileExceptionCode.BREAK_OUTSIDE_LOOP, name == null ? "Not in a loop" : "No such loop: " + name);
		return new BreakStatement(position, target);
	}
}