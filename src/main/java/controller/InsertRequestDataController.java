package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.SagimaraLogger;

import org.apache.log4j.Logger;

import utility.JsonBuilder;
import database.RequestDAO;
import database.UserDAO;
import dto.Request;
import dto.User;

public class InsertRequestDataController implements Controller {
	private Logger logger;
	private JsonBuilder jb;
	private String forwardPath;
	
	public InsertRequestDataController(String forwardPath) {
		super();
		this.logger = SagimaraLogger.logger;
		this.jb = JsonBuilder.getJsonBuilder();
		this.forwardPath = forwardPath;
	}
	
	public String run(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		logger.info("Content-type : " + request.getHeader("Content-type"));
		
		String from = (String) request.getParameter("from");
		String to = (String) request.getParameter("to");
		String date = (String) request.getParameter("date");
		
		User user = new User();
		UserDAO userDAO = new UserDAO();
		try {
			user = userDAO.selectById(from);
			if(user == null) {
				user = new User(from, "false", "1", "위치정보 없음");
				userDAO.add(user);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Request requestDTO = new Request(from,to,date);
		RequestDAO requestDAO = new RequestDAO();
		
		String json = null;
		
		try {
			if(requestDAO.add(requestDTO)){
				json = jb.requestSuccessJSON();
			}else{
				json = jb.requestFailedJSON();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setAttribute("json", json);
		
		return forwardPath;

	}
}
