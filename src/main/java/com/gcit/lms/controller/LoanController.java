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

import com.gcit.lms.dao.LoanDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Loan;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class LoanController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
	
	@Autowired
	LoanDAO ldao;
	
	@Autowired
	BookDAO bdao;
	
	@RequestMapping(value = "/admin/loans", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Loan> loans(@RequestParam(value="searchString", required=false) String q, 
			@RequestParam(value="seeAll", required=false, defaultValue="false") boolean seeAll) throws SQLException {
		logger.info("admin GET loans: q="+q+" seeAll="+seeAll);
		return ldao.readAllLoans(q, seeAll);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/loan/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Loan> editLoan(@RequestBody Loan loan, @RequestParam(value="searchString", required=false) String q, 
			@RequestParam(value="seeAll", required=false, defaultValue="false") boolean seeAll) throws SQLException{
		logger.info("admin PUT loan: loan="+loan+" q="+q+" seeAll="+seeAll);
		ldao.updateDueDate(loan);
		return ldao.readAllLoans(q, seeAll);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/loan/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Loan> deleteLoan(@PathVariable("id") Integer loanId, @RequestParam(value="searchString", required=false) String q, 
			@RequestParam(value="seeAll", required=false, defaultValue="false") boolean seeAll) throws SQLException{
		logger.info("admin DELETE loan: loanId="+loanId+" q="+q+" seeAll="+seeAll);
		Loan loan = new Loan();
		loan.setLoanId(loanId);
		ldao.deleteLoan(loan);
		return ldao.readAllLoans(q, seeAll);
	}
}
