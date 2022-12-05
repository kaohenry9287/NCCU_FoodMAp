package SQL;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import Utility.ConfigReader;

public class BaseDB {
	private String username;
	private String password;
	private String DBName;
	private String DBURL;
	private Connection connection;
	private Statement statement;
	
	public BaseDB() throws SQLException
	{
		this.initState();
		this.DBURL = String.format("jdbc:mysql://localhost:3306/%s", this.DBName);
		this.connection = DriverManager.getConnection(this.DBURL, this.username, this.password);
		this.statement = connection.createStatement();
	}
	
	public BaseDB(String username, String password, String DBName) throws SQLException
	{
		this.username = username;
		this.password = password;
		this.DBName = DBName;
		this.DBURL = String.format("jdbc:mysql://localhost:3306/%s", this.DBName);
		connection = DriverManager.getConnection(this.DBURL, this.username, this.password);
		this.statement = connection.createStatement();
	}
	
	//Generic function, <T> between modifier and return type
	//Create
	public <T> boolean create(T t, Class<T> clazzType) throws Exception
	{
		Field[] properties = t.getClass().getDeclaredFields();
		String tableName = t.getClass().getName().replace(t.getClass().getPackageName()+".", "");
		String keys = "";
		String values = "";
		
		//check PK
		String PK = this.getPK(tableName);
		boolean isPKAutoFilled = this.isAutoIncreament(t.getClass().getName(), PK);
		
		for(Field prop : properties)
		{
			if(prop.getName().equals(PK) && isPKAutoFilled){
				continue;
			}
			
			keys += prop.getName()+",";
			String tmpVal = prop.get(t).toString();
			if(prop.get(t) instanceof String) {
				tmpVal = "\'"+tmpVal+"\'";
			}
			values += tmpVal+","; 
		}
		keys = keys.substring(0, keys.length()-1);
		values = values.substring(0, values.length()-1);
		
		String sql = String.format("Insert into %s(%s) Values(%s);", tableName, keys, values);
		try {
			this.statement.execute(sql);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	//Read
	public <T> ArrayList<T> read(String sql, Class<T> clazzType) throws Exception
	{
		ArrayList<T> resultList = new ArrayList<T>();
        ResultSet result;
        ResultSetMetaData metaData;
        Field[] properties;
		try {
			result = this.statement.executeQuery(sql);
			metaData = result.getMetaData();
			T data = clazzType.getDeclaredConstructor().newInstance(); //new a generic need the class type
			properties = data.getClass().getDeclaredFields();
			while(result.next())
			{
				data = clazzType.getDeclaredConstructor().newInstance(); //point to a new memory
				for(int i=0; i<metaData.getColumnCount(); i++) //get database field
				{
					for(Field prop : properties) // get model field
					{
						if(prop.getName().equals(metaData.getColumnLabel(i+1))) //if match, then set value
						{
							try {
								
								prop.set(data, result.getObject(metaData.getColumnLabel(i+1)));
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
				resultList.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	public <T> boolean update(T t, Class<T> clazzType) throws Exception
	{
		Field[] properties = t.getClass().getDeclaredFields();
		String tableName = t.getClass().getName().replace(t.getClass().getPackageName()+".", "");
		String PK = this.getPK(tableName);
		String PKValue = "";
		String setStatement = "";
		String sql = "";
		
		for(Field prop : properties)
		{
			if(prop.getName().equals(PK)) {
				PKValue = prop.get(t).toString();
				continue;
			}
			
			String tmpVal = prop.get(t).toString();
			if(prop.get(t) instanceof String) {
				tmpVal = "\'"+tmpVal+"\'";
			}
			setStatement += prop.getName()+"="+tmpVal+",";
		}
		setStatement = setStatement.substring(0, setStatement.length()-1);
		
		sql += "update "+tableName;
		sql += " set "+setStatement;
		sql += " where "+PK+"="+PKValue+";";
		
		try {
			this.statement.execute(sql);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public <T> boolean delete(T t, Class<T> clazzType) throws Exception
	{
		Field[] properties = t.getClass().getDeclaredFields();
		String tableName = t.getClass().getName().replace(t.getClass().getPackageName()+".", "");
		String PK = this.getPK(tableName);
		String PKValue = "";
		String sql = "";
		
		for(Field prop : properties)
		{
			if(prop.getName().equals(PK)) {
				PKValue = prop.get(t).toString();
				break;
			}
		}
		
		sql = String.format("delete from %s where %s=%s", tableName, PK, PKValue);
		try {
			this.statement.execute(sql);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean isAutoIncreament(String table, String col) throws SQLException
	{
		//get metadata and get columns
		ResultSet rs = this.connection.getMetaData().getColumns(null, null, table, null);
		while(rs.next())
		{
			if(rs.getString("COLUMN_NAME").equals(col)) {
				return rs.getString("IS_AUTOINCREMENT").equals("YES");
			}
		}
		return false;
	}
	
	private String getPK(String tableName) throws SQLException
	{
		ResultSet result;
        String sql = String.format("show index from %s;", tableName);
        result = this.statement.executeQuery(sql);
        while(result.next())
        {
        	if(result.getString("Key_name").equals("PRIMARY")) {
        		return  result.getString("Column_name");
        	}
        }
        
		return "";
	}
	
	private void initState()
	{
		ConfigReader reader = new ConfigReader();
		try {
			HashMap<String, String> map = reader.readFile("resource/DBconfig.txt");
			for(Map.Entry<String, String> entry : map.entrySet())
			{
				if(entry.getKey().equals("username")) {
					this.username = entry.getValue();
				}else if(entry.getKey().equals("password")){
					this.password = entry.getValue();
				}else if(entry.getKey().equals("DBName")) {
					this.DBName = entry.getValue();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose() throws SQLException
	{
		this.connection.close();
	}
}

