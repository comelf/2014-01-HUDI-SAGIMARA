package database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logger.SagimaraLogger;

import org.apache.log4j.Logger;

import dto.Video;

public class VideoDAO {
	private Connection conn;
	private DatabaseConnector connector;
	private Logger logger = SagimaraLogger.logger;
	
	public VideoDAO() {
		this.connector = new DatabaseConnector();
	}
	
	
	
	public boolean add(Video video) throws SQLException {
		conn = connector.getMysqlConnection();
		String tableName = video.getTableName();
		String sql = "INSERT INTO " + tableName
				+ "(USER_user_phone, video_link, video_date)"
				+ " VALUES (?, ?, ?)";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, video.getVideoId());
		pstmt.setString(2, video.getVideoLink());
		pstmt.setString(3, video.getVideoDate());

		int result = pstmt.executeUpdate();

		if (result == 1) {
			logger.info(String.format("Add Complete %s : %s, %s, %s",
					tableName, video.getVideoId(),
					video.getVideoLink(),
					video.getVideoDate()));
		} else {
			logger.info("Add Fail " + tableName);
			pstmt.close();
			conn.close();
			return false;
		}

		pstmt.close();
		conn.close();
		return true;
	}

	public boolean update(Video video) throws SQLException {
		conn = connector.getMysqlConnection();
		String tableName = video.getTableName();
		String sql = "UPDATE " + tableName
				+ " SET video_link=?, video_date=?"
				+ " WHERE USER_user_phone = ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, video.getVideoLink());
		pstmt.setString(2, video.getVideoDate());
		pstmt.setString(3, video.getVideoId());
		
		int result = pstmt.executeUpdate();

		if (result == 1) {
			logger.info(String.format("Update Complete %s : %s, %s, %s",
					tableName, video.getVideoId(),
					video.getVideoLink(),
					video.getVideoDate()));
		} else {
			logger.info("Add Fail " + tableName);
			pstmt.close();
			conn.close();
			return false;
		}

		pstmt.close();
		conn.close();
		return true;
	}
	
	public Video selectById(String phoneNum) throws SQLException {
		conn = connector.getMysqlConnection();
		
		String sql = "select * from VIDEO where USER_user_phone = ? order by video_date desc";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, phoneNum);
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			Video video = new Video(rs.getString("USER_user_phone"),
									rs.getString("video_link"),
									rs.getString("video_date"));
			pstmt.close();
			rs.close();
			conn.close();
			return video;
		}

		pstmt.close();
		rs.close();
		conn.close();
		return null;
	}
}
