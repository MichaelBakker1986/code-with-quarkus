package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.QuarkusHibernateUtil;
import nl.appmodel.Tags;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
@Slf4j
@Path("/api/tags/{best}")
public class TagService {
    @PathParam("best") @DefaultValue("10") private int                  best;
    @Inject                                        QuarkusHibernateUtil util;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Cache(maxAge = 43200)
    public Response tags() {
        try {
            return Response.ok(util.session("tags", s -> {
                var q = s.createNativeQuery(
                        "select id,LOWER(name) as name,popularity From most_popular mt inner join tags t on mt.tag_id = t.id  order by mt.popularity desc",
                        Tags.class);
                q.setMaxResults(best);
                q.setReadOnly(true);
                q.setHint("org.hibernate.cacheable", true);
                q.setCacheable(true);
                return q.list();
            })).build();
        } catch (Exception e) {
            log.error("ERROR", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
