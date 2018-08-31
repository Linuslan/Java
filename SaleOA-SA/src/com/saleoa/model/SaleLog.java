package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_sale_log")
public class SaleLog {
	
	@Column(name="id", isPrimaryKey = true, jdbcType=JdbcType.INTEGER, isNotNull=true)
	private Long id;
	
	@Column(name="sale_id", jdbcType=JdbcType.INTEGER)
	private Long saleId;
	
	@Column(name="employee_id", jdbcType=JdbcType.INTEGER)
	private Long employeeId;
	
	@Column(name="employee_name")
	private String employeeName;
	
	@Column(name="bonus", jdbcType=JdbcType.INTEGER)
	private Long bonus;
	
	@Column(name="sale_date")
	private Date saleDate;
	
	@Column(name="create_date")
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getBonus() {
		return bonus;
	}

	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
