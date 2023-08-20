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

    /**
     * Добавляет или обновляет данные игрока в базе данных.
     */
    public static boolean add(Player player) {
        //В целях безопасности данных игроков функция замены данных по уникальному полю uuid не добавлена
        if (!HibernateManager.addObject(player)) {
            return false;
        }
        playerList.put(player.getUuid(), player);
        return true;
    }

    /**
     * Возвращает данные игрока из базы данных. Создает, если данных игрока не существовало.
     */
    public static Player get(String uuid) {
        Player player = playerList.get(uuid);
        if (player != null) {
            return player;
        }
        player = (Player) HibernateManager.getObject(Player.class, "uuid", uuid);
        if (player == null) {
            player = getPatternPlayerFromJson(new File("src/main/resources/jsonTables/PatternPlayer.json"), uuid);
            if (!add(player)) {
                return null;
            }
        }
        return player;
    }

    /**
     * Обновляет данные игрока в базе данных и удаляет эти данные из листа и сессии Hibernate.
     */
    public static boolean remove(String uuid) {
        Player player = playerList.get(uuid);
        if (player == null) {
            return false;
        }
        if (!HibernateManager.addObject(player)) {
            return false;
        }
        HibernateManager.evict(player);
        playerList.remove(uuid);
        return true;
    }

    /**
     * Удаляет все данные из листа и сессии Hibernate.
     */
    public static void clear() {
        for (Player player : playerList.values()) {
            HibernateManager.evict(player);
        }
        playerList.clear();
    }

    /**
     * Возвращает количество объектов выгруженных с базы данных в лист.
     */
    public static int getCount() {
        return playerList.size();
    }

    /**
     * Создает объект Player из json файла.
     */
    public static Player getPatternPlayerFromJson(File file, String uuid) {
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonLoader jsonLoader = gson.fromJson(fileReader, JsonLoader.class);
            List<Skill> skills = new ArrayList<>();
            List<Quest> quests = new ArrayList<>();
            List<Role> roles = new ArrayList<>();
            for (int id : jsonLoader.patternSkillsId) {
                skills.add(PatternSkills.get(id).createEmptySkill());
            }
            for (int id : jsonLoader.patternQuestsId) {
                quests.add(PatternQuests.get(id).createEmptyQuest());
            }
            for (int id : jsonLoader.patternRolesId) {
                roles.add(PatternRoles.get(id).createEmptyRole());
            }
            return new Player(uuid, jsonLoader.level, jsonLoader.experience, skills, quests, roles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class JsonLoader {
        public int level;
        public int experience;
        public List<Integer> patternSkillsId;
        public List<Integer> patternQuestsId;
        public List<Integer> patternRolesId;
    }

}
