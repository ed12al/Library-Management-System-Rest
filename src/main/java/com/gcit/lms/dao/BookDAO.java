package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

public class BookDAO extends BaseDAO implements RowMapper<Book>{

	public void addBook(Book book) throws SQLException {
		checkBook(book);
		template.update("insert into tbl_book (title, pubId) values (?, ?)", new Object[] { book.getTitle(), book.getPublisher().getPublisherId() });
	}

	public Integer addBookWithID(Book book) throws SQLException{
		String sql = "insert into tbl_book (title, pubId) values (?, ?)";
		checkBook(book);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "bookId" });
				ps.setObject(1, book.getTitle());
				ps.setObject(2, book.getPublisher().getPublisherId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public void addBookWithDetails(Book book) throws SQLException {
		Integer bookId = addBookWithID(book);
		book.setBookId(bookId);
		if (bookId != null && bookId > 0) {
			addAllAuthorsByBook(book);
			addAllGenresByBook(book);
		}
	}

	public void updateBook(Book book) throws SQLException {
		checkBook(book);
		template.update("update tbl_book set title = ?, pubId = ? where bookId = ?",
				new Object[] { book.getTitle(), book.getPublisher().getPublisherId(), book.getBookId() });
	}

	public void updateBookWithDetails(Book book) throws SQLException {
		updateBook(book);
		deleteAllAuthorsByBook(book);
		addAllAuthorsByBook(book);
		deleteAllGenresByBook(book);
		addAllGenresByBook(book);
	}

	private void addAllAuthorsByBook(Book book) throws SQLException {
		if (book.getAuthors() != null) {
			for (Author author : book.getAuthors()) {
				if(author != null && author.getAuthorId() != null)
					template.update("insert into tbl_book_authors (authorId, bookId) values (?, ?)",
							new Object[] { author.getAuthorId(), book.getBookId() });
			}
		}
	}

	private void addAllGenresByBook(Book book) throws SQLException {
		if (book.getGenres() != null) {
			for (Genre genre : book.getGenres()) {
				if(genre != null && genre.getGenreId() != null)
					template.update("insert into tbl_book_genres (genreId, bookId) values (?, ?)",
							new Object[] { genre.getGenreId(), book.getBookId() });
			}
		}
	}

	private void deleteAllAuthorsByBook(Book book) throws SQLException {
		template.update("delete from tbl_book_authors where bookId = ? ", new Object[] { book.getBookId() });
	}

	private void deleteAllGenresByBook(Book book) throws SQLException {
		template.update("delete from tbl_book_genres where bookId = ? ", new Object[] { book.getBookId() });
	}

	public void deleteBook(Book book) throws SQLException {
		template.update("delete from tbl_book where bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAllBooks(String q) throws SQLException {
		if (q == null || q.trim().length() == 0) {
			return template.query("select * from tbl_book", this);
		} else {
			q = "%" + q + "%";
			return template.query("select * from tbl_book where title like ?", new Object[]{q}, this);
		}
	}

	public List<Book> readAllBooksWithPageNo(Integer pageNo, Integer pageSize, String q) throws SQLException {
		if (q == null || q.trim().length() == 0) {
			return template.query("select * from tbl_book limit ? , ?", new Object[] { (pageNo-1)*pageSize, pageSize }, this);
		} else {
			q = "%" + q + "%";
			return template.query("select * from tbl_book where title like ? limit ? , ?", new Object[]{q, (pageNo-1)*pageSize, pageSize }, this);
		}
	}

	public Integer getBooksCount(String q) throws SQLException {
		if (q == null || q.trim().length() == 0) {
			return template.queryForObject("select count(*) AS COUNT from tbl_book", Integer.class);
		} else {
			q = "%" + q + "%";
			return template.queryForObject("select count(*) AS COUNT from tbl_book where title like ?", new Object[] { q }, Integer.class);
		}
	}

	public Book readBookById(Book book) throws SQLException {
		return template.queryForObject("select * from tbl_book where bookId = ?", new Object[] { book.getBookId() }, this);
	}

	public List<Book> readAllBooksByAuthor(Author author) throws SQLException {
		return template.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_authors where authorId = ?)",
				new Object[] { author.getAuthorId() }, this);
	}

	public List<Book> readAllBooksByGenre(Genre genre) throws SQLException {
		return template.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_genres where genreId = ?)",
				new Object[] { genre.getGenreId() }, this);
	}

	public List<Book> readAllBooksByPublisher(Publisher publisher) throws SQLException {
		return template.query("select * from tbl_book where pubId = ?",
				new Object[] { publisher.getPublisherId() }, this);
	}
	
	public List<Book> readAllBooksWithNoCopyByBranch(Branch branch) throws SQLException {
		return template.query("select * from tbl_book where bookId not in (Select bookId from tbl_book_copies where branchId = ? and noOfCopies > 0)", 
				new Object[] { branch.getBranchId() }, this);
	}
	
	private void checkBook(Book book){
		if(book.getTitle() != null && book.getTitle().trim().length() == 0)	book.setTitle(null);
		if(book.getPublisher() == null) book.setPublisher(new Publisher());
	}
	
	@Override
	public Book mapRow(ResultSet rs, int row) throws SQLException {
		Book b = new Book();
		b.setBookId(rs.getInt("bookId"));
		b.setTitle(rs.getString("title"));
		Integer pubId = rs.getInt("pubId");
		if (pubId != 0) {
			Publisher publisher = new Publisher();
			publisher.setPublisherId(pubId);
			b.setPublisher(publisher);
		}
		return b;
	}

}
