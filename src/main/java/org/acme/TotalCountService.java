package org.acme;

import nl.appmodel.HibernateUtilWithJavaConfig;
import org.hibernate.Session;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("/total")
public class TotalCountService {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Long hello() {
        Session session = HibernateUtilWithJavaConfig
                .getSessionFactory()
                .getCurrentSession();
        var o = session.createQuery("select count(*) from Pro where downloaded=true")
                       .setHint("org.hibernate.cacheable", true)
                       .uniqueResult();
        //session.close();
        return (Long) o;
    }
}
