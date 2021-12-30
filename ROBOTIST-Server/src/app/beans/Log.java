package app.beans;

import static app.utils.DateHandler.formatDate;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "t_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l"),
    @NamedQuery(name = "Log.findByPk", query = "SELECT l FROM Log l WHERE l.pkLog = :pk"),
    @NamedQuery(name = "Log.findByStartTime", query = "SELECT l FROM Log l WHERE l.startTime = :startTime"),
    @NamedQuery(name = "Log.findByEndTime", query = "SELECT l FROM Log l WHERE l.endTime = :endTime")})

public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_log")
    private Integer pkLog;
    @Basic(optional = false)
    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Basic(optional = false)
    @Lob
    @Column(name = "picture")
    private byte[] picture;
    @JoinColumn(name = "fk_user", referencedColumnName = "pk_user")
    @ManyToOne(optional = false)
    private User fkUser;

    public Log() {
    }

    public Log(Integer pkLog) {
        this.pkLog = pkLog;
    }

    public Log(Integer pkLog, Date startTime, byte[] picture) {
        this.pkLog = pkLog;
        this.startTime = startTime;
        this.picture = picture;
    }

    public Integer getPkLog() {
        return pkLog;
    }

    public void setPkLog(Integer pkLog) {
        this.pkLog = pkLog;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkLog != null ? pkLog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        return !((this.pkLog == null && other.pkLog != null) || (this.pkLog != null && !this.pkLog.equals(other.pkLog)));
    }

    @Override
    public String toString() {
        if (endTime != null) {
            return pkLog + " - User : (" + fkUser.getUsername() + ") - From: (" + formatDate(startTime) + ") to (" + formatDate(endTime) + ")";
        } else {
            return pkLog + " -> User : (" + fkUser.getUsername() + ") - From: (" + formatDate(startTime) + ") to now";
        }
    }


}
