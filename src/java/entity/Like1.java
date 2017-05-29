/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hung-PC
 */
@Entity
@Table(name = "like", catalog = "photo_social_network", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Like1.findAll", query = "SELECT l FROM Like1 l")
    , @NamedQuery(name = "Like1.findById", query = "SELECT l FROM Like1 l WHERE l.id = :id")
    , @NamedQuery(name = "Like1.findByUserId", query = "SELECT l FROM Like1 l WHERE l.userId = :userId")
    , @NamedQuery(name = "Like1.findByImageId", query = "SELECT l FROM Like1 l WHERE l.imageId = :imageId")
    , @NamedQuery(name = "Like1.findByDateCreated", query = "SELECT l FROM Like1 l WHERE l.dateCreated = :dateCreated")})
public class Like1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "image_id")
    private int imageId;
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    public Like1() {
    }

    public Like1(Integer id) {
        this.id = id;
    }

    public Like1(Integer id, int userId, int imageId) {
        this.id = id;
        this.userId = userId;
        this.imageId = imageId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Like1)) {
            return false;
        }
        Like1 other = (Like1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Like1[ id=" + id + " ]";
    }
    
}
