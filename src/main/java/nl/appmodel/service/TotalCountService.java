package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Slf4j
@Path("/api/total")
public class TotalCountService {
    @Inject Session s;
    @GET
    @Cache(maxAge = 36000)
    @Produces(MediaType.TEXT_PLAIN)
    public Response total() {
        return Response
                .ok(s.createQuery("select SUM(downloaded) from Host")
                     .setReadOnly(true)
                     .setCacheable(true)
                     .setHint("org.hibernate.cacheable", true)
                     .uniqueResult()).build();
    }
}
