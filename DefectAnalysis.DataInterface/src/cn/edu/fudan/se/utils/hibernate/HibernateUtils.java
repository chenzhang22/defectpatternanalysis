/**
 * 
 */
package cn.edu.fudan.se.utils.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Lotay
 * 
 */
public class HibernateUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*String HIBERNATE_CONF_PATH ="src/com/huawei/conf/hibernate/hibernate.cfg.xml";
		HibernateFactory
				.getSessionFactory(HIBERNATE_CONF_PATH);*/
		
		
		
		String hql = "from SVNLogPath where LOG_DATE between '2013-04-22' and '2014-04-22'";
		
		System.out.println(retrieveObjects(hql, 0,10,
				"src/com/huawei/conf/hibernate/analysis/hibernate.cfg.xml"));
	}

	public static boolean save(Object obj, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return false;
		}
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			try {
				session.saveOrUpdate(obj);
			} catch (ClassCastException e) {
				System.out.println("object:" + obj);
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				System.out.println("object:" + obj);
				e.printStackTrace();
				return false;
			}
			session.getTransaction().commit();
		} catch (org.hibernate.HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			System.out.println("object:" + obj);
			return false;
		}
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return true;
	}

	@SuppressWarnings("rawtypes")
	public static int saveAll(Collection objs, final String hibernateConf) {
		int num = 0;
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return -1;
		}
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			for (Object obj : objs) {
				try {
					session.saveOrUpdate(obj);
					num++;
				} catch (ClassCastException e) {
					System.out.println(obj);
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println(obj);
					e.printStackTrace();
				}
			}
			session.getTransaction().commit();
		} catch (org.hibernate.HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return num;
	}
	
	public static int update(final String hql, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return -1;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery(hql);
		int result = q.executeUpdate();
		session.getTransaction().commit();
		sessionFactory.close();
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static void updateOrSaveAll(List objs, List<String> hqls, 
			String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		int index = 0;
		for (String hql : hqls) {
			Query q = session.createQuery(hql);
			int result = q.executeUpdate();
			if (result == 0) {
				session.saveOrUpdate(objs.get(index));
			}
			index ++;
		}
		session.getTransaction().commit();
		sessionFactory.close();
	}

	@SuppressWarnings("rawtypes")
	public static List retrieveAll(Class c, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return null;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from " + c.getSimpleName() + " as ccc");
		List objs = q.list();
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return objs;
	}

	@SuppressWarnings("rawtypes")
	public static List retrieveObjects(final String hql,
			final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return null;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery(hql);
		List objs = q.list();
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return objs;
	}

	@SuppressWarnings("rawtypes")
	public static List retrieveObjectsSql(final String sql,
			final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return null;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createSQLQuery(sql);
		List objs = q.list();
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return objs;
	}

	@SuppressWarnings("rawtypes")
	public static List retrieveObjects(Class c, final int start,
			final int size, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return null;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from " + c.getSimpleName());
		q.setFirstResult(start);
		q.setMaxResults(size);
		List objs = q.list();
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return objs;
	}
	
	@SuppressWarnings("rawtypes")
	public static List retrieveObjects(String hql, final int start,
			final int size, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return null;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery(hql);
		q.setFirstResult(start);
		q.setMaxResults(size);
		List objs = q.list();
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return objs;
	}

	@SuppressWarnings("rawtypes")
	public static long retrieveSize(Class c, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return -1;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("select count(*) from "
				+ c.getCanonicalName());
		try {
			return ((Long) q.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return 0;
	}

	public static long retrieveSize(String sql, final String hibernateConf) {
		SessionFactory sessionFactory = HibernateFactory
				.getSessionFactory(hibernateConf);
		if (sessionFactory == null) {
			return -1;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery(sql);
		try {
			return ((Long)(q.iterate().next())).longValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (session.isOpen()) {
			session.close();
		}
		sessionFactory.close();
		return 0;
	}
}
