package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentDao =  DaoFactory.createDepartmentDao();
		
//		System.out.println("==== TEST 1: department insert ====");
//		Department newDepartment = new Department(null, "Gamers");
//		departmentDao.insert(newDepartment);
//		System.out.println("Inserted! New id = " + newDepartment.getId());
		
		System.out.println("==== TEST 2: Department findById =====");
		Department findIdDepartment = departmentDao.findById(4);
		System.out.println(findIdDepartment);
		
		System.out.println("==== TEST 3: Department update ====");
		Department updateDepartment = departmentDao.findById(6);
		updateDepartment.setName("RH");
		departmentDao.update(updateDepartment);
		System.out.println("Update completed!");
		
		System.out.println("==== TEST 4: Department findAll ====");
		List<Department> findAllDepartment = departmentDao.findAll();
		for(Department department : findAllDepartment) {
			System.out.println(department);
		}
		
	}

}
