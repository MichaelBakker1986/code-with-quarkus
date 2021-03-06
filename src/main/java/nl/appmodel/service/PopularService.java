package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.DataObjectPro;
import nl.appmodel.Pro;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
@Slf4j
@Path("/api/search")
public class PopularService {
    @Inject QuarkusHibernateUtil util;
    @Inject Session              session;
    @POST
    @Cache(maxAge = 3600)
    @Produces({MediaType.APPLICATION_JSON})
    public Response hello() {
        try {
            Object[] x = util.session(s -> {
                var sql = "SELECT pro_id as id,downloaded,p.views as views,thumbs,header,embed from promyis p " +
                          "join pro pp on pp.id =p.pro_id " +
                          "where pp.downloaded=1";
                Query<Pro> query = s.createNativeQuery(sql, Pro.class);
                query.setReadOnly(true);
                query.setMaxResults(32);
                query.setHint("org.hibernate.cacheable", true);
                query.setCacheable(true);
                List<Pro> list = query.list();
                return list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()), l.getHeader(), l.getEmbed()))
                           .toArray();
            });
            return Response.ok(x).build();
        } catch (Exception e) {
            log.error("ERROR", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
