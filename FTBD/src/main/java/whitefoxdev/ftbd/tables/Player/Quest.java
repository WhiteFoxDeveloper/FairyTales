package whitefoxdev.ftbd.tables.Player;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.PatternQuest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Quests")
public class Quest extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private PatternQuest patternQuest;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Task> tasks = new ArrayList<>();

     //==================================================================================================================
     //ENUMS
    public enum Status {
        COMPLETED,
        UNCOMPLETED,
        UNAVAILABLE
    }

    //==================================================================================================================
    //CONSTRUCTORS

    private Quest() {
    }

    public Quest(PatternQuest patternQuest, Status status, List<Task> tasks) {
        this();
        this.patternQuest = patternQuest;
        this.status = status;
        this.tasks = tasks;
    }


    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PatternQuest getPatternQuest() {
        return patternQuest;
    }

    public void setPatternQuest(PatternQuest patternQuest) {
        this.patternQuest = patternQuest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}

