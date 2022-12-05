package Service;

import java.sql.SQLException;
import java.util.List;

import Controller.RegisterController;
import Model.Response;
import Model.User;
import SQL.BaseDB;

public class UserService {
	private BaseDB db;
	
	public UserService()
	{
		try {
			this.db = new BaseDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createUser(User user)
	{
		try {
			return this.db.<User>create(user, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Response register(User user)
	{
		try {
			List<User> check = db.read(String.format("select * from User where account='%s' and password='%s'", user.account, user.password), User.class);
			if(check.size() == 0) {
				boolean suc = db.<User>create(user, User.class);
				return new Response(suc, "Register Success!");
			}else {
				return new Response(false, "The account or password has already existed.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(false, "error occured");
		}
	}
	
	public Response login(String account, String password)
	{
		try {
			List<User> res = db.<User>read(String.format("select * from User where account='%s' and password='%s'", account, password), User.class);
			return res.size()==0 ? new Response(false, "使用者不存在") : new Response(true, "Login succesfully!", res.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false, "Error occured");
		}
	}
	
	public Response updateUser(User user) 
	{
		try {
			boolean success = this.db.update(user, User.class);
			if(success) {
				return new Response(true, "Update user successfully");
			}
			return new Response(false, "Update user failed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(false, "Somthing wrong when updating user");
		}
	}
}
