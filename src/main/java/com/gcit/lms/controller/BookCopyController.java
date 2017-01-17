package com.gcit.lms.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class BookCopyController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookCopyController.class);
	
	@Autowired
	BookCopyDAO bcdao;
	
	@RequestMapping(value = "/borrower/bookcopies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BookCopy> bookCopies(@PathVariable("id") Integer bookId) throws SQLException{
		logger.info("borrower GET bookCopies: bookId="+bookId);
		Book book = new Book();
		book.setBookId(bookId);
		return bcdao.readAllBookCopiesByBook(book);
	}
}
