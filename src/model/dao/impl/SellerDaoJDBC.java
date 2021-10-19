package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {

		this.conn = conn;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement pst = null;
		
		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES "
					+"(?,?,?,?,?)", 
					Statement.RETURN_GENERATED_KEYS);
//			@formatter:on
			
			pst.setString(1, seller.getName());
			pst.setString(2, seller.getEmail());
			pst.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			pst.setDouble(4, seller.getBaseSalary());
			pst.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally{
			DB.closeStatement(pst);
			
		}

	}

	@Override
	public void update(Seller seller) {
		
PreparedStatement pst = null;
		
		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
//			@formatter:on
			
			pst.setString(1, seller.getName());
			pst.setString(2, seller.getEmail());
			pst.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			pst.setDouble(4, seller.getBaseSalary());
			pst.setInt(5, seller.getDepartment().getId());
			pst.setInt(6, seller.getId());
			
			pst.executeUpdate();

			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally{
			DB.closeStatement(pst);
			
		}

	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE seller.Id = ?");
//			@formatter:on
			
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				Department department = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, department);
				return seller;
			}

			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {

			DB.closeStatement(pst);
			DB.closeResultSet(rs);

		}

	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"SELECT seller.*,department.Name DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.id "
					+ "ORDER BY Name");
//			@formatter:on
			
			rs = pst.executeQuery();		
			List<Seller> listSeller = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				
				Seller sel = instantiateSeller(rs, dep);
				listSeller.add(sel);				
			}
			return listSeller;
		} 
		catch (SQLException e) {
			
			throw new DbException(e.getMessage());
		
		}
		finally {
			
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
			
		}
		
		
		
	}

	private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(department);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("DepartmentId"));
		department.setName(rs.getString("DepName"));
		return department;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName  "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE  DepartmentId = ? "
					+ "ORDER BY Name");
//			@formatter:on

			pst.setInt(1, department.getId());
			rs = pst.executeQuery();
			
			List<Seller> listSeller = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				
				Seller sel = instantiateSeller(rs, dep);
				listSeller.add(sel);				
			}
			return listSeller;
		
		}
		catch (SQLException e) {

			throw new DbException(e.getMessage());

		}
		finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

}
