package nl.appmodel.service;

import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;
import nl.appmodel.MostUsed;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Slf4j
@Path("/api")
public class PagingService {
    @GET
    @Cache(maxAge = 36000)
    @Path("/page/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    @CacheResult(cacheName = "page-count")
    public Response page(@PathParam("name") String name) {
        MostUsed panacheEntityBase = MostUsed.find("name", name).firstResult();
        if (panacheEntityBase == null) return Response.ok(0L).build();
        return Response.ok((long) panacheEntityBase.getUsed()).build();
    }
}
