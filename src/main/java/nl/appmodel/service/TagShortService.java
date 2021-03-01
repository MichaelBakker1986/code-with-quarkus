package nl.appmodel.service;

import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;
import nl.appmodel.Tags;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
@Slf4j
@Path("/api/tags_fn/{short_name}")
public class TagShortService {
    @Inject Session s;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Cache(maxAge = 43200)
    @CacheResult(cacheName = "short-fn")
    public Response tags(@PathParam("short_name") @DefaultValue("short_name") String short_name) {
        if (short_name.length() < 2) return empty();
        NativeQuery<Tags> q;
        if (short_name.length() == 2) {
            q = s.createNativeQuery(
                    "select id,LOWER(name) as name,mt.popularity as popularity From most_popular mt inner join tags t on mt.tag_id = t.id  where short_2=:short_name and popularity > 0 order by mt.popularity desc",
                    Tags.class);
            q.setParameter("short_name", short_name);
        } else if (short_name.length() == 3) {
            q = s.createNativeQuery(
                    "select id,LOWER(name) as name,mt.popularity as popularity From most_popular mt inner join tags t on mt.tag_id = t.id  where short_3=:short_name and popularity > 0 order by mt.popularity desc",
                    Tags.class);
            q.setParameter("short_name", shorten(short_name, 3));
        } else if (short_name.length() == 4) {
            q = s.createNativeQuery(
                    "select id,LOWER(name) as name,mt.popularity as popularity From most_popular mt inner join tags t on mt.tag_id = t.id  where short_4=:short_name and popularity > 0 order by mt.popularity desc",
                    Tags.class);
            q.setParameter("short_name", shorten(short_name, 4));
        } else if (short_name.length() >= 5) {
            q = s.createNativeQuery(
                    "select id,LOWER(name) as name,mt.popularity as popularity From most_popular mt inner join tags t on mt.tag_id = t.id  where short_5=:short_name and popularity > 0 order by mt.popularity desc",
                    Tags.class);
            q.setParameter("short_name", shorten(short_name, 5));
        } else {
            return empty();
        }
        q.setMaxResults(20);
        q.setReadOnly(true);
        q.setHint("org.hibernate.cacheable", true);
        q.setCacheable(true);
        return Response.ok(q.list()).build();
    }
    private Response empty() {
        return Response.ok(new ArrayList<>()).build();
    }
    private String shorten(String short_name, int size) {
        return short_name.substring(0, size);
    }
}
