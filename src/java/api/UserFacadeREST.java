/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import entity.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;


class FbResponse {
    public String name;
    public String id;
    
    @Override
    public String toString() {
        return name + id;
    }
}

class UserResponse {
    public Integer id;
    public String fbId;
}

/**
 *
 * @author Hung-PC
 */
@Stateless
@Path("entity.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "Chagram_v2PU")
    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(User entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    public User find(Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private String getFbData(String access_token) throws MalformedURLException, IOException {
      StringBuilder result = new StringBuilder();
      URL url = new URL("https://graph.facebook.com/me?access_token=" + access_token);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = rd.readLine()) != null) {
         result.append(line);
      }
      rd.close();
      return result.toString();
    }
    
    public List<User> findByFbId(String fbId) {
        return this.getEntityManager().createQuery("SELECT u from User u where u.fbId = :value1")
                 .setParameter("value1", fbId).getResultList();
    }
    
    
    @GET
    @Path("/login/{access_token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("access_token") String access_token) throws IOException {
        String data = this.getFbData(access_token);
        Gson gs = new Gson();
        FbResponse fa = gs.fromJson(data, FbResponse.class);
        List<User> countUser = this.findByFbId(fa.id);
        User u;
        if(countUser.isEmpty()){
            // add new User
            u = new User();
            u.setFbId(fa.id);
            u.setName(fa.name);
            u.setAvatar("http://graph.facebook.com/" + fa.id + "/picture?type=square");
            this.create(u);
        } else {
            u = countUser.get(0);
        }
        
        UserResponse ur = new UserResponse();
        ur.fbId = u.getFbId();
        ur.id = u.getId();
        
        return gs.toJson(ur);
    }
    
}
