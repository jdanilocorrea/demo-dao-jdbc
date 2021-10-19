package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		Scanner scn = new Scanner(System.in);

		System.out.println("==== TEST 1: seller findById =====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("==== TEST 2: seller findByDepartment =====");
		Department department = new Department(2, null);
		List<Seller> listSeller = sellerDao.findByDepartment(department);
		for (Seller sel : listSeller) {
			System.out.println(sel);
		}

		System.out.println("==== TEST 3: seller findAll =====");
		listSeller = sellerDao.findAll();
		for (Seller sel : listSeller) {
			System.out.println(sel);
		}
		/*
		 * System.out.println("==== TEST 4: seller insert ====="); Seller newSeller =
		 * new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		 * sellerDao.insert(newSeller); System.out.println("Inserted! New id = " +
		 * newSeller.getId());
		 */

		System.out.println("==== TEST 5: seller update =====");
		seller = sellerDao.findById(1);
		seller.setName("Melissa Oliveira");
		seller.setEmail("melissaoliveira@hotmail.com");
		sellerDao.update(seller);
		System.out.println("Update completed!");

		System.out.println("==== TEST 6: seller delete =====");
		System.out.println("Enter id for delete test:");
		int id = scn.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed");
		scn.close();

	}
}
