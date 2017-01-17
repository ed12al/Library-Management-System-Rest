package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;

public class BranchDAO extends BaseDAO implements RowMapper<Branch>{
	
	public void addBranch(Branch branch) throws SQLException {
		checkBranch(branch);
		template.update("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)", 
				new Object[] { branch.getBranchName(), branch.getBranchAddress() });
	}
	
	public void addAllBookCopiesByBranch(Branch branch) throws SQLException {
		if(branch.getBookCopy() != null){
			for(BookCopy bookCopy : branch.getBookCopy()){
				Integer noOfCopy = bookCopy.getNoOfCopies() == null || bookCopy.getNoOfCopies() < 0 ? 0 : bookCopy.getNoOfCopies();
				template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", new Object[] { bookCopy.getBook().getBookId(), branch.getBranchId(), noOfCopy });
			}
		}
	}
	
	public void updateBranch(Branch branch) throws SQLException {
		checkBranch(branch);
		template.update("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] { branch.getBranchName(), branch.getBranchAddress(), branch.getBranchId() });
	}
	
	public void updateBranchWithDetails(Branch branch) throws SQLException {
		updateBranch(branch);
		updateAllBookCopiesByBranch(branch);
	}
	
	public void updateAllBookCopiesByBranch(Branch branch) throws SQLException {
		deleteAllBookCopiesByBranch(branch);
		addAllBookCopiesByBranch(branch);
	}
	
	public void deleteBranch(Branch branch) throws SQLException {
		template.update("delete from tbl_library_branch where branchId = ?", new Object[] { branch.getBranchId() });
	}
	
	public void deleteAllBookCopiesByBranch(Branch branch) throws SQLException {
		template.update("delete from tbl_book_copies where branchId = ?", new Object[] { branch.getBranchId() });
	}
	
	public List<Branch> readAllBranches(String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_library_branch", this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_library_branch where branchName like ? ", new Object[]{q}, this);
		}
	}
	
	public List<Branch> readAllBranchesWithPageNo(Integer pageNo, Integer pageSize, String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_library_branch limit ? , ?", new Object[] { (pageNo-1)*pageSize, pageSize }, this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_library_branch where branchName like ? limit ? , ?", new Object[]{q, (pageNo-1)*pageSize, pageSize }, this);
		}
	}
	
	public Integer getBranchesCount(String q) throws SQLException{
		if(q==null||q.trim().length()==0){
			return template.queryForObject("select count(*) AS COUNT from tbl_library_branch", Integer.class);
		}else{
			q = "%"+q+"%";
			return template.queryForObject("select count(*) AS COUNT from tbl_library_branch where branchName like ?", new Object[]{q}, Integer.class);
		}
	}
	
	public Branch readBranchById(Branch branch) throws SQLException{
		return template.queryForObject("select * from tbl_library_branch where branchId = ?", new Object[]{ branch.getBranchId() }, this);
	}

	private void checkBranch(Branch branch){
		if(branch.getBranchName() != null && branch.getBranchName().trim().length() == 0)	branch.setBranchName(null);
		if(branch.getBranchAddress() != null && branch.getBranchAddress().trim().length() == 0)	branch.setBranchAddress(null);
	}
	
	@Override
	public Branch mapRow(ResultSet rs, int row) throws SQLException {
		Branch b = new Branch();
		b.setBranchId(rs.getInt("branchId"));
		b.setBranchAddress(rs.getString("branchAddress"));
		b.setBranchName(rs.getString("branchName"));
		return b;
	}

}
