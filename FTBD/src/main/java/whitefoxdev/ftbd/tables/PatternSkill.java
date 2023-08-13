package whitefoxdev.ftbd.tables;


import whitefoxdev.ftbd.abstracts.ObjectToString;

import javax.persistence.*;

@Entity
@Table(name = "patternSkills")
public class PatternSkill extends ObjectToString {
    /**
     * =================================================================================================================
     * FIELDS
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private int maxLevel;

    /**
     * =================================================================================================================
     * CONSTRUCTORS
     */
    private PatternSkill() {
    }

    public PatternSkill(String name, int maxLevel) {
        this();
        this.name = name;
        this.maxLevel = maxLevel;
    }

    /**
     * =================================================================================================================
     * GETTERS_AND_SETTERS
     */

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


}
