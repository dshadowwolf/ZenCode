/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.type.storage;

import org.openzen.zencode.shared.CodePosition;
import org.openzen.zencode.shared.CompileExceptionCode;

/**
 *
 * @author Hoofdgebruiker
 */
public class SharedStorageType implements StorageType {
	public static final SharedStorageType INSTANCE = new SharedStorageType();
	
	private SharedStorageType() {}

	@Override
	public String getName() {
		return "shared";
	}

	@Override
	public StorageTag instance(CodePosition position, String[] arguments) {
		if (arguments.length > 0)
			return new InvalidStorageTag(position, CompileExceptionCode.INVALID_STORAGE_TYPE_ARGUMENTS, "shared storage type doesn't take arguments");
		
		return SharedStorageTag.INSTANCE;
	}
}