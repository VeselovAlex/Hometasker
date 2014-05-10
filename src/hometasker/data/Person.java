package hometasker.data;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")

@Entity(name = "Person")
@MappedSuperclass
public class Person implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Key key;
	@Column(nullable = false)
	private String surname;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	@Basic
	private String nickname;
	//@Basic
	private String email;
	
	Person(String surname, String firstName, String lastname) {
		this.surname = surname;
		this.lastName = lastname;
		this.firstName = firstName;
	}

	public Person() {
	}

	public Key getKey() {
		return this.key;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static Person getByNickname(String nickname) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Teacher> tQuery = em.createQuery("SELECT t FROM Teacher t WHERE t.nickname = :nickname", Teacher.class).setParameter("nickname", nickname);
			return tQuery.getSingleResult();
		} catch (NoResultException exc) {
			try {
				TypedQuery<Student> sQuery = em.createQuery("SELECT s FROM Student s WHERE s.nickname = :nickname", Student.class).setParameter("nickname", nickname);
				return sQuery.getSingleResult();
			} catch (NoResultException exc2) {
				return null;
			}
		} finally {
			em.close();
		}
	}
}
