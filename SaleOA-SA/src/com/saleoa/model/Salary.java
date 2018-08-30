package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_salary")
public class Salary {
	@Column(name="id", isPrimaryKey=true, isNotNull=true, jdbcType=JdbcType.INTEGER)
	private Long id;
	
	//�û�id
	@Column(name="user_id", jdbcType=JdbcType.INTEGER)
	private Long userId;
	
	//�û�����
	@Column(name="user_name")
	private String userName;
	
	//��
	@Column(name="year", jdbcType=JdbcType.INTEGER)
	private Integer year;
	
	//��
	@Column(name="month", jdbcType=JdbcType.INTEGER)
	private Integer month;
	
	@Column(name="department_id", jdbcType=JdbcType.INTEGER)
	private Long departmentId;
	
	@Column(name="department_name")
	private String departmentName;
	
	//���ʽ��Է�Ϊ��λ����ȷ��С�������λ
	@Column(name="money", jdbcType=JdbcType.INTEGER)
	private Long money;
	
	//�ۿ�
	@Column(name="deduct_money", jdbcType=JdbcType.INTEGER)
	private Long deductMoney;
	
	//����ʱ��
	@Column(name="create_date")
	private Date createDate;
	
	//����ʱ��
	@Column(name="update_date")
	private Date updateDate;
	
	//״̬��Ԥ���ֶ�
	@Column(name="status", jdbcType=JdbcType.INTEGER)
	private Integer status;
	
	//ɾ��״̬��0��δɾ����1����ɾ����
	@Column(name="is_delete", jdbcType=JdbcType.INTEGER)
	private Integer isDelete;
	
	@Column(name="memo", length=4000)
	private String memo;
	
	//���չ���
	@Column(name="total_money", jdbcType=JdbcType.INTEGER)
	private Long totalMoney;
	
	//3500���Ͽ�˰20%
	@Column(name="tax", jdbcType=JdbcType.INTEGER)
	private Long tax=0l;
	
	//��꽱
	@Column(name="reach_goal_bonus", jdbcType=JdbcType.INTEGER)
	private Long reachGoalBonus;
	
	//��곬�
	@Column(name="over_goal_bonus", jdbcType=JdbcType.INTEGER)
	private Long overGoalBonus;
	
	//���ڹ���
	@Column(name="office_manage_bonus", jdbcType=JdbcType.INTEGER)
	private Long officeManageBonus;
	
	//���ڽ�
	@Column(name="full_duty_bonus", jdbcType=JdbcType.INTEGER)
	private Long fullDutyBonus;
	
	//�ܴ�꽱
	@Column(name="total_reach_goal_bonus", jdbcType=JdbcType.INTEGER)
	private Long totalReachGoalBonus;
	
	//����
	@Column(name="amercement", jdbcType=JdbcType.INTEGER)
	private Long amercement;
	
	//��˾���
	@Column(name="company_lend", jdbcType=JdbcType.INTEGER)
	private Long companyLend;
	
	@Column(name="supposed_money", jdbcType=JdbcType.INTEGER)
	private Long supposedMoney;

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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Long totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Long getTax() {
		return tax;
	}

	public void setTax(Long tax) {
		this.tax = tax;
	}

	public Long getReachGoalBonus() {
		return reachGoalBonus;
	}

	public void setReachGoalBonus(Long reachGoalBonus) {
		this.reachGoalBonus = reachGoalBonus;
	}

	public Long getOverGoalBonus() {
		return overGoalBonus;
	}

	public void setOverGoalBonus(Long overGoalBonus) {
		this.overGoalBonus = overGoalBonus;
	}

	public Long getOfficeManageBonus() {
		return officeManageBonus;
	}

	public void setOfficeManageBonus(Long officeManageBonus) {
		this.officeManageBonus = officeManageBonus;
	}

	public Long getFullDutyBonus() {
		return fullDutyBonus;
	}

	public void setFullDutyBonus(Long fullDutyBonus) {
		this.fullDutyBonus = fullDutyBonus;
	}

	public Long getTotalReachGoalBonus() {
		return totalReachGoalBonus;
	}

	public void setTotalReachGoalBonus(Long totalReachGoalBonus) {
		this.totalReachGoalBonus = totalReachGoalBonus;
	}

	public Long getAmercement() {
		return amercement;
	}

	public void setAmercement(Long amercement) {
		this.amercement = amercement;
	}

	public Long getCompanyLend() {
		return companyLend;
	}

	public void setCompanyLend(Long companyLend) {
		this.companyLend = companyLend;
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

	public Long getSupposedMoney() {
		return supposedMoney;
	}

	public void setSupposedMoney(Long supposedMoney) {
		this.supposedMoney = supposedMoney;
	}
}
