/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import entity.User;

/**
 *
 * @author Hung-PC
 */

public class UserFacadeREST extends AbstractFacade<User> {

    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
        em = Persistence.createEntityManagerFactory("Chagram_v2PU").createEntityManager();
    }

    public void create(User entity) {
        super.create(entity);
    }

    public void edit(Integer id, User entity) {
        super.edit(entity);
    }

    public void remove(Integer id) {
        super.remove(super.find(id));
    }

    public User find(Integer id) {
        return super.find(id);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }
    
    public List<User> findByFbId(String fbId) {
        return em.createQuery("SELECT u from User u where u.fbId = :value1")
                 .setParameter("value1", fbId).getResultList();
    }

    public List<User> findRange(Integer from, Integer to) {
        return super.findRange(new int[]{from, to});
    }

    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }    
}
