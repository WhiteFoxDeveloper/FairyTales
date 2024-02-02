package whitefoxdev.ftbd.tables;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import whitefoxdev.ftbd.abstracts.ObjectToString;
import whitefoxdev.ftbd.tables.Player.Quest;
import whitefoxdev.ftbd.tables.Player.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patternQuests")
public class PatternQuest extends ObjectToString {
    //==================================================================================================================
    //FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String title;

    private String subtitle;

    private String description;

    private String taskMap;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<PatternTask> patternTasks = new ArrayList<>();

    //возможно потребуется LAZY во избежание от дубликации объектов в ОЗУ
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<PatternQuest> nextPatternQuests = new ArrayList<>();

    //==================================================================================================================
    //CONSTRUCTORS
    private PatternQuest() {
    }

    public PatternQuest(String title, String subtitle, String description, String taskMap, List<PatternTask> patternTasks, List<PatternQuest> nextPatternQuests) {
        this();
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.taskMap = taskMap;
        this.patternTasks = patternTasks == null ? this.patternTasks : patternTasks;
        this.nextPatternQuests = nextPatternQuests == null ? this.nextPatternQuests : nextPatternQuests;
    }

    //==================================================================================================================
    //GETTERS_AND_SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(String taskMap) {
        this.taskMap = taskMap;
    }

    public List<PatternQuest> getNextPatternQuests() {
        return nextPatternQuests;
    }

    public void setNextPatternQuests(List<PatternQuest> list) {
        this.nextPatternQuests = list;
    }

    public List<PatternTask> getPatternTasks() {
        return patternTasks;
    }

    public void setPatternTasks(List<PatternTask> list) {
        this.patternTasks = list;
    }

    //==================================================================================================================
    //FUNCTIONS
    public Quest createEmptyQuest() {
        List<Task> tasks = new ArrayList<>();
        for (PatternTask patternTask : this.patternTasks) {
            tasks.add(patternTask.createEmptyTask());
        }
        return new Quest(this, Quest.Status.UNCOMPLETED, tasks);
    }
}
