package whitefoxdev.ftbd.managers;

import whitefoxdev.ftbd.tables.Player.Player;

public class PlayerManager {
    private HibernateManager hibernateManager;

    public PlayerManager(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    //==================================================================================================================
    //FUNCTIONS
    public boolean add(Player player) {
        try {
            hibernateManager.addObject(player);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Player get(String uuid) {
        try {
            return get("uuid", uuid);
        }catch (Exception e){

        }
        return null;
    }

    public void evict(Player player){
        hibernateManager.evict(player);
    }

    //==================================================================================================================
    //PRIVATE_FUNCTIONS
    private Player get(String fieldName, Object value) {
        try {
            return hibernateManager.getObject(Player.class, fieldName, value);
        } catch (Exception e) {
            throw e;
        }
    }
}
