package com.gcit.lms.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.entity.Genre;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class GenreController {
	
	private static final Logger logger = LoggerFactory.getLogger(GenreController.class);
	
	@Autowired
	GenreDAO gdao;
	
	@RequestMapping(value = "/admin/genres", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Genre> genres(@RequestParam(value="searchString", required=false) String q) throws SQLException {
		logger.info("admin GET genres: q="+q);
		return gdao.readAllGenres(q);
	}

	@Transactional
	@RequestMapping(value = "/admin/addgenre", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Genre> addGenre(@RequestBody Genre genre, @RequestParam(value="searchString", required=false) String q) throws SQLException{
		logger.info("admin POST addgenre: genre="+genre+" q="+q);
		gdao.addGenre(genre);
		return gdao.readAllGenres(q);
	}
}
