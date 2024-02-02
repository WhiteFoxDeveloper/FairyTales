package whitefoxdev.ftbd.tables.Player;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.PatternSkill;

import javax.persistence.*;

@Entity
@Table(name = "Skills")
public class Skill extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private PatternSkill patternSkill;

    private int level;

    private int experience;

    //==================================================================================================================
    //CONSTRUCTORS

    private Skill(){}

    public Skill(PatternSkill patternSkill, int level, int experience) {
        this();
        this.patternSkill = patternSkill;
        this.level = level;
        this.experience = experience;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public PatternSkill getPatternSkill() {
        return patternSkill;
    }

    public void setPatternSkill(PatternSkill patternSkill) {
        this.patternSkill = patternSkill;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
