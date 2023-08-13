package whitefoxdev.ftbd;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateManager {

    private static SessionFactory sessionFactory;
    private static Session session;

    public static void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        session = sessionFactory.openSession();
    }

    public static boolean addObject(Object object) {
        if (object == null) {
            return false;
        }
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            //e.printStackTrace();
            return false;
        }
        return true;
    }


    public static Object getObject(String className, String fieldName, String value) {
        try {
            String hql = "FROM " + className + " WHERE " + fieldName + " = :value";
            Query query = session.createQuery(hql);
            query.setParameter("value", value);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Object> getAllObjects(Class clazz) {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<Object> root = criteriaQuery.from(clazz);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removeObject(Object object){
        try{
            session.remove(object);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }


}
