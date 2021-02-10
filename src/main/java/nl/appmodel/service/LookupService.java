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
@Path("/lookup/{name}")
public class LookupService {
    @Inject QuarkusHibernateUtil util;
    /*
    val     cb      = session.getCriteriaBuilder();
       val     cr      = cb.createQuery(Pro.class);
       val     root    = cr.from(Pro.class);
       cr.select(root);
       cr.orderBy(cb.desc(root.get("views")));

       Predicate predLike
               = cb.like(root.get("tag"), "%" + name + "%");
       Predicate predDownloaded
               = cb.equal(root.get("downloaded"), true);
       cr.where(cb.and(predDownloaded, predLike));
       Query<Pro> query = session.createQuery(cr);
       */
    @javax.ws.rs.PathParam("name")
    private String               name;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        Object[] x = util.session("lookup/" + name, s -> {
            var sql_old = "SELECT * from pro p join pro_tags pt on pt.pro=p.id join tags t on  t.id=pt.tag where t.name=:name and p.downloaded=1 order by p.views desc";
            var sql = "SELECT pro_id as id,downloaded,p.views as views,thumbs,header,embed from promyis p " +
                      "join pro_tags pt on pt.pro=p.pro_id " +
                      "join tags t on t.id=pt.tag " +
                      "join pro pp on pp.id =p.pro_id " +
                      "where pp.downloaded=1 " +
                      "and t.name=:name";
            Query<Pro> query = s.createNativeQuery(sql, Pro.class);
            query.setParameter("name", name);
            query.setReadOnly(true);
            query.setMaxResults(50);
            query.setHint("org.hibernate.cacheable", true);
            query.setCacheable(true);
            List<Pro> list = query.list();
            return list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()), l.getHeader(), l.getEmbed())).toArray();
        });
        return new Gson().toJson(x);
    }
}
