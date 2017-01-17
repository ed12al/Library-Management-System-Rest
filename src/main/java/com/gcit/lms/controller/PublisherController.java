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

import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Publisher;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class PublisherController {
	
	private static final Logger logger = LoggerFactory.getLogger(PublisherController.class);
	
	@Autowired
	PublisherDAO pdao;
	
	@Autowired
	BookDAO bdao;
	
	@RequestMapping(value = "/admin/publishers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Publisher> publishers(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("admin GET publishers: q="+q);
		return pdao.readAllPublishers(q);
	}
	
	@RequestMapping(value = "/admin/publisher/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Publisher viewPublisher(@PathVariable("id") Integer publisherId) throws SQLException{
		logger.info("admin GET publisher: publisherId="+publisherId);
		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherId);
		publisher = pdao.readPublisherById(publisher);
		return publisher;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/publisher/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Publisher> editPublisher(@RequestBody Publisher publisher, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin PUT publisher: publisher="+publisher+" q="+q);
		pdao.updatePublisher(publisher);
		return pdao.readAllPublishers(q);
	}
	
	@Transactional
	@RequestMapping(value = "/admin/publisher/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Publisher> deletePublisher(@PathVariable("id") Integer publisherId, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin DELETE publisher: publisherId="+publisherId+" q="+q);
		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherId);
		pdao.deletePublisher(publisher);
		return pdao.readAllPublishers(q);
	}
	
	@RequestMapping(value = "/admin/addpublisher", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Publisher viewAddPublisher() throws SQLException{
		logger.info("admin GET addpublisher");
		Publisher publisher = new Publisher();
		return publisher;
	}
	
	@Transactional
	@RequestMapping(value = "/admin/addpublisher", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Publisher> addPublisher(@RequestBody Publisher publisher, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin POST addpublisher: publisher="+publisher+" q="+q);
		pdao.addPublisher(publisher);
		return pdao.readAllPublishers(q);
	}
}
