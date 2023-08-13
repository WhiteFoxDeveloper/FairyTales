import com.google.gson.Gson;
import whitefoxdev.ftbd.tables.*;
import whitefoxdev.ftbd.managers.PatternQuests;
import whitefoxdev.ftbd.managers.PatternRoles;
import whitefoxdev.ftbd.managers.PatternSkills;
import whitefoxdev.ftbd.tables.Player.*;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Tools {

    private static Random random = new Random();

    public static Player generatePlayer() {
        String uuid = UUID.randomUUID().toString();
        int level = random.nextInt(16);
        int experience = random.nextInt(16);
        List<Quest> quests = new ArrayList<>();
        List<Skill> skills = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < 1 + random.nextInt(4); i++) {
            quests.add(generateQuest());
        }
        for (int i = 0; i < 1 + random.nextInt(4); i++) {
            skills.add(generateSkill());
        }
        for (int i = 0; i < 1 + random.nextInt(4); i++) {
            roles.add(generateRole());
        }
        return new Player(uuid, level, experience, skills, quests, roles);
    }

    public static Quest generateQuest() {
        PatternQuest patternQuest = PatternQuests.get(random.nextInt(PatternQuests.getCount() - 1));
        List<PatternTask> patternTasks = patternQuest.getPatternTasks();
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < patternTasks.size(); i++) {
            tasks.add(new Task(patternTasks.get(i), Task.Status.COMPLETED, 0));
        }
        return new Quest(patternQuest, Quest.Status.COMPLETED, tasks);
    }

    public static Skill generateSkill() {
        PatternSkill patternSkill = PatternSkills.get(random.nextInt(PatternSkills.getCount() - 1));
        int level = random.nextInt(16);
        int experience = random.nextInt(16);
        return new Skill(patternSkill, level, experience);
    }

    public static Role generateRole() {
        PatternRole patternRole = PatternRoles.get(random.nextInt(PatternRoles.getCount() - 1));
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 16 * 10 * 5 + random.nextInt(16 * 10 * 8));
        return new Role(patternRole, date1, date2);
    }

    //==================================================================================================================

//    public static PatternQuest generatePatternQuest() {
//        String title = generateRandomText(4, 16);
//        String subtitle = generateRandomText(8, 32);
//        String description = generateRandomText(64, 1024);
//        String taskMap = generateRandomText(8, 32);
//        List<PatternTask> patternTasks = new ArrayList<>();
//        List<PatternQuest> nextPatternQuests = new ArrayList<>();
//        if(PatternTasks.getCount() > 0) {
//            for (int i = 0; i < (int) (Math.random() * 4); i++) {
//                patternTasks.add(PatternTasks.get(1 + (int) (Math.random() * PatternTasks.getCount())));
//            }
//        }
//        if(PatternQuests.getCount() > 0) {
//            for (int i = 0; i < (int) (Math.random() * 4); i++) {
//                nextPatternQuests.add(PatternQuests.get(1 + (int) (Math.random() * PatternQuests.getCount())));
//            }
//        }
//        return new PatternQuest(title, subtitle, description, taskMap, patternTasks, nextPatternQuests);
//    }
//
//    public static PatternTask generatePatternTask() {
//        String script = generateRandomText(16, 64);
//        int maxProgress = 1 + (int) (Math.random() * 11);
//        return new PatternTask(script);
//    }
//
//    public static PatternSkill generatePatternSkill() {
//        String name = generateRandomText(4, 16);
//        int maxLevel = (int) (Math.random() * 16);
//        return new PatternSkill(name, maxLevel);
//    }
//
//    public static PatternRole generatePatternRole() {
//        String name = generateRandomText(4, 16);
//        String right = generateRandomText(16, 32);
//        return new PatternRole(name, right);
//    }

    //==================================================================================================================

    public static String generateRandomText(int minCountChar, int maxCountChar) {
        if (minCountChar > maxCountChar) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i <= minCountChar + random.nextInt(maxCountChar - minCountChar); i++) {
            text.append((char) ('a' + random.nextInt('z' - 'A')));
        }
        return text.toString();
    }
}
