/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streaming.soapservice.services;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.soap.MTOM;
import streaming.soapservice.controllers.MoviesJpaController;
import streaming.soapservice.models.Movies;

/**
 *
 * @author Bunny Gaming
 */

@WebService(serviceName = "MovieStreamService")
@MTOM(enabled=true,threshold = 1000000)
@HandlerChain(file = "/MovieStreamService_handler.xml")
public class MovieStreamService {

    @WebMethod(operationName = "addMovie")
    public Movies addMovie(@WebParam(name = "moviesAdd") Movies mov){
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MOVIEUNIT");
            MoviesJpaController movRepo = new MoviesJpaController(emf);
            movRepo.create(mov);   
            return mov;
        } 
        
        
        catch (Exception ex) {
            Logger.getLogger(MovieStreamService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "updateMovie")
    public boolean updateShow(@WebParam(name = "updateMovie") Movies mov) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MOVIEUNIT");
            MoviesJpaController movRepo = new MoviesJpaController(emf);
            movRepo.edit(mov);   
            return true;
        } catch (Exception ex) {
            Logger.getLogger(MovieStreamService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName = "deleteMovie")
    public boolean deleteShow(@WebParam(name = "deleteMovie") BigDecimal id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MOVIEUNIT");
            MoviesJpaController movRepo = new MoviesJpaController(emf);
            movRepo.destroy(id);   
            return true;
        } catch (Exception ex) {
            Logger.getLogger(MovieStreamService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    

    @WebMethod(operationName = "viewMovie")
    public List<Movies> viewMovie(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MOVIEUNIT");
        MoviesJpaController movRepo = new MoviesJpaController(emf);
        List<Movies> findShowEntities = movRepo.findMoviesEntities();
        return findShowEntities;
    }
    

    @WebMethod(operationName = "findMovie")
    public Movies findMovie(BigDecimal id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MOVIEUNIT");
        MoviesJpaController movRepo = new MoviesJpaController(emf);
        Movies mov = movRepo.findMovies(id);
        return mov;
    }
    
}