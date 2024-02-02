package whitefoxdev.ftbd.tables;
import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.Player.Quest;
import whitefoxdev.ftbd.tables.Player.Role;
import whitefoxdev.ftbd.tables.Player.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patternRoles")
public class PatternRole extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true)
    private String name;

    private String rights;

    //==================================================================================================================
    //CONSTRUCTORS
    private PatternRole(){}

    public PatternRole(String name, String rights) {
        this();
        this.name = name;
        this.rights = rights;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }


    //==================================================================================================================
    //FUNCTIONS
    public Role createEmptyRole() {
        return new Role(this, new Date(), null);
    }

}
