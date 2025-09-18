package projetomps.app_logic.log;

public interface AppLogger {
    boolean isTraceEnabled();
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();

    void trace(String msg, Object... args);
    void debug(String msg, Object... args);
    void info (String msg, Object... args);
    void warn (String msg, Object... args);
    void error(String msg, Object... args);
    void error(String msg, Throwable t, Object... args);
}
