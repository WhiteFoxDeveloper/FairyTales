package whitefoxdev.ftbd.managers;

import org.hibernate.exception.ConstraintViolationException;
import whitefoxdev.ftbd.tables.PatternQuest;

import java.util.List;

public class PatternQuestManager {
    private HibernateManager hibernateManager;

    public PatternQuestManager(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    //==================================================================================================================
    //FUNCTIONS

    public boolean add(PatternQuest patternQuest) {
        try {
            hibernateManager.addObject(patternQuest);
            return true;
        } catch (ConstraintViolationException e) {
            return addWithConstraintViolation(patternQuest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public PatternQuest get(int id) {
        try {
            return get("id", id);
        } catch (Exception e) {

        }
        return null;
    }

    public List<PatternQuest> getAll(){
        return hibernateManager.getAllObjects(PatternQuest.class);
    }

    public void evict(PatternQuest patternQuest) {
        hibernateManager.evict(patternQuest);
    }

    //==================================================================================================================
    //PRIVATE_FUNCTIONS

    private boolean addWithConstraintViolation(PatternQuest patternQuest) {
        try {
            PatternQuest oldPatternQuest = get("title", patternQuest.getTitle());
            patternQuest.setId(oldPatternQuest.getId());
            hibernateManager.evict(oldPatternQuest);
            return add(patternQuest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private PatternQuest get(String fieldName, Object value) {
        try {
            return hibernateManager.getObject(PatternQuest.class, fieldName, value);
        }catch (Exception e){
            throw e;
        }
    }
}
