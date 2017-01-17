package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Book;

public class BookCopyDAO extends BaseDAO implements RowMapper<BookCopy>{

	public void addBookCopy(BookCopy bookCopy) throws SQLException {
		checkBookCopy(bookCopy);
		template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", 
				new Object[] { bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId(), bookCopy.getNoOfCopies()});
	}
	
	public void updateBookCopy(BookCopy bookCopy) throws SQLException {
		checkBookCopy(bookCopy);
		template.update("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[] { bookCopy.getNoOfCopies(), bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}
	
	public boolean deductOneBookCopy(BookCopy bookCopy) throws SQLException {
		checkBookCopy(bookCopy);
		Integer noOfCopies = template.queryForObject("select noOfCopies from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[] { bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId() }, Integer.class);
		if(noOfCopies == null || noOfCopies < 1){
			return false;
		}else {
			template.update("update tbl_book_copies set noOfCopies = noOfCopies-1 where bookId = ? and branchId = ?",
				new Object[] { bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
			return true;
		}
	}
	
	public void addOneBookCopy(BookCopy bookCopy) {
		checkBookCopy(bookCopy);
		template.update("update tbl_book_copies set noOfCopies = noOfCopies+1 where bookId = ? and branchId = ?",
				new Object[] { bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}

	public void deleteBookCopy(BookCopy bookCopy) throws SQLException {
		checkBookCopy(bookCopy);
		template.update("delete from tbl_book_copies where bookId = ? and branchId = ?", 
				new Object[] { bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId() });
	}
	
	public BookCopy readBookCopyByIds(BookCopy bookCopy) throws SQLException{
		checkBookCopy(bookCopy);
		return template.queryForObject("select * from tbl_book_copies Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) where bookId = ? and branchId = ?", 
				new Object[]{bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()}, this);
	}

	public List<BookCopy> readAllBookCopiesByBook(Book book) throws SQLException{
		return template.query("select * from tbl_book_copies Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) where bookId = ? and noOfCopies > 0",
				new Object[] { book.getBookId()}, this);
	}
	
	public List<BookCopy> readAllBookCopiesByBranch(Branch branch) throws SQLException{
		return template.query("select * from tbl_book_copies Left Join tbl_book using(bookId) Left Join tbl_library_branch using(branchId) where branchId = ? and noOfCopies > 0",
				new Object[] { branch.getBranchId()}, this);
	}

	public void checkBookCopy(BookCopy bookCopy) {
		if(bookCopy.getBook() == null) bookCopy.setBook(new Book());
		if(bookCopy.getBranch() == null) bookCopy.setBranch(new Branch());
		if(bookCopy.getNoOfCopies() == null || bookCopy.getNoOfCopies() < 0) bookCopy.setNoOfCopies(0);
	}
	
	@Override
	public BookCopy mapRow(ResultSet rs, int row) throws SQLException {
		BookCopy b = new BookCopy();
		Book book = new Book();
		book.setBookId(rs.getInt("bookId"));
		book.setTitle(rs.getString("title"));
		b.setBook(book);
		Branch branch = new Branch();
		branch.setBranchId(rs.getInt("branchId"));
		branch.setBranchName(rs.getString("branchName"));
		b.setBranch(branch);
		b.setNoOfCopies(rs.getInt("noOfCopies"));
		return b;
	}

}
