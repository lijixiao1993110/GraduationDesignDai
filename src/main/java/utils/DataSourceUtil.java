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
			// 1.通过当前类获取类加载器
			ClassLoader classLoader = DataSourceUtil.class.getClassLoader();
			// 2.通过类加载器的方法获得一个输入流
			InputStream is = classLoader.getResourceAsStream("db.properties");
			// 3.创建一个properties对象
			Properties props = new Properties();
			// 4.加载输入流
			props.load(is);
			// 5.获取相关参数的值
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
				System.out.println("连接数据库失败！");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("加载驱动失败！");
		}
		return conn;
	}
	
	//获取连接池中的一个连接对象
    public static Connection getConnFromPool()
    {
        //当连接池还没空
        if(connectionList.size()>0){
            Connection connection = connectionList.getFirst();
            connectionList.removeFirst();
            currentsize++;
            return connection;
        
        }else if(connectionList.size()==0&&currentsize<maxsize){
            //连接池被拿空，且连接数没有达到上限，创建新的连接
            connectionList.addLast(getConnection());
            
            Connection connection = connectionList.getFirst();
            connectionList.removeFirst();
            currentsize++;
            return connection;        
        }
        
        throw new RuntimeException("连接数达到上限，请等待!");
    }
    
    //把用完的连接放回连接池
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
