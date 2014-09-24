/**
 * 
 */
package cn.edu.fudan.se.utils.hibernate;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Lotay
 * 
 */
public class HibernateFactory {
	private static SessionFactory buildSessionFactory(final String hibernateConf) {
		try {
			return new Configuration().configure(new File(hibernateConf))
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory(final String hibernateConf) {
		return buildSessionFactory(hibernateConf);
	}
}
