package com.saleoa.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.cache.DataCache;
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
		try {
			List<Employee> allEmployees = (List<Employee>) DataCache.selectAll(this.getKey());
			Iterator<Employee> iter = allEmployees.iterator();
			while(iter.hasNext()) {
				Employee emp = iter.next();
				if(emp.getDepartmentId().longValue() == departmentId.longValue()
						&& emp.getEmployeeRoleId().longValue() == EmployeeRoleConst.MANAGER.longValue()) {
					list.add(emp);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		/*String sql = "SELECT * FROM tbl_oa_employee WHERE department_id="+departmentId.longValue()+" AND employee_role_id="+EmployeeRoleConst.MANAGER.longValue();
		try {
			List<Employee> managers = JdbcHelper.select(sql, Employee.class);
			if(null != managers && managers.size() > 0) {
				list.addAll(managers);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return list;
	}
}
