package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.utils.JdbcType;

@Table(name="tbl_oa_employee")
public class Employee {
	@Column(name="id", isPrimaryKey = true, jdbcType=JdbcType.INTEGER, isNotNull=true)
	private Long id;
	
	//名字
	@Column(name="name")
	private String name;
	
	//当前等级
	@Column(name="level", jdbcType=JdbcType.INTEGER)
	private Integer level;
	
	//积分
	@Column(name="reward_points", jdbcType=JdbcType.INTEGER, length=4000)
	private Long rewardPoints;
	
	//工资，以分为单位，精确到小数点后两位
	@Column(name="salary", jdbcType=JdbcType.INTEGER, length=4000)
	private Long salary;
	
	//创建时间
	@Column(name="create_date")
	private Date createDate;
	
	//更新时间
	@Column(name="update_date")
	private Date updateDate;
	
	//当前在职状态
	@Column(name="status", jdbcType=JdbcType.INTEGER, defaultValue="0")
	private Integer status;
	
	//是否删除，0：未删除；1：已删除；
	@Column(name="is_delete", jdbcType=JdbcType.INTEGER, defaultValue="0")
	private Integer isDelete;
	
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
