package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gcit.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO implements RowMapper<Borrower>{

	public void addBorrower(Borrower borrower) throws SQLException {
		checkBorrower(borrower);
		template.update("insert into tbl_borrower (name, address, phone) values (?, ?, ?)", 
				new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone() });
	}

	public void updateBorrower(Borrower borrower) throws SQLException {
		checkBorrower(borrower);
		template.update("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo() });
	}

	public void deleteBorrower(Borrower borrower) throws SQLException {
		template.update("delete from tbl_borrower where cardNo = ?", 
				new Object[] { borrower.getCardNo() });
	}

	public List<Borrower> readAllBorrowers(String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_borrower", this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_borrower where name like ? ", new Object[]{q}, this);
		}
	}
	
	public List<Borrower> readAllBorrowersWithPageNo(Integer pageNo, Integer pageSize, String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_borrower limit ? , ?", new Object[] { (pageNo-1)*pageSize, pageSize }, this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_borrower where name like ? limit ? , ?", new Object[]{q, (pageNo-1)*pageSize, pageSize }, this);
		}
	}
	
	public Integer getBorrowersCount(String q) throws SQLException{
		if(q==null||q.trim().length()==0){
			return template.queryForObject("select count(*) AS COUNT from tbl_borrower", Integer.class);
		}else{
			q = "%"+q+"%";
			return template.queryForObject("select count(*) AS COUNT from tbl_borrower where name like ?", new Object[]{q}, Integer.class);
		}
	}
	
	public Borrower readBorrowerById(Borrower borrower) throws SQLException{
		return template.queryForObject("select * from tbl_borrower where cardNo = ?", new Object[] { borrower.getCardNo() }, this);
	}
	
	private void checkBorrower(Borrower borrower){
		if(borrower.getName() != null && borrower.getName().trim().length() == 0)	borrower.setName(null);
		if(borrower.getAddress() != null && borrower.getAddress().trim().length() == 0)	borrower.setAddress(null);
		if(borrower.getPhone() != null && borrower.getPhone().trim().length() == 0)	borrower.setPhone(null);
	}

	@Override
	public Borrower mapRow(ResultSet rs, int row) throws SQLException {
		Borrower b = new Borrower();
		b.setCardNo(rs.getInt("cardNo"));
		b.setName(rs.getString("name"));
		b.setAddress(rs.getString("address"));
		b.setPhone(rs.getString("phone"));
		return b;
	}

}
