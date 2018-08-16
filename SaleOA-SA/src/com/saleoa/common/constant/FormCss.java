package com.saleoa.common.constant;

import java.awt.Point;

import javax.swing.JComponent;

public class FormCss {
	public final static int HEIGHT = 25;
	public final static int LABEL_WIDTH = 80;
	public final static int FORM_WIDTH = 200;
	public final static int START_X = 25;
	public final static int START_Y = 25;
	public final static int MARGIN_TOP = 10;
	public final static int MARGIN_LEFT = 20;
	
	/**
	 * 以compX和compY的坐标为参照，生成对应的坐标
	 * @param compX 以该组件的x坐标为参照点生成坐标x
	 * @param compY 以该组件的y坐标为参照点生成坐标y
	 * @return
	 */
	public static Point getLocation(JComponent compX, JComponent compY) {
		int x = START_X;
		int y = START_Y;
		if(null != compX) {
			x = compX.getLocation().x+FormCss.LABEL_WIDTH + MARGIN_LEFT;
		}
		if(null != compY) {
			y = compY.getLocation().y + FormCss.HEIGHT + MARGIN_TOP;
		}
		Point p = new Point(x, y);
		return p;
	}
}
