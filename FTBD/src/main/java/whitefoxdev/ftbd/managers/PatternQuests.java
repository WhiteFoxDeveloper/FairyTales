package whitefoxdev.ftbd.managers;

import com.google.gson.Gson;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.tables.PatternQuest;
import whitefoxdev.ftbd.tables.PatternTask;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class PatternQuests {
    private static HashMap<Integer, PatternQuest> patternQuestList = new HashMap<>();

    /**
     * Выгружает все объекты из базы данных в лист.
     */
    public static boolean loadAll() {
        List<Object> objects = HibernateManager.getAllObjects(PatternQuest.class);
        if (objects == null) {
            return false;
        }
        for (Object object : objects) {
            PatternQuest patternQuest = (PatternQuest) object;
            patternQuestList.put(patternQuest.getId(), patternQuest);
        }
        return true;
    }

    /**
     * Добавляет объект в лист и базу данных.
     */
    public static boolean add(PatternQuest patternQuest) {
        if (patternQuest.getId() == 0) {
            PatternQuest patternQuestOld = (PatternQuest) HibernateManager.getObject(PatternQuest.class, "title", patternQuest.getTitle());
            if (patternQuestOld != null) {
                patternQuest.setId(patternQuestOld.getId());
                HibernateManager.evict(patternQuestOld);
            }
        }
        if (!HibernateManager.addObject(patternQuest)) {
            return false;
        }
        patternQuestList.put(patternQuest.getId(), patternQuest);
        return true;
    }

    /**
     * Возвращает объект по айди из базы данных.
     */
    public static PatternQuest get(int id) {
        PatternQuest patternQuest = patternQuestList.get(id);
        if (patternQuest == null) {
            patternQuest = (PatternQuest) HibernateManager.getObject(PatternQuest.class, "id", id);
            if (patternQuest != null) {
                patternQuestList.put(patternQuest.getId(), patternQuest);
            }
        }
        return patternQuest;
    }

    /**
     * Удаляет объект из листа и базы данных.
     */
    public static boolean remove(int id) {
        if (HibernateManager.removeObject(patternQuestList.get(id))) {
            return false;
        }
        patternQuestList.remove(id);
        return true;
    }

    /**
     * Удаляет все данные из листа и сессии Hibernate.
     */
    public static void clear(){
        for(PatternQuest patternQuest : patternQuestList.values()){
            HibernateManager.evict(patternQuest);
        }
        patternQuestList.clear();
    }

    /**
     * Возвращает количество объектов выгруженных с базы данных в лист.
     */
    public static int getCount() {
        return patternQuestList.size();
    }

    /**
     * Выгружает все объекты из json файла в базу данных.
     */
    public static void addViaJson(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonLoader jsonLoader = gson.fromJson(fileReader, JsonLoader.class);
            HashMap<Integer, JsonLoader.Element> elements = jsonLoader.elements;
            List<PatternQuest> patternQuests = new ArrayList<>();
            for (JsonLoader.Element element : elements.values()) {
                PatternQuest patternQuest = new PatternQuest(element.title, element.subtitle, element.description, element.taskMap, element.patternTasks, null);
                patternQuests.add(patternQuest);
                add(patternQuest);
            }
            List<Integer> keys = new ArrayList<>(elements.keySet());
            List<JsonLoader.Element> values = new ArrayList<>(elements.values());
            for (int i = 0; i < elements.size(); i++) {
                for (int id : values.get(i).nextPatternQuests) {
                    int index = keys.indexOf(id);
                    if (index == -1) {
                        PatternQuest patternQuest = get(id);
                        if (patternQuest != null) {
                            patternQuests.get(i).getNextPatternQuests().add(patternQuest);
                        }
                    } else {
                        patternQuests.get(i).getNextPatternQuests().add(patternQuests.get(index));
                    }
                }
                add(patternQuests.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JsonLoader {
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
}
