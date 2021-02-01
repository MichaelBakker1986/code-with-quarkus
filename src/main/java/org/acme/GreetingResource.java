package org.acme;

import nl.appmodel.HibernateUtilWithJavaConfig;
import org.hibernate.Session;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("/hello-resteasy")
public class GreetingResource {
    Session session = HibernateUtilWithJavaConfig
            .getSessionFactory()
            .openSession();
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        var o = session.createQuery("select count(*) from Pro where downloaded=true")
                       .setHint("org.hibernate.cacheable", true)
                       .uniqueResult();
        return "Hello RESTEasydada3131das" + taest() + " ->" + o;
    }
    public String taest() {
        return "daddass1das";
    }
}
