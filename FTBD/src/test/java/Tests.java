
import org.junit.Before;
import org.junit.Test;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.managers.PatternQuests;
import whitefoxdev.ftbd.managers.PatternRoles;
import whitefoxdev.ftbd.managers.PatternSkills;
import whitefoxdev.ftbd.managers.Players;
import whitefoxdev.ftbd.tables.Player.Player;

import java.io.File;

public class Tests {

//    public static void main(String[] args) {
//        setUp();
//    }

    @Before
    public void setUp() {
        HibernateManager.init();
        loadJsonPatterns("src/main/resources/jsonTables/");
        loadAllPatterns();
    }

    @Test
    public void testHibernateDuplicates(){
        Player player1 = Players.getPatternPlayerWithJson(new File("src/main/resources/jsonTables/PatternPlayer.json"), "5f59c63a-f9a8-4d2c-b5b7-6e8843ab6847");
        System.out.println(player1);
        Player player2;
        Players.add(player1);
        Players.remove(player1.getUuid());
        player2 = Players.get(player1.getUuid());
        System.out.println(player2);
        assert player1 == player2;
    }

    public static void printCountPatterns() {
        System.out.println("Count PatternRoles: " + PatternRoles.getCount() +
                "\nCount PatternSkills: " + PatternSkills.getCount() +
                "\nCount PatternQuests: " + PatternQuests.getCount());
    }

    public static void loadJsonPatterns(String path) {
        PatternSkills.addViaJson(new File(path + "PatternSkills.json"));
        PatternQuests.addViaJson(new File(path + "PatternQuests.json"));
        PatternRoles.addViaJson(new File(path + "PatternRoles.json"));
    }

    public static void loadAllPatterns() {
        PatternSkills.loadAll();
        PatternRoles.loadAll();
        PatternQuests.loadAll();
    }

}
