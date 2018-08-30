package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_manager_level")
public class ManagerLevel {
	
	@Column(name="id", isPrimaryKey=true, isNotNull=true, jdbcType=JdbcType.INTEGER)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	//基础工资
	@Column(name="basic_salary")
	private Long basicSalary;
	
	@Column(name="min_sale")
	private Integer minSale;
	
	@Column(name="max_sale")
	private Integer maxSale;
	
	//抽成/套
	@Column(name="commission")
	private Long commission;
	
	//超额达标奖
	@Column(name="over_reach_goal_bonus")
	private Long overGoalBonus;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="create_date")
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Long basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Integer getMinSale() {
		return minSale;
	}

	public void setMinSale(Integer minSale) {
		this.minSale = minSale;
	}

	public Integer getMaxSale() {
		return maxSale;
	}

	public void setMaxSale(Integer maxSale) {
		this.maxSale = maxSale;
	}

	public Long getCommission() {
		return commission;
	}

	public void setCommission(Long commission) {
		this.commission = commission;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getOverGoalBonus() {
		return overGoalBonus;
	}

	public void setOverGoalBonus(Long overGoalBonus) {
		this.overGoalBonus = overGoalBonus;
	}
}
