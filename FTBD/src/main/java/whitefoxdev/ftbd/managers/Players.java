package whitefoxdev.ftbd.managers;

import com.google.gson.Gson;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.tables.PatternQuest;
import whitefoxdev.ftbd.tables.PatternRole;
import whitefoxdev.ftbd.tables.PatternSkill;
import whitefoxdev.ftbd.tables.PatternTask;
import whitefoxdev.ftbd.tables.Player.Player;
import whitefoxdev.ftbd.tables.Player.Quest;
import whitefoxdev.ftbd.tables.Player.Role;
import whitefoxdev.ftbd.tables.Player.Skill;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Players {
    private static HashMap<String, Player> playerList = new HashMap<>();

    public static boolean add(Player player){
        if (!HibernateManager.addObject(player)) {
            return false;
        }
        playerList.put(player.getUuid(), player);
        return true;
    }

    public static Player get(String uuid) {
        Player player = playerList.get(uuid);
        if(player != null){
            return player;
        }
        player = (Player) HibernateManager.getObject("Player", "uuid", uuid);
        if (player == null) {
            player = getPatternPlayerWithJson(new File("src/main/resources/jsonTables/PatternPlayer.json"), uuid);
            if(!add(player)){
                return null;
            }
        }
        return player;
    }

    public static boolean remove(String uuid){
        Player player = playerList.get(uuid);
        if(player == null){
            return false;
        }
        HibernateManager.addObject(player);
        playerList.remove(uuid);
        return true;
    }

    public static int getCount(){
        return playerList.size();
    }

    public static Player getPatternPlayerWithJson(File file, String uuid){
        try(FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonLoader jsonLoader = gson.fromJson(fileReader, JsonLoader.class);
            List<Skill> skills = new ArrayList<>();
            List<Quest> quests = new ArrayList<>();
            List<Role> roles = new ArrayList<>();
            for(int id : jsonLoader.patternSkillsId){
                skills.add(new Skill(PatternSkills.get(id), 0, 0));
            }
            for(int id : jsonLoader.patternQuestsId){
                quests.add(PatternQuests.get(id).createEmptyQuest());
            }
            for(int id : jsonLoader.patternRolesId){
                roles.add(new Role(PatternRoles.get(id), new Date(), null));
            }
            return new Player(uuid, jsonLoader.level, jsonLoader.experience, skills, quests, roles);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private class JsonLoader{
        public int level;
        public int experience;
        public List<Integer> patternSkillsId;
        public List<Integer> patternQuestsId;
        public List<Integer> patternRolesId;
    }

}
