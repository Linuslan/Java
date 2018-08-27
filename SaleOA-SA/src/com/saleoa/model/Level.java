package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_level")
public class Level {
	@Column(name="id", isPrimaryKey=true, isNotNull=true, jdbcType=JdbcType.INTEGER)
	private Long id;
	@Column(name="name")
	private String name;
	//升级需要的积分
	@Column(name="min_point", jdbcType=JdbcType.INTEGER)
	private Long minPoint;
	@Column(name="max_point", jdbcType=JdbcType.INTEGER)
	private Long maxPoint;
	//等级字段
	@Column(name="level", jdbcType=JdbcType.INTEGER)
	private Integer level;
	@Column(name="create_date")
	private Date createDate;
	//等级对应的奖金
	@Column(name="bonus", jdbcType=JdbcType.INTEGER)
	private Long bonus;
	
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
	public Long getMinPoint() {
		return minPoint;
	}
	public void setMinPoint(Long minPoint) {
		this.minPoint = minPoint;
	}
	public Long getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(Long maxPoint) {
		this.maxPoint = maxPoint;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getBonus() {
		return bonus;
	}
	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
}
