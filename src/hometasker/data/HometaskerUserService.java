package hometasker.data;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class HometaskerUserService {
	private UserService userService = UserServiceFactory.getUserService();
	
	public UserService getUserService() {
		return this.userService;
	}
	
	public HometaskerUser getCurrentUser() {
		try {
			if (userService.getCurrentUser() == null)
				return null;
			return new HometaskerUser(userService.getCurrentUser());
		} catch (AuthorityException e) {
			return null;
		}
	}
}
