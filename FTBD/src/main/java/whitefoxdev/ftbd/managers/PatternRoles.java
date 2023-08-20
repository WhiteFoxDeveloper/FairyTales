package whitefoxdev.ftbd.managers;

import com.google.gson.Gson;
import whitefoxdev.ftbd.HibernateManager;
import whitefoxdev.ftbd.tables.PatternQuest;
import whitefoxdev.ftbd.tables.PatternRole;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class PatternRoles {

    private static HashMap<Integer, PatternRole> patternRoleList = new HashMap<>();

    /**
     * Выгружает все объекты из базы данных в лист.
     */
    public static void loadAll(){
        List<Object> objects = HibernateManager.getAllObjects(PatternRole.class);
        if(objects == null){
            return;
        }
        for(Object object : objects){
            PatternRole patternRole = (PatternRole) object;
            patternRoleList.put(patternRole.getId(), patternRole);
        }
    }

    /**
     * Добавляет объект в лист и базу данных.
     */
    public static boolean add(PatternRole patternRole){
        if(patternRole.getId() == 0) {
            PatternRole patternRoleOld = (PatternRole) HibernateManager.getObject(PatternRole.class, "name", patternRole.getName());
            if (patternRoleOld != null) {
                patternRole.setId(patternRoleOld.getId());
                HibernateManager.evict(patternRoleOld);
            }
        }
        if(!HibernateManager.addObject(patternRole)) {
            return false;
        }
        patternRoleList.put(patternRole.getId(), patternRole);
        return true;
    }

    /**
     * Возвращает объект по айди из базы данных.
     */
    public static PatternRole get(int id){
        PatternRole patternRole = patternRoleList.get(id);
        if(patternRole == null){
            patternRole = (PatternRole) HibernateManager.getObject(PatternRole.class, "id", id);
            if(patternRole == null){
                patternRoleList.put(patternRole.getId(), patternRole);
            }
        }
        return patternRole;
    }

    /**
     * Удаляет объект из листа и базы данных.
     */
    public static boolean remove(int id){
        if(!HibernateManager.removeObject(patternRoleList.get(id))){
            return false;
        }
        patternRoleList.remove(id);
        return true;
    }

    /**
     * Удаляет все данные из листа и сессии Hibernate.
     */
    public static void clear(){
        for(PatternRole patternRole : patternRoleList.values()){
            HibernateManager.evict(patternRole);
        }
        patternRoleList.clear();
    }

    /**
     * Возвращает количество объектов выгруженных с базы данных в лист.
     */
    public static int getCount(){
        return patternRoleList.size();
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
                PatternRole patternRole = new PatternRole(element.name, element.rights);
                add(patternRole);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class JsonLoader{
        private class Element{
            public String name;
            public String rights;
        }
        public List<Element> elements;
    }
}
