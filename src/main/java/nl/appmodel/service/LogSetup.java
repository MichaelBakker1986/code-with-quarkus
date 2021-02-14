package nl.appmodel.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.hibernate.cfg.Environment;
import org.slf4j.LoggerFactory;
public interface LogSetup {
    //@formatter:off
     Object[][] LEVELS = {
            { 
             Environment.class,Level.WARN
            }
    };
    //@formatter:on
    static void setupLog(Level base_level) {
        var hibernate  = java.util.logging.Logger.getLogger("org.hibernate");
        var jboss      = java.util.logging.Logger.getLogger("org.jboss");
        var rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(base_level);
        var stdout = (ConsoleAppender<ILoggingEvent>) rootLogger.getAppender("console");
        var layout = new PatternLayout();
        layout.setPattern("%highlight(%d{ss} %-40(.\\(%F:%L\\)) %d{SSS}) %m%n");
        layout.setContext(stdout.getContext());
        stdout.setLayout(layout);

        layout.start();

        for (var log_configuration : LEVELS) {
            var o = (Level) log_configuration[1];
            if (o.isGreaterOrEqual(base_level)) {
                ((Logger) LoggerFactory.getLogger((Class<?>) log_configuration[0])).setLevel((Level) log_configuration[1]);
            }
        }
        hibernate.setLevel(java.util.logging.Level.WARNING);
        hibernate.setUseParentHandlers(false);
        jboss.setLevel(java.util.logging.Level.WARNING);
        jboss.setUseParentHandlers(false);
    }
}
