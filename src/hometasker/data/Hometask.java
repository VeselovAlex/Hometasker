package hometasker.data;

import java.io.Serializable;
import java.sql.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
@Entity(name = "Hometask")
@NamedQueries ({
	@NamedQuery(name = "Hometask.getAllTasks", query = "SELECT t FROM Task t WHERE t.hometaskId = :id ORDER BY t.taskNum"),
	@NamedQuery(name = "Hometask.getAllHometasks", query = "SELECT h FROM Hometask h ORDER BY h.htNum"),
	@NamedQuery(name = "Hometask.getByGroupId", query = "SELECT h FROM Hometask h WHERE h.groupId = :gId ORDER BY h.htNum")
})
public class Hometask implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private long groupId;
	private int htNum;
	private String subject;
	@Basic
	@Temporal(TemporalType.DATE)
	private Date dStart;
	@Basic
	@Temporal(TemporalType.DATE)
	private Date dEnd;
	private boolean completed;
	
	public Key getKey() {
		return this.key;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getHometaskNum() {
		return htNum;
	}

	public void setHometaskNum(int htNum) {
		this.htNum = htNum;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getStartDate() {
		return dStart;
	}

	public void setStartDate(Date dStart) {
		this.dStart = dStart;
	}

	public Date getEndDate() {
		return dEnd;
	}

	public void setEndDate(Date dEnd) {
		this.dEnd = dEnd;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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
		}
		finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
	
	public static Hometask get(Key key) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			return em.find(Hometask.class, key);
		} finally {
			em.close();
		}
	}
	
	public static void delete(Hometask task) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.remove(task);
			tr.commit();
		} finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
	
	public static List<Hometask> getAll() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Hometask> query = em.createNamedQuery("Hometask.getAllHometasks", Hometask.class);
			return query.getResultList();	
		} finally {
			em.close();
		}
	}
	
	public static List<Hometask> getByGroupId(long groupId) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Hometask> query = em.createNamedQuery("Hometask.getByGroupId", Hometask.class).setParameter("gId", groupId);
			return query.getResultList();	
		} finally {
			em.close();
		}
	}
	
	public List<Task> getAllTasks() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Task> query = em.createNamedQuery("Hometask.getAllTasks", Task.class).setParameter("id", key.getId());
			return query.getResultList();	
		} finally {
			em.close();
		}
	}
}
