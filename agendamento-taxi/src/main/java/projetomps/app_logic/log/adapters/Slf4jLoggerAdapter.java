package projetomps.app_logic.log.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projetomps.app_logic.log.AppLogger;      

public final class Slf4jLoggerAdapter implements AppLogger {  
    private final Logger log;

    public Slf4jLoggerAdapter(Class<?> clazz) {               
        this.log = LoggerFactory.getLogger(clazz);
    }

    @Override public boolean isTraceEnabled(){ return log.isTraceEnabled(); }
    @Override public boolean isDebugEnabled(){ return log.isDebugEnabled(); }
    @Override public boolean isInfoEnabled (){ return log.isInfoEnabled();  }
    @Override public boolean isWarnEnabled (){ return log.isWarnEnabled();  }
    @Override public boolean isErrorEnabled(){ return log.isErrorEnabled(); }

    // Use a interpolação nativa do SLF4J (melhor performance, sem MsgFmt)
    @Override public void trace(String m, Object... a){ if (log.isTraceEnabled()) log.trace(m, a); }
    @Override public void debug(String m, Object... a){ if (log.isDebugEnabled()) log.debug(m, a); }
    @Override public void info (String m, Object... a){ if (log.isInfoEnabled())  log.info (m, a); }
    @Override public void warn (String m, Object... a){ if (log.isWarnEnabled())  log.warn (m, a); }
    @Override public void error(String m, Object... a){ if (log.isErrorEnabled()) log.error(m, a); }
    @Override public void error(String m, Throwable t, Object... a){ log.error(m, a, t); }
}
