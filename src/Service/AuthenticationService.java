package Service;

import Model.Response;

public class AuthenticationService {
	//regex to test verified email and password
	//authentication code and email
	
	//all share the same sendEmailService
	private static SendEmailService sendEmailService;
	
	public void initSendEmailService(String email)
	{
		sendEmailService = new SendEmailService(email);
	}
	
	public static SendEmailService getSendEmailService()
	{
		return sendEmailService;
	}
	
	public Response checkValidEmailFormat(String email)
	{
		String pattern = "^([a-zA-Z0-9])+@(([a-zA-Z])+[.])+([a-z])+$";
		boolean isValid = email.matches(pattern);
		if(isValid) {
			return new Response(true, "Valid email format!");
		}else {
			return new Response(false, "Invalid email format!");
		}	
	}
	
	public Response checkValidPasswordFormat(String password)
	{
		String pattern = "^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-z]+))[0-9a-zA-Z]*$";
		boolean isValid = password.matches(pattern) && password.length()>=8;
		if(isValid) {
			return new Response(true, "Valid password format!");
		}else {
			if(password.length()<8) {
				return new Response(false, "Password length must be longer than 8");
			}else {
				return new Response(false, "Password must include at least 1 digit and letter");
			}
		}
	}
}
