package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.appmodel.Tags;
import org.hibernate.Session;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Slf4j
@Path("/api/tags/{best}")
public class TagService {
    @Inject Session s;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Cache(maxAge = 43200)
    public Response tags(@PathParam("best") @DefaultValue("10") int best) {
        val q = s.createNativeQuery(
                "select id,LOWER(name) as name,mt.popularity as popularity From most_popular mt inner join tags t on mt.tag_id = t.id  order by mt.popularity desc",
                Tags.class);
        q.setMaxResults(best);
        q.setReadOnly(true);
        q.setHint("org.hibernate.cacheable", true);
        q.setCacheable(true);
        return Response.ok(q.list()).build();
    }
}
