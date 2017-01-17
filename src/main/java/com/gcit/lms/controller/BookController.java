package com.gcit.lms.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LoanDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Loan;
import com.gcit.lms.entity.Publisher;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class BookController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	PublisherDAO pdao;
	
	@Autowired
	AuthorDAO adao;
	
	@Autowired
	GenreDAO gdao;
	
	@Autowired
	BookCopyDAO bcdao;
	
	@Autowired
	LoanDAO ldao;
	
	@RequestMapping(value = "/admin/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> books(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("admin GET books: q="+q);
		return bdao.readAllBooks(q);
	}
	
	@RequestMapping(value = "/admin/book/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Book viewBook(@PathVariable("id") Integer bookId) throws SQLException{
		logger.info("admin GET book: bookId="+bookId);
		Book book = new Book();
		book.setBookId(bookId);
		book = bdao.readBookById(book);
		book.setPublisher(pdao.readPublisherByBook(book));
		book.setAuthors(adao.readAllAuthorsByBook(book));
		book.setGenres(gdao.readAllGenresByBook(book));
		return book;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/book/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> editBook(@RequestBody Book book, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin PUT book: book="+book+" q="+q);
		bdao.updateBookWithDetails(book);
		return bdao.readAllBooks(q);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/book/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> deleteBook(@PathVariable("id") Integer bookId, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin DELETE book: bookId="+bookId+" q="+q);
		Book book = new Book();
		book.setBookId(bookId);
		bdao.deleteBook(book);
		return bdao.readAllBooks(q);
	}
	
	@RequestMapping(value = "/admin/editbook/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> viewEditBook(@PathVariable("id") Integer bookId) throws SQLException{
		logger.info("admin GET editbook: bookId="+bookId);
		Map<String, Object> response = new HashMap<>();
		Book book = new Book();
		book.setBookId(bookId);
		book = bdao.readBookById(book);
		book.setPublisher(pdao.readPublisherByBook(book));
		book.setAuthors(adao.readAllAuthorsByBook(book));
		book.setGenres(gdao.readAllGenresByBook(book));
		response.put("book", book);
		response.put("newPublisher", new Publisher());
		response.put("newAuthor", new Author());
		response.put("newGenre", new Genre());
		response.put("allPublishers", pdao.readAllPublishers(null));
		response.put("allAuthors", adao.readAllAuthors(null));
		response.put("allGenres", gdao.readAllGenres(null));
		return response;
	}
	
	@RequestMapping(value = "/admin/addbook", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> viewAddBook() throws SQLException{
		logger.info("admin GET addbook:");
		Map<String, Object> response = new HashMap<>();
		response.put("book", new Book());
		response.put("newPublisher", new Publisher());
		response.put("newAuthor", new Author());
		response.put("newGenre", new Genre());
		response.put("allPublishers", pdao.readAllPublishers(null));
		response.put("allAuthors", adao.readAllAuthors(null));
		response.put("allGenres", gdao.readAllGenres(null));
		return response;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/addbook", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> addBook(@RequestBody Book book, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin POST addbook: book="+book+" q="+q);
		bdao.addBookWithDetails(book);
		return bdao.readAllBooks(q);
	}
	
	@RequestMapping(value = "/borrower/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> booksBor(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("borrower GET books: q="+q);
		return bdao.readAllBooks(q);
	}
	
	@RequestMapping(value = "/borrower/book/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Book viewBookBor(@PathVariable("id") Integer bookId) throws SQLException{
		logger.info("borrower GET book: bookId="+bookId);
		Book book = new Book();
		book.setBookId(bookId);
		book = bdao.readBookById(book);
		book.setPublisher(pdao.readPublisherByBook(book));
		book.setAuthors(adao.readAllAuthorsByBook(book));
		book.setGenres(gdao.readAllGenresByBook(book));
		return book;
	}
	
	@Transactional
	@RequestMapping(value = "/borrower/borrow/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> borrowBook(@RequestBody BookCopy bookCopy, @PathVariable("id") Integer cardNo, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("borrower POST borrow: bookCopy="+bookCopy);
		if(bcdao.deductOneBookCopy(bookCopy)) {
			Loan loan = new Loan();
			loan.setBook(bookCopy.getBook());
			Borrower borrower = new Borrower();
			borrower.setCardNo(cardNo);
			loan.setBorrower(borrower);
			loan.setBranch(bookCopy.getBranch());
			ldao.addLoan(loan);
		}
		return bdao.readAllBooks(q);
	}
	
	@Transactional
	@RequestMapping(value = "/borrower/return", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> returnBook(@RequestBody Loan loan, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("borrower PUT return: loan="+loan);
		BookCopy bookCopy = new BookCopy();
		bookCopy.setBook(loan.getBook());
		bookCopy.setBranch(loan.getBranch());
		bcdao.addOneBookCopy(bookCopy);
		loan.setDateIn(new Date());
		ldao.returnBookByLoan(loan);
		return bdao.readAllBooks(q);
	}
}
