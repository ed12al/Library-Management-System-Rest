package com.gcit.lms.controller;

import java.sql.SQLException;
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

import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Branch;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class BranchController {
	
	private static final Logger logger = LoggerFactory.getLogger(BranchController.class);
	
	@Autowired
	BranchDAO bhdao;
	
	@Autowired
	BookCopyDAO bcdao;
	
	@Autowired
	BookDAO bdao;
	
	@RequestMapping(value = "/admin/branches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> branches(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("admin GET branches: q="+q);
		return bhdao.readAllBranches(q);
	}
	
	@RequestMapping(value = "/admin/branch/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Branch viewBranch(@PathVariable("id") Integer branchId) throws SQLException{
		logger.info("admin GET branch: branchId="+branchId);
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		branch = bhdao.readBranchById(branch);
		branch.setBookCopy(bcdao.readAllBookCopiesByBranch(branch));
		return branch;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/branch/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> editBranch(@RequestBody Branch branch, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin PUT branch: branch="+branch+" q="+q);
		bhdao.updateBranch(branch);
		return bhdao.readAllBranches(q);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/branch/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> deleteBranch(@PathVariable("id") Integer branchId, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin DELETE branch: branchId="+branchId+" q="+q);
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		bhdao.deleteBranch(branch);
		return bhdao.readAllBranches(q);
	}
	
	@RequestMapping(value = "/admin/addbranch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Branch viewAddBranch() throws SQLException{
		logger.info("admin GET addbranch");
		Branch branch = new Branch();
		return branch;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/addbranch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> addBranch(@RequestBody Branch branch, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin POST addbranch: branch="+branch+" q="+q);
		bhdao.addBranch(branch);
		return bhdao.readAllBranches(q);
	}
	
	@RequestMapping(value = "/librarian/branches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> branchesLib(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("librarian GET branches: q="+q);
		return bhdao.readAllBranches(q);
	}
	
	@RequestMapping(value = "/librarian/branch/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Branch viewBranchLib(@PathVariable("id") Integer branchId) throws SQLException{
		logger.info("librarian GET branch: branchId="+branchId);
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		branch = bhdao.readBranchById(branch);
		branch.setBookCopy(bcdao.readAllBookCopiesByBranch(branch));
		return branch;
	}
	
	@RequestMapping(value = "/librarian/editbranch/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getEditBranchLib(@PathVariable("id") Integer branchId) throws SQLException{
		logger.info("librarian GET editbranch: branchId="+branchId);
		Map<String, Object> response = new HashMap<>();
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		branch = bhdao.readBranchById(branch);
		branch.setBookCopy(bcdao.readAllBookCopiesByBranch(branch));
		response.put("branch", branch);
		response.put("allBooks", bdao.readAllBooks(null));
		return response;
	}
	
	@Transactional
	@RequestMapping(value = "/librarian/branch/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> editBranchLib(@RequestBody Branch branch, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("librarian PUT branch: branch="+branch+" q="+q);
		bhdao.updateBranchWithDetails(branch);
		return bhdao.readAllBranches(q);
	}
}
