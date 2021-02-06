package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.QuarkusHibernateUtil;
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
            return ((Number) s.createQuery("SELECT count(*) from Pro p " +
                                           "join ProTags pt on pt.pro=p.id " +
                                           "join Tags t on  t.id=pt.tag " +
                                           "where p.downloaded=true " +
                                           "and t.name=:name")
                              .setParameter("name", name)
                              .setReadOnly(true)
                              .setCacheable(true)
                              .uniqueResult()).longValue();
        });
    }
}
