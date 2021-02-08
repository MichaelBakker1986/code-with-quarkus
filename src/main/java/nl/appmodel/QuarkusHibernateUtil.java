package nl.appmodel;

import com.google.common.base.Stopwatch;
import io.quarkus.runtime.Startup;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
@Slf4j
@Singleton
@Startup
public class QuarkusHibernateUtil {
    @ConfigProperty(name = "db_schema", defaultValue = "prosite")
    String db_schema;
    private SessionFactory sessionFactory;
    @PostConstruct
    public void startup() {
        log.info("Setting up hibernate. Got: db_schema: [{}]", db_schema);
        var properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL,
                       "jdbc:mysql://127.0.0.1:3306/" + db_schema + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true");
        properties.put(Environment.USER, "read_only");
        properties.put(Environment.PASS, "Welkom02!");
        properties.put(Environment.FORMAT_SQL, "false");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "none");
        properties.put(Environment.POOL_SIZE, "15");
        properties.put(Environment.USE_QUERY_CACHE, true);
        properties.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        properties.put(Environment.JPA_SHARED_CACHE_MODE, "ALL");
        properties.put("hibernate.cache.ehcache.missing_cache_strategy", "create");
        properties.put(Environment.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Pro.class)
                .addAnnotatedClass(ProTags.class)
                .addAnnotatedClass(Tags.class)
                .addAnnotatedClass(MostUsed.class)
                .addAnnotatedClass(MostPopularTags.class)
                .buildSessionFactory();
    }
    public interface Transact<X> {
        X run(Session session);
    }
    public <X> X session(String name, Transact<X> run) {
        val sw             = Stopwatch.createStarted();
        var currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.setDefaultReadOnly(true);
        X x = run.run(currentSession);
        currentSession.clear();
        currentSession.close();
        log.info("Query [{}] took [{}]ms", name, sw.elapsed(TimeUnit.MILLISECONDS));
        return x;
    }
}
