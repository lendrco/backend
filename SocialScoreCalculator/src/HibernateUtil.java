/*
*  The intention of this util class is to hide the details of the hibernate usage from the application
*  calling it. 
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

public class HibernateUtil {
   private static final SessionFactory sessionFactory = buildSessionFactory();
   private static Log logger = LogFactory.getLog(HibernateUtil.class);
           
   private static SessionFactory buildSessionFactory() 
   {
       try {
           // Create the SessionFactory from hibernate.cfg.xml
           Configuration configuration = new Configuration();
           // configuration.configure("resources/hibernate.cfg.xml"); // It's unclear if I need this line
           configuration.addSqlFunction("group_concat", new StandardSQLFunction("group_concat", new StringType()));
           return configuration.configure("hibernate.cfg.xml").buildSessionFactory();
       }catch (HibernateException e){
           // logger can be null the first time because buildSessionFactory is created statically
           if(logger == null) 
               logger = LogFactory.getLog(HibernateUtil.class);
           logger.error("Failed to build SessionFactory ", e);
           throw e;
       }catch (Throwable ex) {
           // Make sure you log the exception, as it might be swallowed
           logger.error("Initial SessionFactory creation failed.", ex);
           throw new ExceptionInInitializerError(ex);
       }
   }
   
   public static SessionFactory getSessionFactory() {
       return sessionFactory;
   }

   //    Use Current Session 
   
   public static Session beginTransaction() {
       Session hibernateSession = HibernateUtil.getSession();
       hibernateSession.beginTransaction();
       return hibernateSession;
   }

   public static void commitTransaction() {
       HibernateUtil.getSession().getTransaction().commit();
   }

   public static void rollbackTransaction() {
       HibernateUtil.getSession().getTransaction().rollback();
   }

   public static void closeSession() {
       if(null != HibernateUtil.getSession()){
           HibernateUtil.getSession().close();
       }
   }

   public static Session getSession() {
       return sessionFactory.getCurrentSession();
   }
   
   //  Open close the new session
   
   public static void closeSession(Session session) {
       if(null != session){
           session.close();
       }
   }

   public static Session openSession() {
       return sessionFactory.openSession();
   }
   
   public static void commitTransaction(Session session) {
       if(null != session){
           session.getTransaction().commit();
       }
   }

   public static void rollbackTransaction(Session session) {
       if(null != session){
           session.getTransaction().rollback();
       }
   }
}