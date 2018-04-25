/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.codemodel.type;

/**
 *
 * @author Hoofdgebruiker
 */
public interface ITypeVisitor<T> {
	public T visitBasic(BasicTypeID basic);
	
	public T visitArray(ArrayTypeID array);
	
	public T visitAssoc(AssocTypeID assoc);
	
	public T visitIterator(IteratorTypeID iterator);
	
	public T visitFunction(FunctionTypeID function);
	
	public T visitDefinition(DefinitionTypeID definition);
	
	public T visitGeneric(GenericTypeID generic);
	
	public T visitRange(RangeTypeID range);
	
	public T visitConst(ConstTypeID type);
	
	public T visitOptional(OptionalTypeID optional);
}