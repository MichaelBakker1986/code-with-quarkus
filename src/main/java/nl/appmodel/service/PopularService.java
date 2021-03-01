package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.DataObjectPro;
import nl.appmodel.Pro;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
@Slf4j
@Path("/api/popular")
public class PopularService {
    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject Session s;
    @GET
    @Cache(maxAge = 36000)
    @Produces(MediaType.APPLICATION_JSON)
    public Response moseUsed() {
        try {
            var sql = "SELECT p.id,views,thumbs,header,embed,status,duration FROM pro p " +
                      "where p.status=2 " +
                      "order by views DESC ";
            Query<Pro> query = s.createNativeQuery(sql, Pro.class);
            query.setReadOnly(true);
            query.setMaxResults(32);
            query.setHint("org.hibernate.cacheable", true);
            query.setCacheable(true);
            List<Pro> list = query.list();
            Object[] x = list.stream().map(
                    l -> new DataObjectPro(l.getId(), 1, NumberBase64.fromId(l.getId()), l.getHeader(), l.getEmbed(), l.getDuration()))
                             .toArray();
            return Response.ok(x).build();
        } catch (Exception e) {
            new Notifier().displayTray(e.getMessage());
            return Response.serverError().build();
        }
    }
}
