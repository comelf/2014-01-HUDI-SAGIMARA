package framework;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import type.UserProfile;
import database.DatabaseByMysql;
import database.DatabaseController;

public class FrontController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HashMap<String, String> map = new HashMap<String, String>();
	DatabaseController dbc;
	JsonBuilder jb;

	@Override
	public void init() throws ServletException {
		super.init();
		map.put("/test", "/test.jsp");
		map.put("/index", "/index.jsp");
		
		dbc = new DatabaseByMysql();
		jb = new JsonBuilder();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	

		String path = request.getServletPath();
		String result = map.get(path);
		response.setCharacterEncoding("utf8");
		request.setCharacterEncoding("utf8");
		
		if (result != null) {
			RequestDispatcher dispather = getServletContext().getRequestDispatcher(result);
			dispather.forward(request, response);
		} 
		else {
			request.setAttribute("error", "존재하지 않는 주소입니다");
			RequestDispatcher dispather = getServletContext().getRequestDispatcher("/error.jsp");
			dispather.forward(request, response);
		}

		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String path = request.getServletPath();
		
		if(path.equals("/test")) {
			test(request, response);
		}
			
		//super.doPost(request, response);
	}
	
	private void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();
		String result = map.get(path);
		String id = (String) request.getParameter("id");
		
		UserProfile dut = dbc.readtable("USER_PROFILE", id);
		String json = jb.javaToJson(dut);
		System.out.println(json);
		response.setCharacterEncoding("utf8");
		request.setCharacterEncoding("utf8");
		
		request.setAttribute("json", json);

		if (result != null) {
			RequestDispatcher dispather = getServletContext().getRequestDispatcher(result);
			dispather.forward(request, response);
		} else {
			System.out.println("path error");
			System.out.println(path);
			System.out.println(result);
		}
	}
	
	
	
}
