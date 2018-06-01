/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.drawablegui.dimension;

import org.openzen.drawablegui.DUIContext;

/**
 *
 * @author Hoofdgebruiker
 */
public class DPxDimension implements DDimension {
	private final float pixels;
	
	public DPxDimension(float pixels) {
		this.pixels = pixels;
	}

	@Override
	public float eval(DUIContext context) {
		return pixels;
	}
}
