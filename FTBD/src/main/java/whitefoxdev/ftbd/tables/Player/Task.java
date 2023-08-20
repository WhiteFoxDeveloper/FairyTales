package whitefoxdev.ftbd.tables.Player;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.PatternTask;

import javax.persistence.*;

@Entity
@Table(name = "Tasks")
public class Task extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private PatternTask patternTask;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int progress;

    //==================================================================================================================
    //ENUMS

    public enum Status {
        COMPLETED, UNCOMPLETED, UNAVAILABLE
    }

    //==================================================================================================================
    //CONSTRUCTORS

    private Task() {
    }

    public Task(PatternTask patternTask, Status status, int progress) {
        this();
        this.patternTask = patternTask;
        this.status = status;
        this.progress = progress;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public PatternTask getPatternTask() {
        return patternTask;
    }

    public void setPatternTask(PatternTask patternTask) {
        this.patternTask = patternTask;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
