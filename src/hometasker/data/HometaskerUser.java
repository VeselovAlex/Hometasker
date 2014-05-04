package hometasker.data;
import com.google.appengine.api.users.User;

public class HometaskerUser {
	public enum UserType {GUEST, STUDENT, TEACHER, ADMIN}
	private User user;
	private Person person;
	private UserType userType;
	
	public HometaskerUser(User googleUser) throws AuthorityException {
		this.user = googleUser;
		if (this.userType != UserType.ADMIN && googleUser != null) {
			this.person = Person.getByNickname(this.user.getNickname());
			if (person == null)
				this.userType = UserType.GUEST;
			else if (person instanceof Teacher)
				this.userType = UserType.TEACHER;
			else if (person instanceof Student)
				this.userType = UserType.STUDENT;
			else
				throw new AuthorityException();
		} else 
			this.person = null;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public Person getPerson() {
		return this.person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public UserType getUserType() {
		return this.userType;
	}
	
	public void setUserType(UserType type) {
		this.userType = type;
	}
}
