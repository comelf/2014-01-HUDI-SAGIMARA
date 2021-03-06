package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.SagimaraLogger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import utility.JsonBuilder;
import database.AdminDAO;
import dto.Admin;

public class AdminLoginController implements Controller {
	private Logger logger;
	private JsonBuilder jb;
	private String forwardPath;

	public AdminLoginController(String forwardPath) {
		super();
		this.logger = SagimaraLogger.logger;
		this.jb = JsonBuilder.getJsonBuilder();
		this.forwardPath = forwardPath;
	}
	
	@Override
	public String run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id, password;
		String json=null;
		Map<String, String> requestMap;
		
		requestMap = makeParameterMap(request);
		id = requestMap.get("admin_id");
		password =  requestMap.get("admin_pw");
		
		logger.info(id + password);
		
		switch (CheckForadminLogin(id,password)) {
		case 0:
			json = jb.requestSuccessJSON();
			HttpSession session = null;
			session = request.getSession(true);
			session.setAttribute("admin_id", id);
			break;
		case 1:
			json = jb.requstErrorJSON();
			break;
		case 2:
		case -1:
		default:
			json = jb.requestFailedJSON();
			break;
		} 
		

		request.setAttribute("json", json);

		return forwardPath;
	}
	
	public int CheckForadminLogin(String id, String password) {

		Admin admin = new Admin();
		Admin dbAdmin = new Admin();
		AdminDAO adminDAO = new AdminDAO();

		admin.setAdminId(id);
		admin.setAdminPassword(password);

		try {
			dbAdmin = adminDAO.selectById(id);
			
			if (dbAdmin == null) {
				return 2;
			} else if (dbAdmin.getAdminPassword().equals(password)) {
				return 0;
			} else {
				return 1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return -1;
	}

	private Map<String, String> makeParameterMap(HttpServletRequest request) throws IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator<FileItem> ir = items.iterator();
		while (ir.hasNext()) {
			FileItem item = (FileItem) ir.next();

			if (item.isFormField()) {
				// Process form field.
				String name = item.getFieldName();
				String value = item.getString();
				map.put(name, value);
			} 
		} 
		return map;
	}
}
