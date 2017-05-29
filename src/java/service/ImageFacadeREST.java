/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.google.gson.Gson;
import entity.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

import lib.ImageProcessing;
import api.UserFacadeREST;
import entity.User;
import java.util.ArrayList;
import javax.ejb.EJB;

class ImageRender {
    public int user_id;
    public String description;
    public String image;
    public int filter;
}

class ImageResponse {
    public int id;
    public int user_id;
    public String user_name;
    public String user_avatar;
    public String description;
    public String file_name;
}
/**
 *
 * @author Hung-PC
 */
@Stateless
@Path("image")
public class ImageFacadeREST extends AbstractFacade<Image> {

    @EJB
    private UserFacadeREST userFacadeREST;

    @PersistenceContext(unitName = "Chagram_v2PU")
    private EntityManager em;

    public ImageFacadeREST() {
        super(Image.class); 
    }
    
    @Context ServletContext servletContext;

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Image entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Image entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Image find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Image> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Image> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    public String writeTempImage(String base64) throws FileNotFoundException, IOException {
        base64 = base64.split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        String fileLocation = Paths.get(servletContext.getRealPath("/"), "/img/Image.png").toString();
        //String output = Paths.get(servletContext.getRealPath("/"), "/img/Image-output.png").toString();
        //File imageFile = new File(servletContext.getRealPath("/") + "../../web/img/Image.png");
        File imageFile2 = new File(fileLocation);
        
        

        //ImageIO.write(bufferedImage, "png", imageFile);
        ImageIO.write(bufferedImage, "png", imageFile2);
        
        return fileLocation;
    }
    
    public String renderFilter(String filename, int type) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        String output = Paths.get(servletContext.getRealPath("/"), "img",UUID.randomUUID().toString() + ".png").toString();
        switch(type) {
            case 0: 
                ImageProcessing.Sharpness(filename, output);
                break;
            case 1: 
                ImageProcessing.Brightness(filename, output);
                break;
            case 2: 
                ImageProcessing.GrayScale(filename, output);
                break;
            default: 
                ImageProcessing.Blur(filename, output);
                break;
        }
        return output;
    }
    
    
    public String getBase64FromImage(String filename) throws FileNotFoundException, IOException {
        File originalFile = new File(filename);
        String encodedBase64 = null;
        FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
        byte imageData[] = new byte[(int) originalFile.length()];
        fileInputStreamReader.read(imageData);
        encodedBase64 = DatatypeConverter.printBase64Binary(imageData);
        return encodedBase64;
    }
    
    public boolean removeTempFile(String Filename) throws IOException {
        File file = new File(Filename);
        if (file.exists()) {
            System.out.println("Delete file in here ===========================");
//            FileDeleteStrategy.FORCE.delete(file);
            file.delete();     
        }
        return true;
    }
    
    public String getUserFolder(int user_id) {
        File theDir;
        theDir = new File(Paths.get(servletContext.getRealPath("/"), "img", Integer.toString(user_id)).toString());

        // if the directory does not exist, create it
        if (!theDir.exists()) {

            try{
                theDir.mkdir();
            } 
            catch(SecurityException se){
                //handle it
            }        
        }

        
        return theDir.toString();
    }
    
    public String writeFile(String base64, int userId) throws IOException {
        base64 = base64.split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        String imgName = UUID.randomUUID().toString() + ".png";
        String output = Paths.get(getUserFolder(userId), imgName).toString();
        File imageFile2 = new File(output);
        //ImageIO.write(bufferedImage, "png", imageFile);
        ImageIO.write(bufferedImage, "png", imageFile2);
        
        return imgName;
    }
    
    @POST
    @Path("/render")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String renderImage(String json) throws IOException{
        Gson gs = new Gson();
        ImageRender info = gs.fromJson(json, ImageRender.class);
        //write temporatory file to local disk
        String filename = writeTempImage(info.image);
        //Add filter
        System.out.println(info.filter);
        //
        String output = "";
        try {
            output = renderFilter(filename, info.filter);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(ImageFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ImageFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ImageFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        //get output
        String filebase64 = getBase64FromImage(output); 
        //Delete file
        //Remove input
        removeTempFile(filename);
        //remove output
        removeTempFile(output);
        return gs.toJson(filebase64);
    } 
    
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadImage(String json) throws IOException{
        Gson gs = new Gson();
        ImageRender info = gs.fromJson(json, ImageRender.class);
        
        //write file to local disk
        String filename = writeFile(info.image, info.user_id);
        // Add to database
        Image img = new Image();
        User u = userFacadeREST.find(info.user_id);
        img.setUserId(u);
        img.setFileName(filename);
        img.setDescription(info.description);
        this.create(img);
        return gs.toJson(null);
    } 
    
    @GET
    @Path("/all")
    @Produces({ MediaType.APPLICATION_JSON})
    public String loadAll() {
        List<Image> images = super.findAll();
        List<ImageResponse> res = new ArrayList<>();
        
        for(Image img : images) {
            ImageResponse t = new ImageResponse();
            t.id = img.getId();
            t.user_id = img.getUserId().getId();
            t.user_name = img.getUserId().getName();
            t.user_avatar = img.getUserId().getAvatar();
            t.description = img.getDescription();
            t.file_name = img.getFileName();
            
            res.add(t);
        }
         
        Gson gs = new Gson();
        return gs.toJson(res);
    }
}
