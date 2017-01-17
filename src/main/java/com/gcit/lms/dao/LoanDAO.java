package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gcit.lms.entity.Loan;
import com.gcit.lms.common.Constants;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;

public class LoanDAO extends BaseDAO implements RowMapper<Loan>{

	public void addLoan(Loan loan) throws SQLException {
		Date today = new Date();
		template.update("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values (?, ?, ?, ?, ?)", 
				new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo(), new java.sql.Date(today.getTime()), new java.sql.Date(today.getTime()+Constants.ONE_WEEK) });
	}
	
	public void updateDueDate(Loan loan) throws SQLException {
		java.sql.Date dueDate = new java.sql.Date(loan.getDueDate().getTime());
		template.update("update tbl_book_loans set dueDate = ? where loanId = ? and ? >= CURDATE()",
				new Object[] { dueDate, loan.getLoanId(), dueDate});
	}
	
	public void returnBookByLoan(Loan loan) throws SQLException{
		template.update("update tbl_book_loans set dateIn = ? where loanId = ? and dateIn is null",
				new Object[] { new java.sql.Date(loan.getDateIn().getTime()), loan.getLoanId() });
	}

	public void deleteLoan(Loan loan) throws SQLException {
		template.update("delete from tbl_book_loans where loanId = ?", 
				new Object[] { loan.getLoanId() });
	}

	public List<Loan> readAllLoans(String q, Boolean seeAll) throws SQLException {
		if(q==null||q.trim().length()==0){
			if(seeAll){
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo)", this);
			}else{
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where dateIn is null", this);
			}
		}else{
			q = "%"+q+"%";
			if(seeAll){
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where branchName like ? or name like ? or title like ? ", 
						new Object[] { q, q, q}, this);
			}else{
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where (branchName like ? or name like ? or title like ?) and dateIn is null", 
						new Object[] { q, q, q}, this);
			}
		}
	}
	
	public List<Loan> readAllLoansWithPageNo(Integer pageNo, Integer pageSize, String q, Boolean seeAll) throws SQLException {
		if(q==null||q.trim().length()==0){
			if(seeAll){
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) limit ? , ?", 
						new Object[] { (pageNo-1)*pageSize, pageSize }, this);
			}else{
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where dateIn is null limit ? , ?", 
						new Object[] { (pageNo-1)*pageSize, pageSize }, this);
			}
		}else{
			q = "%"+q+"%";
			if(seeAll){
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where branchName like ? or name like ? or title like ? limit ? , ?", 
						new Object[] { q, q, q, (pageNo-1)*pageSize, pageSize }, this);
			}else{
				return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where (branchName like ? or name like ? or title like ?) and dateIn is null limit ? , ?", 
						new Object[] { q, q, q, (pageNo-1)*pageSize, pageSize }, this);
			}
		}
	}
	
	public Integer getLoansCount(String q, Boolean seeAll) throws SQLException{
		if(q==null||q.trim().length()==0){
			if(seeAll){
				return template.queryForObject("select count(*) AS COUNT from tbl_book_loans", Integer.class);
			}else{
				return template.queryForObject("select count(*) AS COUNT from tbl_book_loans where dateIn is null", Integer.class);
			}
		}else{
			q = "%"+q+"%";
			if(seeAll){
				return template.queryForObject("select count(loanId) AS COUNT from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where branchName like ? or name like ? or title like ?", new Object[] { q, q, q }, Integer.class);
			}else{
				return template.queryForObject("select count(loanId) AS COUNT from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where (branchName like ? or name like ? or title like ?) and dateIn is null", new Object[] { q, q, q }, Integer.class);
			}
		}
	}
	
	public Loan readLoanById(Loan loan) throws SQLException{
		return template.queryForObject("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where loanId = ?", 
				new Object[]{loan.getLoanId()}, this);
	}
	
	public List<Loan> readAllLoansByBorrower(Borrower borrower, Boolean seeAll) throws SQLException{
		if(seeAll){
			return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where cardNo = ?", 
					new Object[] { borrower.getCardNo()}, this);
		}else{
			return template.query("select * from tbl_book_loans Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) Left Join tbl_borrower using(cardNo) where cardNo = ? and dateIn is null", 
					new Object[] { borrower.getCardNo()}, this);
		}
	}

	@Override
	public Loan mapRow(ResultSet rs, int row) throws SQLException {
		Loan l = new Loan();
		l.setLoanId(rs.getInt("loanId"));
		Book book = new Book();
		book.setBookId(rs.getInt("bookId"));
		book.setTitle(rs.getString("title"));
		l.setBook(book);
		Branch branch = new Branch();
		branch.setBranchId(rs.getInt("branchId"));
		branch.setBranchName(rs.getString("branchName"));
		l.setBranch(branch);
		Borrower borrower = new Borrower();
		borrower.setCardNo(rs.getInt("cardNo"));
		borrower.setName(rs.getString("name"));
		l.setBorrower(borrower);
		l.setDateOut(rs.getDate("dateOut"));
		l.setDateIn(rs.getDate("dateIn"));
		l.setDueDate(rs.getDate("dueDate"));
		return l;
	}

}
