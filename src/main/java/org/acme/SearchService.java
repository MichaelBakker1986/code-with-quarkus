package org.acme;

import com.google.gson.Gson;
import lombok.val;
import nl.appmodel.Base64;
import nl.appmodel.DataObjectPro;
import nl.appmodel.HibernateUtilWithJavaConfig;
import nl.appmodel.Pro;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.persistence.criteria.Predicate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
@Path("/lookup/{name}")
public class SearchService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello(@PathParam String name) {
        Session session = HibernateUtilWithJavaConfig.getSessionFactory().getCurrentSession();

        val cb   = session.getCriteriaBuilder();
        val cr   = cb.createQuery(Pro.class);
        val root = cr.from(Pro.class);
        cr.select(root);
        cr.orderBy(cb.desc(root.get("views")));

        Predicate predLike
                = cb.like(root.get("tag"), "%" + name + "%");
        Predicate predDownloaded
                = cb.equal(root.get("downloaded"), true);
        cr.where(cb.and(predDownloaded, predLike));
        Query<Pro> query = session.createQuery(cr);
        query.setMaxResults(50);
        query.setHint("org.hibernate.cacheable", true);
        query.setCacheable(true);
        List<Pro> list = query.list();
        /*   session.close();*/

        var object = list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()))).toArray();
        return new Gson().toJson(object);
    }
}
