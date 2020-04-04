/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streaming.soapservice.controllers;

import java.io.Serializable;
import java.math.BigDecimal;
 
import java.util.logging.Level; 
import java.util.logging.Logger; 
  
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import streaming.soapservice.controllers.exceptions.NonexistentEntityException;
import streaming.soapservice.controllers.exceptions.PreexistingEntityException;
import streaming.soapservice.models.Movies;

/**
 *
 * @author Bunny Gaming
 */
public class MoviesJpaController implements Serializable {

    public MoviesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movies movies) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(movies);
            em.getTransaction().commit();
        } 
        catch (ConstraintViolationException e) {
            Logger logger 
            = Logger.getLogger( 
                MoviesJpaController.class.getName()); 
       logger.log(Level.SEVERE,"Exception: ");
       e.getConstraintViolations().forEach(err->logger.log(Level.SEVERE,err.toString()));
    }
        
        catch (Exception ex) {
            if (findMovies(movies.getId()) != null) {
                throw new PreexistingEntityException("Movies " + movies + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movies movies) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            movies = em.merge(movies);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = movies.getId();
                if (findMovies(id) == null) {
                    throw new NonexistentEntityException("The movies with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movies movies;
            try {
                movies = em.getReference(Movies.class, id);
                movies.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movies with id " + id + " no longer exists.", enfe);
            }
            em.remove(movies);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movies> findMoviesEntities() {
        return findMoviesEntities(true, -1, -1);
    }

    public List<Movies> findMoviesEntities(int maxResults, int firstResult) {
        return findMoviesEntities(false, maxResults, firstResult);
    }

    private List<Movies> findMoviesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movies.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Movies findMovies(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movies.class, id);
        } finally {
            em.close();
        }
    }

    public int getMoviesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movies> rt = cq.from(Movies.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
