package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_sale")
public class SaleLog {
	@Column(name="id", isPrimaryKey = true, jdbcType=JdbcType.INTEGER, isNotNull=true)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="name_en")
	private String nameEn;
	
	//售出的套数，即第几套
	@Column(name="sale_no", jdbcType=JdbcType.INTEGER)
	private Integer saleNo;
	
	@Column(name="employee_id", jdbcType=JdbcType.INTEGER)
	private Long employeeId;
	
	@Column(name="employee_name")
	private String employeeName;
	
	@Column(name="last_sale_id", jdbcType=JdbcType.INTEGER)
	private Long lastSaleId;
	
	@Column(name="last_sale_name")
	private String lastSaleName;
	
	@Column(name="sale_date")
	private Date saleDate;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="update_date")
	private Date updateDate;
	
	@Column(name="is_delete", defaultValue="0")
	private Integer isDelete=0;
	
	@Column(name="reward_points", jdbcType=JdbcType.INTEGER)
	private Long rewardPoints;
	
	@Column(name="salary", jdbcType=JdbcType.INTEGER)
	private Long salary;
	
	@Column(name="level_id", jdbcType=JdbcType.INTEGER)
	private Long levelId;
	
	@Column(name="level_name", jdbcType=JdbcType.TEXT)
	private String levelName;

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

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getLastSaleId() {
		return lastSaleId;
	}

	public void setLastSaleId(Long lastSaleId) {
		this.lastSaleId = lastSaleId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getLastSaleName() {
		return lastSaleName;
	}

	public void setLastSaleName(String lastSaleName) {
		this.lastSaleName = lastSaleName;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	
	public String toString() {
		return name;
	}

	public Integer getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(Integer saleNo) {
		this.saleNo = saleNo;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

	public Long getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
}
