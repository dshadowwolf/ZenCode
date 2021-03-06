/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.ide.ui.view.project;

import live.MutableLiveBool;
import org.openzen.drawablegui.tree.DTreeNode;

/**
 * @author Hoofdgebruiker
 */
public abstract class ProjectOverviewNode implements DTreeNode<ProjectOverviewNode> {
	private final MutableLiveBool collapsed;

	public ProjectOverviewNode(MutableLiveBool collapsed) {
		this.collapsed = collapsed;
	}

	public abstract Kind getKind();

	@Override
	public MutableLiveBool isCollapsed() {
		return collapsed;
	}

	public enum Kind {
		ROOT,
		PROJECT,
		LIBRARY,
		MODULE,
		PACKAGE,
		SCRIPT
	}
}
