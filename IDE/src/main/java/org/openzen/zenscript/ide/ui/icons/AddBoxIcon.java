/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zenscript.ide.ui.icons;

import org.openzen.drawablegui.DPath;
import org.openzen.drawablegui.DTransform2D;
import org.openzen.drawablegui.DColorableIcon;
import org.openzen.drawablegui.draw.DDrawTarget;

public class AddBoxIcon implements DColorableIcon {
	public static final AddBoxIcon INSTANCE = new AddBoxIcon();
	public static final ColoredIcon BLACK = new ColoredIcon(INSTANCE, 0xFF000000);
	public static final ColoredIcon BLUE = new ColoredIcon(INSTANCE, 0xFF1B1464);
	public static final ColoredIcon GREEN = new ColoredIcon(INSTANCE, 0xFF006266);
	public static final ColoredIcon ORANGE = new ColoredIcon(INSTANCE, 0xFFEE5A24);
	public static final ColoredIcon GRAY = new ColoredIcon(INSTANCE, 0xFF888888);
	private static final DPath PATH = tracer -> {
		tracer.moveTo(19f, 3f);
		tracer.lineTo(5.0f, 3.0f);
		tracer.bezierCubic(3.8899999f, 3.0f, 3.0f, 3.9f, 3.0f, 5.0f);
		tracer.lineTo(3.0f, 19.0f);
		tracer.bezierCubic(3.0f, 20.1f, 3.8899999f, 21.0f, 5.0f, 21.0f);
		tracer.lineTo(19.0f, 21.0f);
		tracer.bezierCubic(20.1f, 21.0f, 21.0f, 20.1f, 21.0f, 19.0f);
		tracer.lineTo(21.0f, 5.0f);
		tracer.bezierCubic(21.0f, 3.9f, 20.1f, 3.0f, 19.0f, 3.0f);
		tracer.close();
		tracer.moveTo(17.0f, 13.0f);
		tracer.lineTo(13.0f, 13.0f);
		tracer.lineTo(13.0f, 17.0f);
		tracer.lineTo(11.0f, 17.0f);
		tracer.lineTo(11.0f, 13.0f);
		tracer.lineTo(7.0f, 13.0f);
		tracer.lineTo(7.0f, 11.0f);
		tracer.lineTo(11.0f, 11.0f);
		tracer.lineTo(11.0f, 7.0f);
		tracer.lineTo(13.0f, 7.0f);
		tracer.lineTo(13.0f, 11.0f);
		tracer.lineTo(17.0f, 11.0f);
		tracer.lineTo(17.0f, 13.0f);
		tracer.close();
	};

	private AddBoxIcon() {
	}

	@Override
	public void draw(DDrawTarget target, int z, DTransform2D transform, int color) {
		target.fillPath(z, PATH, transform, color);
	}

	@Override
	public float getNominalWidth() {
		return 24;
	}

	@Override
	public float getNominalHeight() {
		return 24;
	}
}
