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

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO implements RowMapper<Publisher>{

	public void addPublisher(Publisher publisher) throws SQLException {
		checkPublisher(publisher);
		template.update("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?, ?, ?)", 
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone()});
	}
	
	public Integer addPublisherWithID(Publisher publisher) throws SQLException{
		String sql = "insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "publisherId" });
				ps.setObject(1, publisher.getPublisherName());
				ps.setObject(2, publisher.getPublisherAddress());
				ps.setObject(3, publisher.getPublisherPhone());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void updatePublisher(Publisher publisher) throws SQLException {
		checkPublisher(publisher);
		template.update("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone(), publisher.getPublisherId() });
	}

	public void deletePublisher(Publisher publisher) throws SQLException {
		template.update("delete from tbl_publisher where publisherId = ?", 
				new Object[] { publisher.getPublisherId() });
	}

	public List<Publisher> readAllPublishers(String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_publisher", this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_publisher where publisherName like ? ", new Object[]{q}, this);
		}
	}
	
	public List<Publisher> readAllPublishersWithPageNo(Integer pageNo, Integer pageSize, String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.query("select * from tbl_publisher limit ? , ?", new Object[] { (pageNo-1)*pageSize, pageSize }, this);
		}else{
			q = "%"+q+"%";
			return template.query("select * from tbl_publisher where publisherName like ? limit ? , ?", new Object[]{q, (pageNo-1)*pageSize, pageSize }, this);
		}
	}
	
	public Integer getPublishersCount(String q) throws SQLException {
		if(q==null||q.trim().length()==0){
			return template.queryForObject("select count(*) AS COUNT from tbl_publisher", Integer.class);
		}else{
			q = "%"+q+"%";
			return template.queryForObject("select count(*) AS COUNT from tbl_publisher where publisherName like ?", new Object[]{q}, Integer.class);
		}
	}
	
	public Publisher readPublisherById(Publisher publisher) throws SQLException {
		if(publisher.getPublisherId() == null) return null;
		return template.queryForObject("select * from tbl_publisher where publisherId = ?", new Object[]{ publisher.getPublisherId() }, this);
	}
	
	public Publisher readPublisherByBook(Book book) throws SQLException {
		if(book.getPublisher() == null) return null;
		return readPublisherById(book.getPublisher());
	}
	
	private void checkPublisher(Publisher publisher){
		if(publisher.getPublisherName() != null && publisher.getPublisherName().trim().length() == 0)	publisher.setPublisherName(null);
		if(publisher.getPublisherAddress() != null && publisher.getPublisherAddress().trim().length() == 0)	publisher.setPublisherAddress(null);
		if(publisher.getPublisherPhone() != null && publisher.getPublisherPhone().trim().length() == 0)	publisher.setPublisherPhone(null);
	}

	@Override
	public Publisher mapRow(ResultSet rs, int row) throws SQLException {
		Publisher p = new Publisher();
		p.setPublisherId(rs.getInt("publisherId"));
		p.setPublisherAddress(rs.getString("publisherAddress"));
		p.setPublisherName(rs.getString("publisherName"));
		p.setPublisherPhone(rs.getString("publisherPhone"));
		return p;
	}
}
