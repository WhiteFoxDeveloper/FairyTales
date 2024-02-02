package whitefoxdev.ftbd.managers;

import org.hibernate.exception.ConstraintViolationException;
import whitefoxdev.ftbd.tables.PatternRole;

public class PatternRoleManager {
    private HibernateManager hibernateManager;

    public PatternRoleManager(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    //==================================================================================================================
    //FUNCTIONS
    public boolean add(PatternRole patternRole) {
        try {
            hibernateManager.addObject(patternRole);
            return true;
        } catch (ConstraintViolationException e) {
            addWithConstraintViolation(patternRole);
        } catch (Exception e) {

        }
        return false;
    }

    public PatternRole get(int id) {
        try {
            return (PatternRole) hibernateManager.getObject(PatternRole.class, "id", id);
        } catch (Exception e) {

        }
        return null;
    }

    public void evict(PatternRole patternRole) {
        hibernateManager.evict(patternRole);
    }

    //==================================================================================================================
    //PRIVATE_FUNCTIONS

    private void addWithConstraintViolation(PatternRole patternRole) {
        try {
            PatternRole oldPatternRole = get("name", patternRole.getName());
            patternRole.setId(oldPatternRole.getId());
            hibernateManager.evict(oldPatternRole);
            add(patternRole);
        } catch (Exception e) {
            throw e;
        }
    }

    private PatternRole get(String fieldName, Object value) {
        try {
            return hibernateManager.getObject(PatternRole.class, fieldName, value);
        } catch (Exception e) {
            throw e;
        }
    }
}
