package hometasker.data;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMFSingleton {
	private static final EntityManagerFactory instance = Persistence.createEntityManagerFactory("transactions-optional");
	private EMFSingleton() {}
	public static EntityManagerFactory get() {
		return instance;
	}
}
