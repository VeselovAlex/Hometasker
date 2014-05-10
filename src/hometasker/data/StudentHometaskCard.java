package hometasker.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
@Entity(name = "Card")
@NamedQueries({
	@NamedQuery(name = "Card.getByStudentId", query = "SELECT c FROM Card c WHERE c.studentId = :id"),
	@NamedQuery(name = "Card.getByTaskId", query = "SELECT c FROM Card c WHERE c.taskId = :id"),
	@NamedQuery(name = "Card.getByStudentAndTaskId", query = "SELECT c FROM Card c WHERE c.taskId = :taskId AND c.studentId = :sId")
})
public class StudentHometaskCard implements Serializable {
	public enum TaskProgress {NOT_STARTED, NOT_VIEWED, MISTAKE, COMPLETED, ERROR}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	private long studentId;
	private long taskId;
	private float mark;
	@Basic
	private String taskFileUrl;
	@Basic
	private TaskProgress status = TaskProgress.NOT_STARTED;
	
	public Key getKey() {
		return this.key;
	}
	
	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	
	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public float getMark() {
		return mark;
	}

	public void setMark(float mark) {
		this.mark = mark;
	}

	public TaskProgress getStatus() {
		return status;
	}

	public void setStatus(TaskProgress status) {
		this.status = status;
	}

	public String getTaskFileUrl() {
		return taskFileUrl;
	}

	public void setTaskFileUrl(String taskFileUrl) {
		this.taskFileUrl = taskFileUrl;
		this.setStatus(TaskProgress.NOT_VIEWED);
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
	
	public static List<StudentHometaskCard> getByTaskId(long id) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<StudentHometaskCard> query = em.createNamedQuery("Card.getByTaskId", StudentHometaskCard.class).setParameter("id", id);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public static StudentHometaskCard getByStudentAndTask(long studentId, long taskId) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<StudentHometaskCard> query = em.createNamedQuery("Card.getByStudentAndTaskId", StudentHometaskCard.class).
					setParameter("taskId", taskId).setParameter("sId", studentId);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}
	
	public void save() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			if (key == null)
				em.persist(this);
			else
				em.merge(this);
			tr.commit();
		} finally {
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
