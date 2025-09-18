package projetomps.app_logic.log.adapters;

import java.util.logging.Level;
import java.util.logging.Logger;
import projetomps.app_logic.log.AppLogger;

public final class JulLoggerAdapter implements AppLogger {
    private final Logger log;
    public JulLoggerAdapter(Class<?> clazz){ this.log = Logger.getLogger(clazz.getName()); }
    @Override public boolean isTraceEnabled(){ return log.isLoggable(Level.FINER); }
    @Override public boolean isDebugEnabled(){ return log.isLoggable(Level.FINE);  }
    @Override public boolean isInfoEnabled (){ return log.isLoggable(Level.INFO);  }
    @Override public boolean isWarnEnabled (){ return log.isLoggable(Level.WARNING);}
    @Override public boolean isErrorEnabled(){ return log.isLoggable(Level.SEVERE); }
    @Override public void trace(String m, Object... a){ log.log(Level.FINER,   m, a); }
    @Override public void debug(String m, Object... a){ log.log(Level.FINE,    m, a); }
    @Override public void info (String m, Object... a){ log.log(Level.INFO,    m, a); }
    @Override public void warn (String m, Object... a){ log.log(Level.WARNING, m, a); }
    @Override public void error(String m, Object... a){ log.log(Level.SEVERE,  m, a); }
    @Override public void error(String m, Throwable t, Object... a){ log.log(Level.SEVERE, m, t); }
}
