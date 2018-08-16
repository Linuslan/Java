package com.saleoa.model;

import java.util.Date;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.common.constant.JdbcType;

@Table(name="tbl_oa_employee")
public class Employee {
	@Column(name="id", isPrimaryKey = true, jdbcType=JdbcType.INTEGER, isNotNull=true)
	private Long id;
	
	//����
	@Column(name="name")
	private String name;
	
	//��ǰ�ȼ�
	@Column(name="level", jdbcType=JdbcType.INTEGER)
	private Integer level;
	
	@Column(name="level_name")
	private String levelName;
	
	//����
	@Column(name="reward_points", jdbcType=JdbcType.INTEGER, length=4000)
	private Long rewardPoints;
	
	//���ʣ��Է�Ϊ��λ����ȷ��С�������λ
	@Column(name="salary", jdbcType=JdbcType.INTEGER, length=4000)
	private Long salary;
	
	@Column(name="register_date")
	private Date registerDate;
	
	//����ʱ��
	@Column(name="create_date")
	private Date createDate;
	
	//����ʱ��
	@Column(name="update_date")
	private Date updateDate;
	
	//��ǰ��ְ״̬
	@Column(name="status", jdbcType=JdbcType.INTEGER, defaultValue="0")
	private Integer status;
	
	//�Ƿ�ɾ����0��δɾ����1����ɾ����
	@Column(name="is_delete", jdbcType=JdbcType.INTEGER, defaultValue="0")
	private Integer isDelete;
	
	//������id
	@Column(name="introducer_id", jdbcType=JdbcType.INTEGER)
	private Long introducerId;
	
	@Column(name="introducer_name")
	private String introducerName;
	
	//�ϼ�id
	@Column(name="leader_id", jdbcType=JdbcType.INTEGER)
	private Long leaderId;
	
	@Column(name="leader_name")
	private String leaderName;
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

	public Long getIntroducerId() {
		return introducerId;
	}

	public void setIntroducerId(Long introducerId) {
		this.introducerId = introducerId;
	}

	public Long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getIntroducerName() {
		return introducerName;
	}

	public void setIntroducerName(String introducerName) {
		this.introducerName = introducerName;
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
}
