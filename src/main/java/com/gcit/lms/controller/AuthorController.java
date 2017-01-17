package com.gcit.lms.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Author;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class AuthorController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
	
	@Autowired
	AuthorDAO adao;
	
	@Autowired
	BookDAO bdao;
	
	@RequestMapping(value = "/admin/authors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Author> authors(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("admin GET authors: q="+q);
		return adao.readAllAuthors(q);
	}
	
	@RequestMapping(value = "/admin/author/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Author viewAuthor(@PathVariable("id") Integer authorId) throws SQLException{
		logger.info("admin GET author: authorId="+authorId);
		Author author = new Author();
		author.setAuthorId(authorId);
		author = adao.readAuthorById(author);
		author.setBooks(bdao.readAllBooksByAuthor(author));
		return author;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/author/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Author> editAuthor(@RequestBody Author author, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin PUT author: author="+author+" q="+q);
		adao.updateAuthor(author);
		return adao.readAllAuthors(q);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/author/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Author> deleteAuthor(@PathVariable("id") Integer authorId, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin DELETE author: authorId="+authorId+" q="+q);
		Author author = new Author();
		author.setAuthorId(authorId);
		adao.deleteAuthor(author);
		return adao.readAllAuthors(q);
	}
	
	@RequestMapping(value = "/admin/addauthor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Author viewAddAuthor() throws SQLException{
		logger.info("admin GET addauthor");
		Author author = new Author();
		return author;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/addauthor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Author> addAuthor(@RequestBody Author author, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin POST addauthor: author="+author+" q="+q);
		adao.addAuthor(author);
		return adao.readAllAuthors(q);
	}
}
