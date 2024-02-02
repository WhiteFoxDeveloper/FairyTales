package whitefoxdev.ftbd.managers;

import org.hibernate.exception.ConstraintViolationException;
import whitefoxdev.ftbd.tables.PatternSkill;

public class PatternSkillManager {
    private HibernateManager hibernateManager;

    public PatternSkillManager(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    //==================================================================================================================
    //FUNCTIONS

    public boolean add(PatternSkill patternSkill) {
        try {
            hibernateManager.addObject(patternSkill);
            return true;
        } catch (ConstraintViolationException e) {
            addWithConstraintViolation(patternSkill);
        } catch (Exception e) {

        }
        return false;
    }

    public PatternSkill get(int id) {
        try {
            return get("id", id);
        }catch (Exception e){

        }
        return null;
    }

    public void evict(PatternSkill patternSkill) {
        hibernateManager.evict(patternSkill);
    }

    //==================================================================================================================
    //PRIVATE_FUNCTIONS

    private void addWithConstraintViolation(PatternSkill patternSkill) {
        try {
            PatternSkill oldPatternSkill = get("name", patternSkill.getName());
            patternSkill.setId(oldPatternSkill.getId());
            hibernateManager.evict(oldPatternSkill);
            add(patternSkill);
        } catch (Exception e) {
            throw e;
        }
    }

    private PatternSkill get(String fieldName, Object value) {
        try {
            return hibernateManager.getObject(PatternSkill.class, fieldName, value);
        } catch (Exception e) {
            throw e;
        }
    }
}
