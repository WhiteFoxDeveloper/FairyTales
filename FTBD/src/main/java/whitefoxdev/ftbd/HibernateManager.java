package whitefoxdev.ftbd;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateManager {

    private static SessionFactory sessionFactory;
    private static Session session;

    /**
     * Соединяет базу данных с приложением, если до этого соединение уже было установлено, то функция не выполняется.
     */
    public static void init() {
        if (sessionFactory != null) {
            if (!sessionFactory.isClosed()) {
                return;
            }
        }
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        session = sessionFactory.openSession();
    }

    /**
     * Добавляет или обновляет объект в базе данных.
     */
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
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Возвращает объект определенного типа(clazz) из базы данных по значению поля(value) в столбце(fieldName).
     */
    public static Object getObject(Class clazz, String fieldName, Object value) {
        try {
            String hql = "FROM " + clazz.getName() + " WHERE " + fieldName + " = :value";
            Query query = session.createQuery(hql);
            query.setParameter("value", value);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Возвращает все объекты по определенному типу(clazz).
     */
    public static List<Object> getAllObjects(Class clazz) {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<Object> root = criteriaQuery.from(clazz);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Удаляет объект из базы данных.
     */
    public static boolean removeObject(Object object) {
        try {
            session.remove(object);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Удаляет все объекты определенного типа(clazz).
     */
    public static void removeAllObjectsFromTable(Class clazz) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM " + clazz.getName();
            session.createQuery(hql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Удаляет объект из сессии Hibernate.
     */
    public static void evict(Object object) {
        session.evict(object);
    }

    /**
     * Закрывает соединение базы данных с приложением, если до этого соединения отсутствовало, то функция не выполняется.
     */
    public static void close() {
        if (sessionFactory == null) {
            return;
        }
        if (sessionFactory.isClosed()) {
            return;
        }
        session.close();
        sessionFactory.close();
    }
}
