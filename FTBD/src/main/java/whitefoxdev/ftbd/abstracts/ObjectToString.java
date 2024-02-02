package whitefoxdev.ftbd.abstracts;

import java.lang.reflect.Field;

public abstract class ObjectToString {

    public String toString() {
        StringBuilder builder = new StringBuilder(); //"\n" + getClass().getSimpleName() + ":"
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String text = field.get(this).toString();
                if (text.charAt(0) == '[') {
                    text = text.substring(1, text.length() - 1).replaceAll(",", "\n________");
                }
                text = text.replaceAll("\n", "\n\t");
                builder.append("\n" + name + ": " + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return builder.toString();
    }
}
