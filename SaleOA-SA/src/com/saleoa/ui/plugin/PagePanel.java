package com.saleoa.ui.plugin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.saleoa.base.IBaseService;
import com.saleoa.common.plugin.Page;

public class PagePanel<T> extends JPanel {
	private long totalPage = 1;
	private long currPage = 1;
	private int limit = 1;
	private IBaseService<T> baseService = null;
	private JGridPanel<T> parent = null;
	private Map<String, Object> paramMap = null;
	
	public PagePanel(JGridPanel<T> parent, IBaseService<T> baseService) {
		this.parent = parent;
		this.baseService = baseService;
	}
	
	public void refresh(Long currPage) {
		if(null != currPage) {
			this.currPage = currPage;
		}
		initPageBtn();
	}
	
	private void initPageBtn() {
		this.removeAll();
		this.updateUI();
		int pageBtnLimit = 10;
		JButton prePageBtn = new JButton("上一页");
		prePageBtn.addActionListener(new PageClickListener(this.baseService));
		prePageBtn.setName("prePage");
        JButton nextPageBtn = new JButton("下一页");
        nextPageBtn.addActionListener(new PageClickListener(this.baseService));
        nextPageBtn.setName("nextPage");
        JButton firstPageBtn = new JButton("首页");
        firstPageBtn.addActionListener(new PageClickListener(this.baseService));
        firstPageBtn.setName("firstPage");
        JButton lastPageBtn = new JButton("尾页");
        lastPageBtn.addActionListener(new PageClickListener(this.baseService));
        lastPageBtn.setName("lastPage");
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
        this.add(firstPageBtn);
        this.add(prePageBtn);
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
    			this.add(btn);
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
    			this.add(currLbl);
    		} else {
    			JButton btn = new JButton(String.valueOf(left));
    			btn.addActionListener(new PageClickListener(this.baseService));
    			btn.setName(String.valueOf(left));
    			this.add(btn);
    		}
    		
    	}
    	if(rightHide) {
    		JButton btn = new JButton("...");
			this.add(btn);
    	}
        this.add(nextPageBtn);
        this.add(lastPageBtn);
        this.setPreferredSize(new Dimension(0, 100));
        this.updateUI();
	}
	
	public IBaseService<T> getBaseService() {
		return baseService;
	}

	public void setBaseService(IBaseService<T> baseService) {
		this.baseService = baseService;
	}
	
	public void loadData(Long currPage) {
		try {
			if(null != currPage) {
				this.currPage = currPage;
			}
			Page<T> pageObj = baseService.selectPage(parent.getParamMap(), this.currPage, limit);
			parent.setData(pageObj.getData());
			parent.initGrid();
			totalPage = pageObj.getTotalPage();
			this.currPage = pageObj.getCurrPage();
			refresh(pageObj.getCurrPage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class PageClickListener implements ActionListener {

		private IBaseService<T> baseService = null;
		public PageClickListener(IBaseService<T> baseService) {
			this.baseService = baseService;
		}
		
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			JButton button = (JButton) event.getSource();
			String text = button.getName();
			long page = currPage;
			if("prePage".equals(text)) {
				page = (currPage - 1) < 1 ? 1 : currPage - 1;
			} else if("nextPage".equals(text)) {
				page = currPage + 1 > totalPage ? totalPage : currPage + 1;
			} else if("firstPage".equals(text)) {
				page = 1;
			} else if("lastPage".equals(text)) {
				page = totalPage;
			} else {
				page = Long.parseLong(text);
			}
			loadData(page);
		}
		
	}
	
}
