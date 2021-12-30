package app.beans;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "t_sequence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sequence.findAll", query = "SELECT s FROM Sequence s"),
    @NamedQuery(name = "Sequence.findByPk", query = "SELECT s FROM Sequence s WHERE s.pkSequence = :pk"),
    @NamedQuery(name = "Sequence.findBySequenceName", query = "SELECT s FROM Sequence s WHERE s.sequenceName = :sequenceName")})
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_sequence")
    private Integer pkSequence;
    @Basic(optional = false)
    @Lob
    @Column(name = "actionFile")
    private byte[] actionFile;
    @Basic(optional = false)
    @Column(name = "sequenceName")
    private String sequenceName;
    @JoinColumn(name = "fk_user", referencedColumnName = "pk_user")
    @ManyToOne(optional = false)
    private User fkUser;

    public Sequence() {
    }

    public Sequence(Integer pkSequence) {
        this.pkSequence = pkSequence;
    }

    public Sequence(Integer pkSequence, byte[] actionFile, String sequenceName) {
        this.pkSequence = pkSequence;
        this.actionFile = actionFile;
        this.sequenceName = sequenceName;
    }

    public Integer getPkSequence() {
        return pkSequence;
    }

    public void setPkSequence(Integer pkSequence) {
        this.pkSequence = pkSequence;
    }

    public byte[] getActionFile() {
        return actionFile;
    }

    public void setActionFile(byte[] actionFile) {
        this.actionFile = actionFile;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
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
        hash += (pkSequence != null ? pkSequence.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sequence)) {
            return false;
        }
        Sequence other = (Sequence) object;
        return !((this.pkSequence == null && other.pkSequence != null) || (this.pkSequence != null && !this.pkSequence.equals(other.pkSequence)));
    }

    @Override
    public String toString() {
        return sequenceName;
    }

}
