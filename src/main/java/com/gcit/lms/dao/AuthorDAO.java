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

public class AuthorDAO extends BaseDAO implements RowMapper<Author>{
	
	public void addAuthor(Author author) throws SQLException {
		checkAuthor(author);
		template.update("insert into tbl_author (authorName) values (?)", new Object[] { author.getAuthorName() });
	}
	
	public Integer addAuthorWithID(Author author) throws SQLException{
		String sql = "insert into tbl_author (authorName) values (?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "authorId" });
				ps.setObject(1, author.getAuthorName());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void updateAuthor(Author author) throws SQLException {
		checkAuthor(author);
		template.update("update tbl_author set authorName = ? where authorId = ?", new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	public void deleteAuthor(Author author) throws SQLException {
		template.update("delete from tbl_author where authorId = ?", new Object[] { author.getAuthorId() });
	}

	public List<Author> readAllAuthors(String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_author", this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_author where authorName like ? ", new Object[]{q}, this);
		}
	}
	
	public List<Author> readAllAuthorsWithPageNo(Integer pageNo, Integer pageSize, String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_author limit ? , ?", new Object[] { (pageNo-1)*pageSize, pageSize }, this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_author where authorName like ? limit ? , ?", new Object[]{q, (pageNo-1)*pageSize, pageSize }, this);
		}
	}
	
	public Integer getAuthorsCount(String q) throws SQLException{
		if(q==null||q.trim().length()==0){
			return template.queryForObject("select count(*) AS COUNT from tbl_author", Integer.class);
		}else{
			q = "%"+q+"%";
			return template.queryForObject("select count(*) AS COUNT from tbl_author where authorName like ?", new Object[]{ q }, Integer.class);
		}
	}
	
	public Author readAuthorById(Author author) throws SQLException{
		return template.queryForObject("select * from tbl_author where authorId = ?", new Object[]{ author.getAuthorId() }, this);
	}
	
	public List<Author> readAllAuthorsByBook(Book book) throws SQLException{
		return template.query("select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId = ?)",
				new Object[] { book.getBookId()}, this);
	}

	private void checkAuthor(Author author){
		if(author.getAuthorName() == null || author.getAuthorName().trim().length() == 0)	author.setAuthorName(null);
	}
	
	@Override
	public Author mapRow(ResultSet rs, int row) throws SQLException {
		Author a = new Author();
		a.setAuthorId(rs.getInt("authorId"));
		a.setAuthorName(rs.getString("authorName"));
		return a;
	}
}
