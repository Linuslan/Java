package com.saleoa.model;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

/**
 * 差额等级
 * @author 
 *
 */
@Table(name="tbl_oa_balance_level")
public class BalanceLevel {
	
	@Column(name="id", isPrimaryKey=true, isNotNull=true, jdbcType=JdbcType.INTEGER)
	private Long id;
	
	//奖金
	@Column(name="bonus", jdbcType=JdbcType.INTEGER)
	private Long bonus;
	
	//要求达到的经理数
	@Column(name="manager_count", jdbcType=JdbcType.INTEGER)
	private Integer managerCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBonus() {
		return bonus;
	}

	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}

	public Integer getManagerCount() {
		return managerCount;
	}

	public void setManagerCount(Integer managerCount) {
		this.managerCount = managerCount;
	}
}
