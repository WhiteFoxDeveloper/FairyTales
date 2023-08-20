package whitefoxdev.ftbd.managers;

import com.google.gson.Gson;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.tables.PatternQuest;
import whitefoxdev.ftbd.tables.PatternSkill;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class PatternSkills {

    private static HashMap<Integer, PatternSkill> patternSkillList = new HashMap<>();


    /**
     * Выгружает все объекты из базы данных в лист.
     */
    public static void loadAll(){
        List<Object> objects = HibernateManager.getAllObjects(PatternSkill.class);
        if(objects == null){
            return;
        }
        for(Object object : objects){
            PatternSkill patternSkill = (PatternSkill) object;
            patternSkillList.put(patternSkill.getId(), patternSkill);
        }
    }

    /**
     * Добавляет объект в лист и базу данных.
     */
    public static boolean add(PatternSkill patternSkill){
        if(patternSkill.getId() == 0) {
            PatternSkill patternSkillOld = (PatternSkill) HibernateManager.getObject(PatternSkill.class, "name", patternSkill.getName());
            if (patternSkillOld != null) {
                patternSkill.setId(patternSkillOld.getId());
                HibernateManager.evict(patternSkillOld);
            }
        }
        if(!HibernateManager.addObject(patternSkill)) {
            return false;
        }
        patternSkillList.put(patternSkill.getId(), patternSkill);
        return true;
    }

    /**
     * Возвращает объект по айди из базы данных.
     */
    public static PatternSkill get(int id){
        PatternSkill patternSkill = patternSkillList.get(id);
        if(patternSkill == null){
            patternSkill = (PatternSkill) HibernateManager.getObject(PatternSkill.class, "id", id);
            if(patternSkill == null){
                patternSkillList.put(patternSkill.getId(), patternSkill);
            }
        }
        return patternSkill;
    }

    /**
     * Удаляет объект из листа и базы данных.
     */
    public static boolean remove(int id){
        if(!HibernateManager.removeObject(patternSkillList.get(id))){
            return false;
        }
        patternSkillList.remove(id);
        return true;
    }

    /**
     * Удаляет все данные из листа и сессии Hibernate.
     */
    public static void clear(){
        for(PatternSkill patternSkill : patternSkillList.values()){
            HibernateManager.evict(patternSkill);
        }
        patternSkillList.clear();
    }

    /**
     * Возвращает количество объектов выгруженных с базы данных в лист.
     */
    public static int getCount(){
        return patternSkillList.size();
    }

    /**
     * Выгружает все объекты из json файла в базу данных.
     */
    public static void addViaJson(File file){
        try(FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonLoader jsonLoader = gson.fromJson(fileReader, JsonLoader.class);
            List<JsonLoader.Element> elements = jsonLoader.elements;
            for (JsonLoader.Element element : elements) {
                PatternSkill patternSkill = new PatternSkill(element.name, element.maxLevel);
                add(patternSkill);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class JsonLoader{
        private class Element{
            public String name;
            public int maxLevel;
        }
        public List<JsonLoader.Element> elements;
    }
}
