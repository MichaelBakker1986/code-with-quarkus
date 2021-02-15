package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.appmodel.MostUsed;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Slf4j
@Path("/api/page/{name}")
public class PagingService {
    @PathParam("name") String  name;
    @Inject            Session s;
    @GET
    @Cache(maxAge = 3600)
    @Produces(MediaType.TEXT_PLAIN)
    public Response page() {
        val cb   = s.getCriteriaBuilder();
        val cr   = cb.createQuery(MostUsed.class);
        val root = cr.from(MostUsed.class);
        cr.select(root);
        cr.where(cb.equal(root.get("name"), name));
        Query<MostUsed> query = s.createQuery(cr);
        query.setMaxResults(1);
        query.setReadOnly(true);
        query.setCacheable(true);
        var list = query.uniqueResult();
        if (list == null) return Response.ok(0L).build();
        return Response.ok((long) list.getUsed()).build();
    }
}
