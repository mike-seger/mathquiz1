package com.net128.application.mathquiz.persistence.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.net128.application.mathquiz.persistence.PersistenceManagerBean;
import com.net128.application.mathquiz.persistence.entities.User;
 
public class TestUser {
    public static void main(String[] args) {
    	new TestUser().test();
    }
    
    private void insert(EntityManager em) {
        em.getTransaction().begin();
        for (int i = 0; i < 100; i++) {
            User user = new User("user."+i, "password."+i);
            em.persist(user);
        }
        em.getTransaction().commit();
    }
    
    private void select(EntityManager em) {
        Query query = em.createQuery("SELECT COUNT(user) FROM User user");
        System.out.println("Total User: " + query.getSingleResult());

        TypedQuery<User> typedQuery = em.createQuery("SELECT user FROM User user", User.class);
        List<User> users = typedQuery.getResultList();
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    private void delete(EntityManager em) {
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM User user where user.name like 'user.%'");
        System.out.println("Number of Users deleted: " + query.executeUpdate());
        em.getTransaction().commit();
    }
    
    private void test() {
        EntityManagerFactory emf = PersistenceManagerBean.getInstance().getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
 
        insert(em);
        select(em);
        delete(em);

        em.close();
        emf.close();
    }
}
