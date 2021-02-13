package nl.appmodel;

import com.google.common.base.Stopwatch;
import io.quarkus.runtime.Startup;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.Session;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;
@Slf4j
@Singleton
@Startup
public class QuarkusHibernateUtil {
    @Inject
    EntityManager em;
    public interface Transact<X> {
        X run(Session session);
    }
    @Transactional
    public <X> X session(String name, Transact<X> run) {
        val sw     = Stopwatch.createStarted();
        var unwrap = em.unwrap(Session.class);
        unwrap.setDefaultReadOnly(true);
        X x = run.run(unwrap);
        log.info("Query [{}] took [{}]ms", name, sw.elapsed(TimeUnit.MILLISECONDS));
        return x;
    }
}
