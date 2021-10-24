package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement pst = null;

		try {
//			@formatter:0ff
			pst = conn.prepareStatement("INSERT INTO department " + "(NAME) " + "VALUES " + "(?) ",
					Statement.RETURN_GENERATED_KEYS);
			//			@formatter:on

			pst.setString(1, department.getName());

			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}

	}

	@Override
	public void update(Department department) {
		PreparedStatement pst = null;
		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"UPDATE department "
					+"SET Name = ? "
					+"WHERE Id = ?");
//			@formatter:on

			pst.setString(1, department.getName());
			pst.setInt(2, department.getId());

			pst.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
//			@formatter:off
			pst = conn.prepareStatement(
					"SELECT * FROM department "
					+"WHERE Id = ?");		
//			@formatter:on

			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				Department department = instantiateDepartment(rs);
				return department;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("Id"));
		department.setName(rs.getString("Name"));
		return department;
	}

	@Override
	public List<Department> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
