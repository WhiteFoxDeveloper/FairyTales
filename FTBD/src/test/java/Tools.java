import com.google.gson.Gson;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.tables.*;
import whitefoxdev.ftbd.managers.PatternQuests;
import whitefoxdev.ftbd.managers.PatternRoles;
import whitefoxdev.ftbd.managers.PatternSkills;
import whitefoxdev.ftbd.tables.Player.*;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Tools {

    private static Random random = new Random();

    public static void printCountPatterns() {
        System.out.println("Count PatternRoles: " + PatternRoles.getCount() +
                "\nCount PatternSkills: " + PatternSkills.getCount() +
                "\nCount PatternQuests: " + PatternQuests.getCount());
    }

    public static void init(){
        HibernateManager.init();
        loadAllPatterns();
        loadJsonPatterns("src/main/resources/jsonTables/");
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
