package com.saleoa.model;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_salary_config")
public class SalaryConfig {
	@Column(name="id", isPrimaryKey=true, isNotNull=true, jdbcType=JdbcType.INTEGER)
	private Long id;
	
	@Column(name="total_reach_goal_bonus", jdbcType=JdbcType.INTEGER)
	private Long totalReachGoalBonus;
	
	@Column(name="tax_rate", jdbcType=JdbcType.INTEGER)
	private Integer taxRate=0;
	
	@Column(name="tax_threshold", jdbcType=JdbcType.INTEGER)
	private Long taxThreshold;
	
	@Column(name="salary_start_day", jdbcType=JdbcType.INTEGER)
	private Integer salaryStartDay = 1;
	
	@Column(name="salary_end_day", jdbcType=JdbcType.INTEGER)
	private Integer salaryEndDay = 1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTotalReachGoalBonus() {
		return totalReachGoalBonus;
	}

	public void setTotalReachGoalBonus(Long totalReachGoalBonus) {
		this.totalReachGoalBonus = totalReachGoalBonus;
	}

	public Long getTaxThreshold() {
		return taxThreshold;
	}

	public void setTaxThreshold(Long taxThreshold) {
		this.taxThreshold = taxThreshold;
	}
	
	public Integer getSalaryStartDay() {
		return salaryStartDay;
	}

	public void setSalaryStartDay(Integer salaryStartDay) {
		this.salaryStartDay = salaryStartDay;
	}

	public Integer getSalaryEndDay() {
		return salaryEndDay;
	}

	public void setSalaryEndDay(Integer salaryEndDay) {
		this.salaryEndDay = salaryEndDay;
	}

	public Integer getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Integer taxRate) {
		this.taxRate = taxRate;
	}
}
