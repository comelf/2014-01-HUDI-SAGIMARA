package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.SagimaraLogger;

import org.apache.log4j.Logger;

import utility.JsonBuilder;
import database.DatabaseHandler;

public class InsertRequestDataController implements Controller {
	private Logger logger;
	private DatabaseHandler dbh;
	private JsonBuilder jb;
	private String forwardPath;
	
	public InsertRequestDataController(String forwardPath) {
		super();
		this.logger = SagimaraLogger.logger;
		this.dbh = DatabaseHandler.getDatabaseHandler();
		this.jb = JsonBuilder.getJsonBuilder();
		this.forwardPath = forwardPath;
	}
	
	public String run(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		logger.info("Content-type : " + request.getHeader("Content-type"));
		
		String from = (String) request.getParameter("from");
		String to = (String) request.getParameter("to");
		String date = (String) request.getParameter("date");
		
		String json;
		if(dbh.insertRequest(from,to,date)){
			json = jb.requestSuccessJSON();
		}else{
			json = jb.requestFailedJSON();
		}

		request.setAttribute("json", json);
		
		return forwardPath;

	}
}
