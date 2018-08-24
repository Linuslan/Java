package com.saleoa.ui.plugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PagePanel extends JPanel {
	private long totalPage = 12;
	private long currPage = 8;
	
	public void refresh(long currPage, long totalPage) {
		this.currPage = currPage;
		this.totalPage = totalPage;
		initPageBtn();
	}
	
	private void initPageBtn() {
		int pageBtnLimit = 10;
		JButton prePageBtn = new JButton("上一页");
        JButton nextPageBtn = new JButton("下一页");
        JButton firstPageBtn = new JButton("首页");
        JButton lastPageBtn = new JButton("尾页");
        JPanel pageBtnPanel = new JPanel();
        pageBtnPanel.setBackground(Color.WHITE);
        pageBtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
        pageBtnPanel.add(firstPageBtn);
        pageBtnPanel.add(prePageBtn);
        boolean leftHide = false;
        boolean rightHide = false;
        long left = currPage - 4 < 1 ? 1 : currPage - 4;
    	long right = (currPage + 4) > totalPage ? totalPage : (currPage + 4);
    	if(right >= totalPage) {
    		left = right - currPage <= 4 ? left - (5-(right-currPage)) : left;
        	left = left < 1 ? 1 : left;
    	}
    	if(left <= 1) {
    		right = currPage - left <= 4 ? right + (5-(currPage-left)) : right;
        	right = right > totalPage ? totalPage : right;
    	}
    	if(totalPage > pageBtnLimit) {
    		if(left > 1) {
        		leftHide = true;
        	}
        	
        	if(totalPage > right) {
        		rightHide = true;
        	}
        	if(leftHide) {
        		JButton btn = new JButton("...");
    			pageBtnPanel.add(btn);
        	}
    	} else {
    		right = totalPage;
    		left = 1;
    	}
    	
    	if(left < 1) {
    		left = 1;
    	}
    	if(right > totalPage) {
    		right = totalPage;
    	}
    	for(;left <= right; left++) {
    		if(left == currPage) {
    			JLabel currLbl = new JLabel(String.valueOf(left));
    			pageBtnPanel.add(currLbl);
    		} else {
    			JButton btn = new JButton(String.valueOf(left));
    			pageBtnPanel.add(btn);
    		}
    		
    	}
    	if(rightHide) {
    		JButton btn = new JButton("...");
			pageBtnPanel.add(btn);
    	}
        pageBtnPanel.add(nextPageBtn);
        pageBtnPanel.add(lastPageBtn);
        pageBtnPanel.setPreferredSize(new Dimension(0, 100));
        this.add(pageBtnPanel, BorderLayout.SOUTH);
	}
	
}
