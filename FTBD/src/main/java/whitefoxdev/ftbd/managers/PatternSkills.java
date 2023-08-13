package whitefoxdev.ftbd.managers;

import com.google.gson.Gson;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.tables.PatternSkill;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class PatternSkills {

    private static HashMap<Integer, PatternSkill> patternSkillList = new HashMap<>();

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

    public static void add(PatternSkill patternSkill){
        HibernateManager.addObject(patternSkill);
        patternSkillList.put(patternSkill.getId(), patternSkill);
    }

    public static PatternSkill get(int id){
        return patternSkillList.get(id);
    }

    public static void remove(int id){
        HibernateManager.removeObject(patternSkillList.get(id));
        patternSkillList.remove(id);
    }

    public static int getCount(){
        return patternSkillList.size();
    }

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
