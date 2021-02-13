package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.appmodel.MostUsed;
import nl.appmodel.QuarkusHibernateUtil;
import org.hibernate.query.Query;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
@Slf4j
@Path("/api/Page/page-{name}")
public class PagingService {
    @PathParam("name") private String               name;
    @Inject                    QuarkusHibernateUtil util;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello() {
        try {
            return Response.ok(util.session("Page/page-" + name, s -> {
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
            })).build();
        } catch (Exception e) {
            log.error("ERROR", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
