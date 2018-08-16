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
	 * ��compX��compY������Ϊ���գ����ɶ�Ӧ������
	 * @param compX �Ը������x����Ϊ���յ���������x
	 * @param compY �Ը������y����Ϊ���յ���������y
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
