package com.saleoa.ui.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public class JGridPanel<T> extends JPanel {
	protected List<T> data = new ArrayList<T> ();
	protected Map<String, Object> paramMap = new HashMap<String, Object>();
	public void initGrid() {
		
	}
	
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
}
