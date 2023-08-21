package whitefoxdev.ftbd.managers;

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.List;

public class Manager {
    private HibernateManager hibernateManager;
    private PlayerManager playerManager;
    private PatternQuestManager patternQuestManager;
    private PatternSkillManager patternSkillManager;
    private PatternRoleManager patternRoleManager;

    public Manager(String hibernateCfgXmlPath) {
        hibernateManager = new HibernateManager(hibernateCfgXmlPath);
        playerManager = new PlayerManager(hibernateManager);
        patternQuestManager = new PatternQuestManager(hibernateManager);
        patternSkillManager = new PatternSkillManager(hibernateManager);
        patternRoleManager = new PatternRoleManager(hibernateManager);
    }

    public void init() {
        hibernateManager.init();
    }

    public void close() {
        hibernateManager.close();
    }


    public HibernateManager getHibernateManager(){
        return hibernateManager;
    }
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PatternQuestManager getPatternQuestManager() {
        return patternQuestManager;
    }

    public PatternSkillManager getPatternSkillManager() {
        return patternSkillManager;
    }

    public PatternRoleManager getPatternRoleManager() {
        return patternRoleManager;
    }

    /**
     * Возвращает объект Player из json файла.
     */
    public Player getPlayerFromJson(File file, String uuid) {
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonPlayerLoader jsonLoader = gson.fromJson(fileReader, JsonPlayerLoader.class);
            List<Skill> skills = new ArrayList<>();
            List<Quest> quests = new ArrayList<>();
            List<Role> roles = new ArrayList<>();
            for (int id : jsonLoader.patternQuestsId) {
                quests.add(patternQuestManager.get(id).createEmptyQuest());
            }
            for (int id : jsonLoader.patternSkillsId) {
                skills.add(patternSkillManager.get(id).createEmptySkill());
            }
            for (int id : jsonLoader.patternRolesId) {
                roles.add(patternRoleManager.get(id).createEmptyRole());
            }
            return new Player(uuid, jsonLoader.level, jsonLoader.experience, skills, quests, roles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private class JsonPlayerLoader{
        public int level;
        public int experience;
        public List<Integer> patternSkillsId;
        public List<Integer> patternQuestsId;
        public List<Integer> patternRolesId;
    }


    /**
     * Загружает объекты PatternQuest из json файла в базу данных.
     */
    public void addPatternQuestsFromJson(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonPatternQuestsLoader jsonLoader = gson.fromJson(fileReader, JsonPatternQuestsLoader.class);
            HashMap<Integer, JsonPatternQuestsLoader.Element> elements = jsonLoader.elements;
            List<PatternQuest> patternQuests = new ArrayList<>();
            for (JsonPatternQuestsLoader.Element element : elements.values()) {
                PatternQuest patternQuest = new PatternQuest(element.title, element.subtitle, element.description, element.taskMap, element.patternTasks, null);
                patternQuests.add(patternQuest);
                patternQuestManager.add(patternQuest);
            }
            List<Integer> keys = new ArrayList<>(elements.keySet());
            List<JsonPatternQuestsLoader.Element> values = new ArrayList<>(elements.values());
            for (int i = 0; i < elements.size(); i++) {
                for (int id : values.get(i).nextPatternQuests) {
                    int index = keys.indexOf(id);
                    if (index == -1) {
                        PatternQuest patternQuest = patternQuestManager.get(id);
                        if (patternQuest != null) {
                            patternQuests.get(i).getNextPatternQuests().add(patternQuest);
                        }
                    } else {
                        patternQuests.get(i).getNextPatternQuests().add(patternQuests.get(index));
                    }
                }
                patternQuestManager.add(patternQuests.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class JsonPatternQuestsLoader {
        private class Element {
            public String title;
            public String subtitle;
            public String description;
            public String taskMap;
            public List<PatternTask> patternTasks;
            public List<Integer> nextPatternQuests;
        }
        public HashMap<Integer, Element> elements;
    }

    /**
     * Загружает объекты PatternSkill из json файла в базу данных.
     */
    public void addPatternSkillFromJson(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonPatternSkillsLoader jsonLoader = gson.fromJson(fileReader, JsonPatternSkillsLoader.class);
            List<JsonPatternSkillsLoader.Element> elements = jsonLoader.elements;
            for (JsonPatternSkillsLoader.Element element : elements) {
                PatternSkill patternSkill = new PatternSkill(element.name, element.maxLevel);
                patternSkillManager.add(patternSkill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class JsonPatternSkillsLoader {
        private class Element {
            public String name;
            public int maxLevel;
        }

        public List<JsonPatternSkillsLoader.Element> elements;
    }

    /**
     * Загружает объекты PatternRole из json файла в базу данных.
     */
    public void addPatternRoleFromJson(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonPatternRolesLoader jsonLoader = gson.fromJson(fileReader, JsonPatternRolesLoader.class);
            List<JsonPatternRolesLoader.Element> elements = jsonLoader.elements;
            for (JsonPatternRolesLoader.Element element : elements) {
                PatternRole patternRole = new PatternRole(element.name, element.rights);
                patternRoleManager.add(patternRole);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class JsonPatternRolesLoader {
        private class Element {
            public String name;
            public String rights;
        }

        public List<Element> elements;
    }
}
