package service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import service.RegistService;
import utils.DataSourceUtil;

public class RegistServiceImpl implements RegistService {

	@Override
	public boolean findUserName(Map<String, Object> conditions) {
		// TODO Auto-generated method stub
		boolean isExisted = false;
		try {
			String userName =conditions.get("userName") == null?"":conditions.get("userName").toString();
			Connection conn = DataSourceUtil.getConnFromPool();
			String sql = "select * from user where userName = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				isExisted = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isExisted;
	}

	@Override
	public boolean regist(Map<String, Object> conditions) {
		// TODO Auto-generated method stub
		boolean isRegisted = false;
		String userName = conditions.get("userName") == null?"":conditions.get("userName").toString();
		String passWord = conditions.get("passWord") == null?"":conditions.get("passWord").toString();
		String sex = conditions.get("sex") == null?"":conditions.get("sex").toString();
		String email = conditions.get("email") == null?"":conditions.get("email").toString();
		String tel = conditions.get("tel") == null?"":conditions.get("tel").toString();
		String birth = conditions.get("birth") == null?"":conditions.get("birth").toString();
		String isAdimin = conditions.get("isAdimin") == null?"":conditions.get("isAdimin").toString();
		Connection conn = DataSourceUtil.getConnFromPool();
		String sql = "insert into user (userName,passWord,sex,email,tel,birth,isAdimin) value (?,?,?,?,?,?,?) ";
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			pst.setString(2, passWord);
			pst.setString(3, sex);
			pst.setString(4, email);
			pst.setString(5, tel);
			pst.setString(6, birth);
			pst.setString(7, isAdimin);
			int executeNum = pst.executeUpdate(sql);
			if(executeNum>0){
				isRegisted = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isRegisted;
	}

}
