package org.acme;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.HibernateUtilWithJavaConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Slf4j
@Path("/total")
public class TotalCountService {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Long total() {
        return (Long) HibernateUtilWithJavaConfig
                .session("/total", session -> session
                        .createQuery("select count(*) from Pro where downloaded=true")
                        .setReadOnly(true)
                        .setCacheable(true)
                        .setHint("org.hibernate.cacheable", true)
                        .uniqueResult());
    }
}
