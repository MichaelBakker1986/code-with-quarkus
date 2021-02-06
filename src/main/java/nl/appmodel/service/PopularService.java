package nl.appmodel.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import nl.appmodel.Base64;
import nl.appmodel.DataObjectPro;
import nl.appmodel.Pro;
import nl.appmodel.QuarkusHibernateUtil;
import org.hibernate.query.Query;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
@Slf4j
@Path("/search")
public class PopularService {
    Gson gson = new Gson();
    @Inject QuarkusHibernateUtil util;
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String hello() {
        Object[] x = util.session("/search", s -> {
       /*     val cb   = session.getCriteriaBuilder();
            val cr   = cb.createQuery(Pro.class);
            val root = cr.from(Pro.class);
            cr.select(root);
            cr.orderBy(cb.desc(root.get("views")));
            cr.where(cb.equal(root.get("downloaded"), true));
            Query<Pro> query = session.createQuery(cr);
            query.setMaxResults(50);
            query.setReadOnly(true);
            query.setHint("org.hibernate.cacheable", true);
            query.setCacheable(true);
            List<Pro> list = query.list();
            return list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()))).toArray();*/

            var sql = "SELECT pro_id as id,downloaded,ref,p.views as views,thumbs from promyis p " +
                      "join pro pp on pp.id =p.pro_id " +
                      "where pp.downloaded=1";
            Query<Pro> query = s.createNativeQuery(sql, Pro.class);
            query.setReadOnly(true);
            query.setMaxResults(50);
            query.setHint("org.hibernate.cacheable", true);
            query.setCacheable(true);
            List<Pro> list = query.list();
            return list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()))).toArray();
        });
        return gson.toJson(x);
    }
}
