package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_sale")
public class SaleLog {
	@Column(name="id", isPrimaryKey = true, jdbcType=JdbcType.INTEGER, isNotNull=true)
	private Long id;
	
	@Column(name="sale_id")
	private Long saleId;
	
	@Column(name="employee_id", jdbcType=JdbcType.INTEGER)
	private Long employeeId;
	
	@Column(name="employee_name")
	private String employeeName;
	
	@Column(name="salary", jdbcType=JdbcType.INTEGER)
	private Long salary;

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

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}
}
