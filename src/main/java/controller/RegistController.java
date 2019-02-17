package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import model.ResultMessage;
import service.RegistService;
import service.impl.RegistServiceImpl;

/**
 * Servlet implementation class RegistController
 */
public class RegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private PrintWriter out = null ;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ResultMessage resultMessage = new ResultMessage();
		out = response.getWriter();
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, Object> conditions = new HashMap<>();
		for(Map.Entry<String, String[]> parameterMapEntry : parameterMap.entrySet()){
			String key = parameterMapEntry.getKey();
			conditions.put(key,(parameterMapEntry.getValue())[0]);
		}
		RegistService registService = new RegistServiceImpl();
		Map<String,Object> resultMap = new HashMap<>();
		boolean isExisted = registService.findUserName(conditions);
		if(isExisted == false){			
			boolean isRegisted = registService.regist(conditions);
			if(isRegisted!=true){
				resultMessage.setFlag(false);
				resultMessage.setMessage("注册失败！");			
			}else{
				resultMessage.setFlag(true);
				resultMessage.setMessage("注册成功！");		
			}
		}else{
			resultMessage.setFlag(false);
			resultMessage.setMessage("此用户名已经存在，请修改！");		
		}
		String resultString = JSON.toJSONString(resultMessage);
		out.print(resultString);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
