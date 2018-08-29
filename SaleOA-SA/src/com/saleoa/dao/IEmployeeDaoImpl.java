package com.saleoa.dao;

import java.util.ArrayList;
import java.util.List;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.constant.EmployeeRoleConst;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Employee;

public class IEmployeeDaoImpl extends IBaseDaoImpl<Employee> implements IEmployeeDao {
	
	/**
	 * 查询部门的领导人
	 * @param departmentId
	 * @return
	 */
	public List<Employee> selectManagerByDepartment(Long departmentId) {
		List<Employee> list = new ArrayList<Employee> ();
		String sql = "SELECT * FROM tbl_oa_employee WHERE department_id="+departmentId.longValue()+" AND employee_role_id="+EmployeeRoleConst.MANAGER.longValue();
		try {
			List<Employee> managers = JdbcHelper.select(sql, Employee.class);
			if(null != managers && managers.size() > 0) {
				list.addAll(managers);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
