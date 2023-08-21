package whitefoxdev.ftbd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import whitefoxdev.ftbd.managers.PlayerManager;

import java.io.File;

public class Listeners implements Listener {

    private static Logger logger = LogManager.getLogger(Listeners.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();
        PlayerManager playerManager = FTBD.getManager().getPlayerManager();

        logger.info("Player \"" + name + "\" join");
        whitefoxdev.ftbd.tables.Player.Player playerData = playerManager.get(uuid);
        if(playerData != null){
            return;
        }
        logger.info("Player \"" + name + "\" has no data in the database");
        playerData = FTBD.getManager().getPlayerFromJson(new File("src/main/resources/jsonTables/PatternPlayer.json"), uuid);
        if(!playerManager.add(playerData)) {
            player.kickPlayer("The database cannot create your entity");
            logger.error("The database does not create a \"Player\" object");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();
        PlayerManager playerManager = FTBD.getManager().getPlayerManager();

        logger.info("Player \"" + name + "\" quit");
        whitefoxdev.ftbd.tables.Player.Player playerData = playerManager.get(uuid);
        if (playerData != null) {
            playerManager.add(playerData);
            playerManager.evict(playerData);
        }
    }
}
