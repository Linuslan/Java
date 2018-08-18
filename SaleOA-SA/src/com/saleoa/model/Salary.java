package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_salary")
public class Salary {
	@Column(name="id", isPrimaryKey=true, isNotNull=true, jdbcType=JdbcType.INTEGER)
	private Long id;
	
	//用户id
	@Column(name="user_id", jdbcType=JdbcType.INTEGER)
	private Long userId;
	
	//用户名称
	@Column(name="user_name")
	private String userName;
	
	//年
	@Column(name="year", jdbcType=JdbcType.INTEGER)
	private Integer year;
	
	//月
	@Column(name="month", jdbcType=JdbcType.INTEGER)
	private Integer month;
	
	//工资金额，以分为单位，精确到小数点后两位
	@Column(name="money", jdbcType=JdbcType.INTEGER)
	private Long money;
	
	@Column(name="deduct_money", jdbcType=JdbcType.INTEGER)
	private Long deductMoney;
	
	//创建时间
	@Column(name="create_date")
	private Date createDate;
	
	//更新时间
	@Column(name="update_date")
	private Date updateDate;
	
	//状态，预留字段
	@Column(name="status", jdbcType=JdbcType.INTEGER)
	private Integer status;
	
	//删除状态，0：未删除；1：已删除；
	@Column(name="is_delete", jdbcType=JdbcType.INTEGER)
	private Integer isDelete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
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

	public Long getDeductMoney() {
		return deductMoney;
	}

	public void setDeductMoney(Long deductMoney) {
		this.deductMoney = deductMoney;
	}
}
