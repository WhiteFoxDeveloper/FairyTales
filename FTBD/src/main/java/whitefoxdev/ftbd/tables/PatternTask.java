package whitefoxdev.ftbd.tables;

import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.Player.Task;

import javax.persistence.*;

@Entity
@Table(name = "patternTasks")
public class PatternTask extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String script;

    //==================================================================================================================
    //CONSTRUCTORS
    private PatternTask() {
    }

    public PatternTask(String script) {
        this();
        this.script = script;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    //==================================================================================================================
    //FUNCTIONS
    public Task createEmptyTask() {
        return new Task(this, Task.Status.UNCOMPLETED, 0);
    }
}
