package nl.appmodel.service;

import io.quarkus.runtime.Startup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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
    public <X> X session(Transact<X> run) {
        var unwrap = em.unwrap(Session.class);
        unwrap.setDefaultReadOnly(true);
        return run.run(unwrap);
    }
}
