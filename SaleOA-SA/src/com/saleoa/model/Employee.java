package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_employee")
public class Employee {
	@Column(name="id", isPrimaryKey = true, jdbcType=JdbcType.INTEGER, isNotNull=true)
	private Long id;
	
	//名字
	@Column(name="name")
	private String name;
	
	@Column(name="name_en")
	private String nameEn;
	
	@Column(name="register_date")
	private Date registerDate;
	
	//创建时间
	@Column(name="create_date")
	private Date createDate;
	
	//更新时间
	@Column(name="update_date")
	private Date updateDate;
	
	//离职时间
	@Column(name="fire_date")
	private Date fireDate;
	
	//当前在职状态
	@Column(name="status", jdbcType=JdbcType.INTEGER, defaultValue="0")
	private Integer status;
	
	//是否删除，0：未删除；1：已删除；
	@Column(name="is_delete", jdbcType=JdbcType.INTEGER, defaultValue="0")
	private Integer isDelete;
	
	//上级id
	@Column(name="leader_id", jdbcType=JdbcType.INTEGER)
	private Long leaderId;
	
	@Column(name="leader_name")
	private String leaderName;
	
	@Column(name="employee_role_id", jdbcType=JdbcType.INTEGER)
	private Long employeeRoleId;
	
	@Column(name="employee_role_name", jdbcType=JdbcType.TEXT)
	private String employeeRoleName;
	
	@Column(name="id_number")
	private String idNumber;
	
	@Column(name="address")
	private String address;
	
	@Column(name="inheritor")
	private String inheritor;
	
	@Column(name="inheritor_phone")
	private String inheritorPhone;
	
	@Column(name="department_id", jdbcType=JdbcType.INTEGER)
	private Long departmentId;
	
	@Column(name="department_name")
	private String departmentName;
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

	public Long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public String toString() {
		return name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Date getFireDate() {
		return fireDate;
	}

	public void setFireDate(Date fireDate) {
		this.fireDate = fireDate;
	}

	public Long getEmployeeRoleId() {
		return employeeRoleId;
	}

	public void setEmployeeRoleId(Long employeeRoleId) {
		this.employeeRoleId = employeeRoleId;
	}

	public String getEmployeeRoleName() {
		return employeeRoleName;
	}

	public void setEmployeeRoleName(String employeeRoleName) {
		this.employeeRoleName = employeeRoleName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInheritor() {
		return inheritor;
	}

	public void setInheritor(String inheritor) {
		this.inheritor = inheritor;
	}

	public String getInheritorPhone() {
		return inheritorPhone;
	}

	public void setInheritorPhone(String inheritorPhone) {
		this.inheritorPhone = inheritorPhone;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
