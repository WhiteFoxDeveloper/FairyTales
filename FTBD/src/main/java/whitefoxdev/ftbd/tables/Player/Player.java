package whitefoxdev.ftbd.tables.Player;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import whitefoxdev.ftbd.abstracts.ObjectToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Players")
public class Player extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String uuid;

    private int level;

    private int experience;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Quest> quests = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Role> roles = new ArrayList<>();

    //==================================================================================================================
    //CONSTRUCTORS

    private Player() {}

    public Player(String uuid, int level, int experience, List<Skill> skills, List<Quest> quests, List<Role> roles) {
        this();
        this.uuid = uuid;
        this.level = level;
        this.experience = experience;
        this.skills = skills;
        this.quests = quests;
        this.roles = roles;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void setQuests(List<Quest> quests) {
        this.quests = quests;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
