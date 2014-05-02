package hometasker.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
@Entity(name = "Task")
@NamedQuery(name = "getAll", query = "SELECT t FROM Task t")
public class Task implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	//@Column(nullable = false)
	private long hometaskId;
	//@Column(nullable = false)
	private int taskNum;
	@Basic
	//@Column(nullable = false)
	private Text taskText;
	private boolean additional = false;
	private boolean fine = false;
	
	public Key getKey() {
		return this.key;
	}

	public long getHometaskId() {
		return hometaskId;
	}

	public void setHometaskId(long hometaskId) {
		this.hometaskId = hometaskId;
	}

	public int getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	
	public Text getText() {
		return this.taskText;
	}
	
	public void setText(Text text) {
		this.taskText = text;
	}

	public boolean isAdditional() {
		return additional;
	}

	public void setAdditional(boolean additional) {
		this.additional = additional;
	}

	public boolean isFine() {
		return fine;
	}

	public void setFine(boolean fine) {
		this.fine = fine;
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
	
	public static Task get(Key key) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			return em.find(Task.class, key);
		} finally {
			em.close();
		}
	}
	
	public static void delete(Task task) {
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
	
	public static List<Task> getAll() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Task> query = em.createNamedQuery("getAll", Task.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}

