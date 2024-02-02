import org.junit.Before;
import org.junit.Test;
import whitefoxdev.ftbd.managers.Manager;
import whitefoxdev.ftbd.tables.PatternQuest;
import whitefoxdev.ftbd.tables.Player.Player;

import java.io.File;


public class Tests {
    private final String UUID = "5f59c63a-f9a8-4d2c-b5b7-6e884353test";
    private Manager manager;

    @Before
    public void setUp() {
        manager = new Manager("hibernate.cfg.xml");
        manager.init();
        manager.getHibernateManager().removeAllObjectsFromTable(Player.class);
        manager.addPatternQuestsFromJson(new File("src/main/resources/jsonTables/PatternQuests.json"));
        manager.addPatternSkillFromJson(new File("src/main/resources/jsonTables/PatternSkills.json"));
        manager.addPatternRoleFromJson(new File("src/main/resources/jsonTables/PatternRoles.json"));
    }

    @Test
    public void testAddPatternsFromJson() {
        manager.addPatternQuestsFromJson(new File("src/main/resources/jsonTables/PatternQuests.json"));
        manager.addPatternSkillFromJson(new File("src/main/resources/jsonTables/PatternSkills.json"));
        manager.addPatternRoleFromJson(new File("src/main/resources/jsonTables/PatternRoles.json"));
    }

    @Test
    public void testDuplicateObject(){
        PatternQuest patternQuest1 = manager.getPatternQuestManager().getAll().get(0);
        PatternQuest patternQuest2 = manager.getPatternQuestManager().get(patternQuest1.getId());
        assert patternQuest1 == patternQuest2;

        patternQuest1.setSubtitle("test");
        manager.getPatternQuestManager().add(patternQuest1);
        patternQuest2 = manager.getPatternQuestManager().get(patternQuest1.getId());
        assert patternQuest1 == patternQuest2;
    }

    @Test
    public void testAddViolatingUniquePattern() {
        PatternQuest patternQuest1 = manager.getPatternQuestManager().getAll().get(0);
        PatternQuest patternQuest2 = new PatternQuest(
                patternQuest1.getTitle(),
                patternQuest1.getSubtitle(),
                patternQuest1.getDescription(),
                patternQuest1.getTaskMap(),
                patternQuest1.getPatternTasks(),
                patternQuest1.getNextPatternQuests());
        manager.getPatternQuestManager().add(patternQuest2);
        patternQuest1 = manager.getPatternQuestManager().get(patternQuest2.getId());
        assert patternQuest1 == patternQuest2;
    }

    @Test
    public void addAndGetPlayer() {
        Player player1 = manager.getPlayerFromJson(new File("src/main/resources/jsonTables/PatternPlayer.json"), UUID);
        assert manager.getPlayerManager().add(player1);

        Player player2 = manager.getPlayerManager().get(player1.getUuid());
        assert player1 == player2;
    }

}
