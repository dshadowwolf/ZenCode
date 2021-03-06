/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.ide;

import java.io.File;

/**
 * @author Hoofdgebruiker
 */
public class Arguments {
	public final File projectDirectory;
	public final String defaultTarget;

	public Arguments(String[] arguments) {
		File projectDir = null;
		String defaultTarget = null;

		int positional = 0;
		for (int i = 0; i < arguments.length; i++) {
			switch (positional) {
				case 0:
					projectDir = new File(arguments[0]);
					break;
				case 1:
					defaultTarget = arguments[1];
					break;
				default:
					throw new IllegalArgumentException("Too many arguments");
			}
			positional++;
		}

		if (projectDir.isFile()) // means we're opening a project file instead of a directory
			projectDir = projectDir.getParentFile();

		this.projectDirectory = projectDir;
		this.defaultTarget = defaultTarget;
	}
}
