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
			// 1.ͨ����ǰ���ȡ�������
			ClassLoader classLoader = DataSourceUtil.class.getClassLoader();
			// 2.ͨ����������ķ������һ��������
			InputStream is = classLoader.getResourceAsStream("db.properties");
			// 3.����һ��properties����
			Properties props = new Properties();
			// 4.����������
			props.load(is);
			// 5.��ȡ��ز�����ֵ
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
				System.out.println("�������ݿ�ʧ�ܣ�");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("��������ʧ�ܣ�");
		}
		return conn;
	}
	
	//��ȡ���ӳ��е�һ�����Ӷ���
    public static Connection getConnFromPool()
    {
        //�����ӳػ�û��
        if(connectionList.size()>0){
            Connection connection = connectionList.getFirst();
            connectionList.removeFirst();
            currentsize++;
            return connection;
        
        }else if(connectionList.size()==0&&currentsize<maxsize){
            //���ӳر��ÿգ���������û�дﵽ���ޣ������µ�����
            connectionList.addLast(getConnection());
            
            Connection connection = connectionList.getFirst();
            connectionList.removeFirst();
            currentsize++;
            return connection;        
        }
        
        throw new RuntimeException("�������ﵽ���ޣ���ȴ�!");
    }
    
    //����������ӷŻ����ӳ�
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
