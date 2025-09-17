package projetomps.app_logic.log;

import projetomps.app_logic.log.adapters.JulLoggerAdapter;   
import projetomps.app_logic.log.adapters.Slf4jLoggerAdapter; 

public final class AppLoggerFactory {
    public enum Backend { SLF4J, JUL }

    private static final Backend BACKEND = resolve();

    private AppLoggerFactory(){}

    public static AppLogger getLogger(Class<?> clazz) {
        return switch (BACKEND) {
            case JUL   -> new JulLoggerAdapter(clazz);
            case SLF4J -> new Slf4jLoggerAdapter(clazz);
        };
    }

    private static Backend resolve() {
        String prop = System.getProperty("app.logger");
        if (prop == null || prop.isBlank()) prop = System.getenv("APP_LOGGER");
        if (prop == null) return Backend.SLF4J;
        try {
            return Backend.valueOf(prop.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            return Backend.SLF4J;
        }
    }
}
