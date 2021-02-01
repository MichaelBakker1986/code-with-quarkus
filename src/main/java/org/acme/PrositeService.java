package org.acme;

import com.google.gson.Gson;
import nl.appmodel.Base64;
import nl.appmodel.DataObjectPro;
import nl.appmodel.HibernateUtilWithJavaConfig;
import nl.appmodel.Pro;
import org.hibernate.Session;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
@Path("/search")
public class PrositeService {
    Session session = HibernateUtilWithJavaConfig
            .getSessionFactory()
            .openSession();
    Gson    gson    = new Gson();
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String hello() {
        CriteriaBuilder    cb   = session.getCriteriaBuilder();
        CriteriaQuery<Pro> cr   = cb.createQuery(Pro.class);
        Root<Pro>          root = cr.from(Pro.class);
        cr.select(root);
        cr.orderBy(cb.desc(root.get("views")));

        Query<Pro> query = session.createQuery(cr);
        query.setMaxResults(100);
        query.setHint("org.hibernate.cacheable", true);
        query.setCacheable(true);
        List<Pro> list   = query.list();
        var       object = list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()))).toArray();
        /*var o = session.createQuery("FROM Pro order by views DESC")
                       .setMaxResults(100)
                       .setHint("org.hibernate.cacheable", true).getResultList();
*/
        return gson.toJson(object);
    }
}
