package hometasker.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

@SuppressWarnings("serial")
@Entity(name = "Card")
@NamedQueries({
	@NamedQuery(name = "Card.getByStudentId", query = "SELECT c FROM Card c WHERE c.studentId = :id"),
	@NamedQuery(name = "Card.getByHometaskId", query = "SELECT c FROM Card c WHERE c.htId = :id")
})
public class StudentHometaskCard implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private long htId;
	@Basic
	private List<StudentTaskProgress> progress;
	
	private long studentId;

	public Key getKey() {
		return this.key;
	}
	
	public long getHtId() {
		return htId;
	}

	public void setHtId(long htId) {
		this.htId = htId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	
	public void resetProgress() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE t.hometaskId = :htId ORDER BY t.taskNum", Task.class).setParameter("htId", this.htId);
			List<Task> result = query.getResultList();
			this.progress = new LinkedList<StudentTaskProgress>();
			for (Task task : result)
			{
				StudentTaskProgress progress = new StudentTaskProgress();
				progress.setTaskId(task.getKey().getId());
				progress.setCardId(this.key.getId());
				this.progress.add(progress);
			}
			
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}
	
	public List<StudentTaskProgress> getProgress() {
		return this.progress;
	}
	
	public void setMark(int number, float mark) {
		progress.get(number).setMark(mark);
	}
	
	public void setTaskFileUrl(int number, Link link) {
		progress.get(number).setTaskFileUrl(link);
	}
	
	public static List<StudentHometaskCard> getByStudentId(long id) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<StudentHometaskCard> query = em.createNamedQuery("Card.getByStudentId", StudentHometaskCard.class).setParameter("id", id);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public static List<StudentHometaskCard> getByHometaskId(long id) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<StudentHometaskCard> query = em.createNamedQuery("Card.getByHometaskId", StudentHometaskCard.class).setParameter("id", id);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public void save() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.persist(this);
			tr.commit();
		} catch (EntityExistsException e) {
			tr.commit();
		}
		finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
	
	public static StudentHometaskCard get(Key key) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			return em.find(StudentHometaskCard.class, key);
		} finally {
			em.close();
		}
	}
	
	public static void delete(StudentHometaskCard card) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.remove(card);
			tr.commit();
		} finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
}
