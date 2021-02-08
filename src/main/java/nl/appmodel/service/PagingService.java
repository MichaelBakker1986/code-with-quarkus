package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.appmodel.MostUsed;
import nl.appmodel.QuarkusHibernateUtil;
import org.hibernate.query.Query;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Slf4j
@Path("/Page/{name}")
public class PagingService {
    @javax.ws.rs.PathParam("name") private String               name;
    @Inject                                QuarkusHibernateUtil util;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Long hello() {
        return util.session("Page/" + name, s -> {
            val cb   = s.getCriteriaBuilder();
            val cr   = cb.createQuery(MostUsed.class);
            val root = cr.from(MostUsed.class);
            cr.select(root);
            cr.where(cb.equal(root.get("name"), name));
            Query<MostUsed> query = s.createQuery(cr);
            query.setMaxResults(1);
            query.setReadOnly(true);
            query.setCacheable(true);
            MostUsed list = query.uniqueResult();
            if (list == null) return 0L;
            return (long) list.getUsed();
           /* return ((Number) s.createQuery("SELECT count(*) from Pro p " +
                                           "join ProTags pt on pt.pro=p.id " +
                                           "join Tags t on  t.id=pt.tag " +
                                           "where p.downloaded=true " +
                                           "and t.name=:name")
                              .setParameter("name", name)
                              .setReadOnly(true)
                              .setCacheable(true)
                              .uniqueResult()).longValue();*/
        });
    }
}
