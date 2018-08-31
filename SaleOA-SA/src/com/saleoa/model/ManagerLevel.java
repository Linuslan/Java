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
	
	//��������
	@Column(name="basic_salary", jdbcType=JdbcType.INTEGER)
	private Long basicSalary;
	
	@Column(name="min_sale", jdbcType=JdbcType.INTEGER)
	private Integer minSale;
	
	@Column(name="max_sale", jdbcType=JdbcType.INTEGER)
	private Integer maxSale;
	
	//���/��
	@Column(name="commission", jdbcType=JdbcType.INTEGER)
	private Long commission;
	
	//�����꽱
	@Column(name="reach_goal_bonus", jdbcType=JdbcType.INTEGER)
	private Long reachGoalBonus;
	
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

	public Long getReachGoalBonus() {
		return reachGoalBonus;
	}

	public void setReachGoalBonus(Long reachGoalBonus) {
		this.reachGoalBonus = reachGoalBonus;
	}
}
