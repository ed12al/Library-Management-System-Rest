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

import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.LoanDAO;
import com.gcit.lms.entity.Borrower;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class BorrowerController {
	
	private static final Logger logger = LoggerFactory.getLogger(BorrowerController.class);
	
	@Autowired
	BorrowerDAO brdao;
	
	@Autowired
	LoanDAO ldao;
	
	@RequestMapping(value = "/admin/borrowers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Borrower> borrowers(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("admin GET borrowers: q="+q);
		return brdao.readAllBorrowers(q);
	}
	
	@RequestMapping(value = "/admin/borrower/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Borrower viewBorrower(@PathVariable("id") Integer cardNo) throws SQLException{
		logger.info("admin GET borrower: cardNo="+cardNo);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		borrower = brdao.readBorrowerById(borrower);
		borrower.setLoans(ldao.readAllLoansByBorrower(borrower, true));
		return borrower;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/borrower/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Borrower> editBorrower(@RequestBody Borrower borrower, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin PUT borrower: borrower="+borrower+" q="+q);
		brdao.updateBorrower(borrower);
		return brdao.readAllBorrowers(q);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/borrower/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Borrower> deleteBorrower(@PathVariable("id") Integer cardNo, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin DELETE borrower: cardNo="+cardNo+" q="+q);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		brdao.deleteBorrower(borrower);
		return brdao.readAllBorrowers(q);
	}
	
	@RequestMapping(value = "/admin/addborrower", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Borrower viewAddBorrower() throws SQLException{
		logger.info("admin GET addborrower");
		Borrower borrower = new Borrower();
		return borrower;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/addborrower", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Borrower> addBorrower(@RequestBody Borrower borrower, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin POST addborrower: borrower="+borrower+" q="+q);
		brdao.addBorrower(borrower);
		return brdao.readAllBorrowers(q);
	}
	
	@RequestMapping(value = "/login/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Borrower login(@PathVariable("id") Integer cardNo) throws SQLException {
		logger.info("borrower POST login: cardNo="+cardNo);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		borrower = brdao.readBorrowerById(borrower);
		return borrower;
	}
	
	@RequestMapping(value = "/borrower/cardNo/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Borrower viewBorrowerBor(@PathVariable("id") Integer cardNo) throws SQLException{
		logger.info("borrower GET borrower: cardNo="+cardNo);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		borrower = brdao.readBorrowerById(borrower);
		borrower.setLoans(ldao.readAllLoansByBorrower(borrower, false));
		return borrower;
	}
}
