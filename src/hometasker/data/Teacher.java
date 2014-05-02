package hometasker.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
@Entity(name = "Teacher")
@NamedQueries({
	@NamedQuery(name = "Teacher.getAll", query = "SELECT t FROM Teacher t")
})
public class Teacher extends Person {
	
	public static List<Teacher> getAll() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Teacher> query = em.createNamedQuery("Teacher.getAll", Teacher.class);
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
			if (key == null)
				em.persist(this);
			else
				em.merge(this);
			tr.commit();
		}
		finally {
			if (tr.isActive())
					tr.rollback();
			em.close();
		}
	}

	public static Teacher get(Key key) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			return em.find(Teacher.class, key);
		} finally {
			em.close();
		}
	}
	
	public static void delete(Teacher teacher) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.remove(teacher);
			tr.commit();
		} finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
}
