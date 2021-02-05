package nl.appmodel;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
@Slf4j
public class HibernateUtilWithJavaConfig {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {

        var                instance   = CacheManager.getInstance();
        var                cacheNames = instance.getCacheNames();
        Cache              c          = instance.getCache("nl.appmodel.Pro");
        CacheConfiguration config     = c.getCacheConfiguration();

        try {
            var properties = new Properties();
            properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            properties.put(Environment.URL, "jdbc:mysql://localhost:3306/prosite?serverTimezone=UTC&useSSL=false");
            properties.put(Environment.USER, "root");
            properties.put(Environment.PASS, "Welkom01!");
            properties.put(Environment.FORMAT_SQL, "false");
            properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            properties.put(Environment.SHOW_SQL, "true");
            properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            properties.put(Environment.HBM2DDL_AUTO, "none");
            properties.put(Environment.POOL_SIZE, "15");
            properties.put(Environment.USE_QUERY_CACHE, true);
            properties.put(Environment.USE_SECOND_LEVEL_CACHE, true);
            properties.put(Environment.JPA_SHARED_CACHE_MODE, "ALL");
            properties.put(Environment.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
            return new Configuration()
                    .setProperties(properties)
                    .addAnnotatedClass(Pro.class)
                    .addAnnotatedClass(ProTags.class)
                    .addAnnotatedClass(Tags.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            log.error("SessionFactory build failed :", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public interface Transact<X> {
        X run(Session session);
    }
    public static <X> X session(String name, Transact<X> run) {
        Stopwatch sw             = Stopwatch.createStarted();
        var       currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.setDefaultReadOnly(true);
        X x = run.run(currentSession);
        currentSession.clear();
        currentSession.close();
        log.info("Query [{}] took [{}]ms", name, sw.elapsed(TimeUnit.MILLISECONDS));
        return x;
    }
    public static Session getSession() {
        var currentSession = sessionFactory.getCurrentSession();
        if (!currentSession.getTransaction().isActive()) {
            currentSession.beginTransaction();
            currentSession.setDefaultReadOnly(true);
        }
        return currentSession;
    }
}
