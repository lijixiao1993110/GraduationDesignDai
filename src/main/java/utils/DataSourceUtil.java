package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

public class DataSourceUtil {
	private static String driverClass ;
	private static String jdbcUrl ;
	private static String user ;
	private static String password ;
	private static int maxsize;
	private static int currentsize = 0;
	private static LinkedList<Connection> connectionList = new LinkedList<>();
	static{
		try {
			ClassLoader classLoader = DataSourceUtil.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream("db.properties");
			Properties props = new Properties();
			props.load(is);
			driverClass = props.getProperty("driverClass");
			jdbcUrl = props.getProperty("jdbcUrl");
			user = props.getProperty("user");
			password = props.getProperty("password");
			maxsize = Integer.parseInt(props.getProperty("maxsize"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(driverClass);
			try {
				 conn = DriverManager.getConnection(jdbcUrl, user, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("连接错误！");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("加载驱动错误！");
		}
		return conn;
	}
	
    public static Connection getConnFromPool()
    {
        if(connectionList.size()>0){
            Connection connection = connectionList.getFirst();
            connectionList.removeFirst();
            currentsize++;
            return connection;
        
        }else if(connectionList.size()==0&&currentsize<maxsize){
            connectionList.addLast(getConnection());
            
            Connection connection = connectionList.getFirst();
            connectionList.removeFirst();
            currentsize++;
            return connection;        
        }
        
        throw new RuntimeException("连接池已超过最大连接数!");
    }
    
    public static void releaseConnection(Connection connection)
    {
    	
    	try {
    		if (connection != null){		
    			connectionList.addLast(connection);
    			connection.close();
    			currentsize--;
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
}
