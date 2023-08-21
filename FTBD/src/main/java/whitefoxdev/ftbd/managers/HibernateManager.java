package whitefoxdev.ftbd.managers;


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

    private SessionFactory sessionFactory;
    private Session session;
    private String hibernateCfgXmlPath;

    public HibernateManager(String hibernateCfgXmlPath){
        this.hibernateCfgXmlPath = hibernateCfgXmlPath;
    }

    /**
     * Соединяет базу данных с приложением, если до этого соединение уже было установлено, то функция не выполняется.
     */
    public void init() {
        if(sessionFactory != null) {
            if (sessionFactory.isOpen()) {
                return;
            }
        }
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure(hibernateCfgXmlPath).build();
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            session = sessionFactory.openSession();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Добавляет или обновляет объект в базе данных.
     */
    public void addObject(Object object) {
        if (object == null) {
            return;
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
            throw e;
        }
    }

    /**
     * Возвращает объект определенного типа(clazz) из базы данных по значению поля(value) в столбце(fieldName).
     */
    public <T> T getObject(Class<T> clazz, String fieldName, Object value) {
        try {
            String hql = "FROM " + clazz.getName() + " WHERE " + fieldName + " = :value";
            Query query = session.createQuery(hql);
            query.setParameter("value", value);
            return (T) query.uniqueResult();
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
    }

    /**
     * Возвращает все объекты по определенному типу(clazz).
     */
    public <T> List<T> getAllObjects(Class<T> clazz) {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<T> root = criteriaQuery.from(clazz);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
    }

    /**
     * Удаляет объект из базы данных.
     */
    public void removeObject(Object object) {
        try {
            session.remove(object);
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
    }

    /**
     * Удаляет все объекты определенного типа(clazz).
     */
    public void removeAllObjectsFromTable(Class clazz) {
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
            //e.printStackTrace();
            throw e;
        }
    }

    /**
     * Удаляет объект из сессии Hibernate.
     */
    public void evict(Object object) {
        try {
            session.evict(object);
        }catch (Exception e){
            //e.printStackTrace();
            throw e;
        }
    }

    /**
     * Закрывает соединение базы данных с приложением, если до этого соединения отсутствовало, то функция не выполняется.
     */
    public void close() {
        if (sessionFactory == null) {
            return;
        }
        if (sessionFactory.isClosed()) {
            return;
        }
        try{
            sessionFactory.close();
        }catch (Exception e){
            //e.printStackTrace();
            throw e;
        }
    }
}
