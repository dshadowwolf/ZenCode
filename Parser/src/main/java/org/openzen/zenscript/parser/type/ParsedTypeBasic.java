/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.parser.type;

import org.openzen.zenscript.codemodel.type.BasicTypeID;
import org.openzen.zenscript.codemodel.type.ITypeID;
import org.openzen.zenscript.linker.BaseScope;

/**
 *
 * @author Hoofdgebruiker
 */
public enum ParsedTypeBasic implements IParsedType {
	VOID(BasicTypeID.VOID),
	ANY(BasicTypeID.ANY),
	BOOL(BasicTypeID.BOOL),
	BYTE(BasicTypeID.BYTE),
	SBYTE(BasicTypeID.SBYTE),
	SHORT(BasicTypeID.SHORT),
	USHORT(BasicTypeID.USHORT),
	INT(BasicTypeID.INT),
	UINT(BasicTypeID.UINT),
	LONG(BasicTypeID.LONG),
	ULONG(BasicTypeID.ULONG),
	FLOAT(BasicTypeID.FLOAT),
	DOUBLE(BasicTypeID.DOUBLE),
	CHAR(BasicTypeID.CHAR),
	STRING(BasicTypeID.STRING);
	
	private final BasicTypeID type;

	private ParsedTypeBasic(BasicTypeID type) {
		this.type = type;
	}
	
	@Override
	public ITypeID compile(BaseScope scope) {
		return type;
	}

	@Override
	public IParsedType withOptional() {
		return new ParsedOptionalBasicType(this);
	}

	@Override
	public IParsedType withModifiers(int modifiers) {
		throw new UnsupportedOperationException();
	}
}