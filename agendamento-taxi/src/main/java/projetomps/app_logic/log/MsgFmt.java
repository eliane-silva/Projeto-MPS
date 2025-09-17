package projetomps.app_logic.log;

import java.util.Arrays;

public final class MsgFmt {
    private MsgFmt(){}

    public static String format(String template, Object... args) {
        if (template == null) return "null";
        // troca {} por %s mas preserva {n} ou {{ }}
        String printf = template.replace("{}", "%s");
        try {
            return String.format(printf, Arrays.stream(args).map(String::valueOf).toArray());
        } catch (Exception e) {
            // fallback defensivo
            return template + " " + Arrays.toString(args);
        }
    }
}
