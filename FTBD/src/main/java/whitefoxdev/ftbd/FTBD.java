package whitefoxdev.ftbd;

import org.bukkit.plugin.java.JavaPlugin;
import whitefoxdev.ftbd.managers.PatternQuests;
import whitefoxdev.ftbd.managers.PatternRoles;
import whitefoxdev.ftbd.managers.PatternSkills;

import java.io.File;

public final class FTBD extends JavaPlugin {

    @Override
    public void onEnable() {
        init();
        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
