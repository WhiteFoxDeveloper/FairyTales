package whitefoxdev.ftbd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import whitefoxdev.ftbd.managers.Manager;

public final class FTBD extends JavaPlugin {

    private static Logger logger = LogManager.getLogger(Listeners.class);
    private static Manager manager;

    @Override
    public void onEnable() {
        manager = new Manager("hibernate.cfg.xml");
        manager.init();
        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        manager.close();
    }

    public static Manager getManager(){
        return manager;
    }
}
