package org.acme;

import com.google.gson.Gson;
import lombok.val;
import nl.appmodel.Base64;
import nl.appmodel.DataObjectPro;
import nl.appmodel.HibernateUtilWithJavaConfig;
import nl.appmodel.Pro;
import org.hibernate.Session;
import org.hibernate.query.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
@Path("/search")
public class PrositeService {
    Gson gson = new Gson();
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String hello() {
        Session session = HibernateUtilWithJavaConfig
                .getSessionFactory()
                .openSession();
        val cb   = session.getCriteriaBuilder();
        val cr   = cb.createQuery(Pro.class);
        val root = cr.from(Pro.class);
        cr.select(root);
        cr.orderBy(cb.desc(root.get("views")));
        cr.where(cb.equal(root.get("downloaded"), true));

        Query<Pro> query = session.createQuery(cr);
        query.setMaxResults(100);
        query.setHint("org.hibernate.cacheable", true);
        query.setCacheable(true);
        List<Pro> list = query.list();
        session.close();
        var object = list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()))).toArray();
        /*var o = session.createQuery("FROM Pro order by views DESC")
                       .setMaxResults(100)
                       .setHint("org.hibernate.cacheable", true).getResultList();
*/
        return gson.toJson(object);
    }
}
