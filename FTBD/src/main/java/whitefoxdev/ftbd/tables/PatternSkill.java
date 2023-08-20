package whitefoxdev.ftbd.tables;


import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.Player.Role;
import whitefoxdev.ftbd.tables.Player.Skill;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patternSkills")
public class PatternSkill extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private int maxLevel;

    //==================================================================================================================
    //CONSTRUCTORS
    private PatternSkill() {
    }

    public PatternSkill(String name, int maxLevel) {
        this();
        this.name = name;
        this.maxLevel = maxLevel;
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

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    //==================================================================================================================
    //FUNCTIONS
    public Skill createEmptySkill() {
        return new Skill(this, 0, 0);
    }

}
