package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.QuarkusHibernateUtil;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
@Slf4j
@Path("/api/total")
public class TotalCountService {
    @Inject QuarkusHibernateUtil util;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response total() {
        try {
            return Response
                    .ok(util.session("/total", session -> session
                            //select count(*) from Pro where downloaded=true
                            .createQuery("select SUM(downloaded) from Host")
                            .setReadOnly(true)
                            .setCacheable(true)
                            .setHint("org.hibernate.cacheable", true)
                            .uniqueResult())).build();
        } catch (Exception e) {
            log.error("ERROR", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
