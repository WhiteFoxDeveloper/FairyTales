package whitefoxdev.ftbd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import whitefoxdev.ftbd.managers.Players;

public class Listeners implements Listener {

    private static Logger logger = LogManager.getLogger(Listeners.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Load playerData from database, if playerData is not exist, then create new playerData
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();

        logger.info("Player \"" + name + "\" join");
        if(Players.get(uuid) == null){
            player.kickPlayer("The database cannot create your entity");
            logger.error("The database does not create a \"Player\" object");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Unload playerData in databaseA
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();

        logger.info("Player \"" + name + "\" quit");
        Players.remove(uuid);
    }


}
