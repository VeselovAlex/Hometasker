package hometasker.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
@Entity(name = "Student")
@NamedQueries({
	@NamedQuery(name = "Student.getAll", query = "SELECT s FROM Student s"),
	@NamedQuery(name = "Student.getByGroup", query = "SELECT s FROM Student s WHERE s.groupId = :gId")
})
public class Student extends Person {

	private long groupId;
	
	public Student(String surname, String firstName, String lastname) {
		super(surname, firstName, lastname);
	}
	
	public Student() {
		super();
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public static List<Student> getAll() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Student> query = em.createNamedQuery("Student.getAll", Student.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public static List<Student> getByGroup(long groupId) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Student> query = em.createNamedQuery("Student.getByGroup", Student.class).setParameter("gId", groupId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public void save() {
		EntityManagerFactory emf = EMFSingleton.get();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.persist(this);
			tr.commit();
		} catch (EntityExistsException e) {
			em.merge(get(this.getKey()));
			tr.commit();
		}
		finally {
			if (tr.isActive())
					tr.rollback();
			em.close();
		}
	}

	public static Student get(Key key) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			return em.find(Student.class, key);
		} finally {
			em.close();
		}
	}
	
	public static void delete(Student student) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.remove(student);
			tr.commit();
		} finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
}
