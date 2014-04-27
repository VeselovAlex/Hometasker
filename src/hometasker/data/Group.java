package hometasker.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
@Entity(name = "Group")
@NamedQueries ({
	@NamedQuery(name = "Group.getAll", query = "SELECT g FROM Group g ORDER BY g.groupNum, g.subgroupNum"),
	@NamedQuery(name = "Group.getByTeacher", query = "SELECT g FROM Group g WHERE g.teacherId = :id ORDER BY g.groupNum, g.subgroupNum"),
	@NamedQuery(name = "Group.getByNum", query = "SELECT g FROM Group g WHERE g.groupNum = :num ORDER BY g.subgroupNum")
})
public class Group implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	private long teacherId;
	private int groupNum;
	private int subgroupNum;
	
	public Key getKey() {
		return key;
	}
	
	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}
	
	public int getGroupNum() {
		return groupNum;
	}
	
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	
	public int getSubgroupNum() {
		return subgroupNum;
	}
	
	public void setSubgroupNum(int subgroupNum) {
		this.subgroupNum = subgroupNum;
	}
	
	public static List<Group> getAll() {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Group> query = em.createNamedQuery("Group.getAll", Group.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public static List<Group> getByTeacher(long teacherId) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Group> query = em.createNamedQuery("Group.getByTeacher", Group.class).setParameter("id", teacherId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	public static List<Group> getByNumber(int number) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			TypedQuery<Group> query = em.createNamedQuery("Group.getByNum", Group.class).setParameter("num", number);
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
			em.merge(get(this.key));
			tr.commit();
		}
		finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
	
	public static Group get(Key key) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		try {
			return em.find(Group.class, key);
		} finally {
			em.close();
		}
	}
	
	public static void delete(Group group) {
		EntityManager em = EMFSingleton.get().createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		try {
			em.remove(group);
			tr.commit();
		} finally {
			if (tr.isActive())
				tr.rollback();
			em.close();
		}
	}
}
