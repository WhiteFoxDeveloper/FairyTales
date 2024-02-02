
import org.junit.Before;
import org.junit.Test;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.Loader;
import whitefoxdev.ftbd.managers.PatternQuests;
import whitefoxdev.ftbd.managers.PatternRoles;
import whitefoxdev.ftbd.managers.PatternSkills;
import whitefoxdev.ftbd.managers.Players;
import whitefoxdev.ftbd.tables.PatternQuest;
import whitefoxdev.ftbd.tables.Player.Player;
import whitefoxdev.ftbd.tables.Player.Quest;

import java.io.File;
import java.util.ArrayList;

public class Tests{

    final String UUID = "5f59c63a-f9a8-4d2c-b5b7-6e884353test";

    @Before
    public void setUp() {
        Tools.init();
    }

    @Test
    public void testPlayerGet() {
        Player player1, player2;
        HibernateManager.init();

        player1 = Players.get(UUID);
        Players.remove(player1.getUuid());
        player2 = Players.get(player1.getUuid());
        assert player1.toString().equals(player2.toString());

        Players.remove(player2.getUuid());
        HibernateManager.removeObject(player2);
    }

    @Test
    public void testPlayerGetWithReloadHibernate() {
        Player player1, player2;
        HibernateManager.init();

        player1 = Players.get(UUID);
        HibernateManager.close();
        HibernateManager.init();
        player2 = Players.get(UUID);
        assert player1.toString().equals(player2.toString());

        Players.remove(player2.getUuid());
        HibernateManager.removeObject(player2);
    }


    @Test
    public void testPlayerEditPropertiesWithoutDeletePatterns() {
        Player player;
        PatternQuest patternQuest1, patternQuest2;
        HibernateManager.init();

        player = Players.get(UUID);
        patternQuest1 = player.getQuests().get(0).getPatternQuest();
        player.setQuests(new ArrayList<>());
        Players.add(player);
        HibernateManager.close();
        HibernateManager.init();
        patternQuest2 = (PatternQuest) HibernateManager.getObject(PatternQuest.class, "id", patternQuest1.getId());
        assert patternQuest1.toString().equals(patternQuest2.toString());

        Players.remove(player.getUuid());
        HibernateManager.removeObject(player);
    }

    @Test
    public void testPlayerDeleteWithoutDeletePatterns() {
        Player player;
        PatternQuest patternQuest1, patternQuest2;
        HibernateManager.init();

        player = Players.get(UUID);
        patternQuest1 = player.getQuests().get(0).getPatternQuest();
        Players.remove(player.getUuid());
        HibernateManager.removeObject(player);
        patternQuest2 = (PatternQuest) HibernateManager.getObject(PatternQuest.class, "id", patternQuest1.getId());
        assert patternQuest1.toString().equals(patternQuest2.toString());

        Players.remove(player.getUuid());
        HibernateManager.removeObject(player);
    }

    @Test
    public void testDublicatePatterns(){
        PatternQuest patternQuest1, patternQuest2;
        HibernateManager.init();
        PatternQuests.loadAll();

        patternQuest1 = PatternQuests.get(1);
        patternQuest1 = patternQuest1.getNextPatternQuests().get(0);
        patternQuest2 = PatternQuests.get(patternQuest1.getId());
        assert patternQuest1 == patternQuest2;
    }
}
