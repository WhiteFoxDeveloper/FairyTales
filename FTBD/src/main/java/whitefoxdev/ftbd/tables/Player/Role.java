package whitefoxdev.ftbd.tables.Player;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.PatternRole;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Roles")
public class Role extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private PatternRole patternRole;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReceipt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateWithdraw;

    //==================================================================================================================
    //CONSTRUCTORS

    private Role() {
    }

    public Role(PatternRole patternRole, Date dateReceipt, Date dateWithdraw) {
        this();
        this.patternRole = patternRole;
        this.dateReceipt = dateReceipt;
        this.dateWithdraw = dateWithdraw;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public PatternRole getPatternRole() {
        return patternRole;
    }

    public void setPatternRole(PatternRole patternRole) {
        this.patternRole = patternRole;
    }

    public Date getDateReceipt() {
        return dateReceipt;
    }

    public void setDateReceipt(Date dateReceipt) {
        this.dateReceipt = dateReceipt;
    }

    public Date getDateWithdraw() {
        return dateWithdraw;
    }

    public void setDateWithdraw(Date dateWithdraw) {
        this.dateWithdraw = dateWithdraw;
    }
}
