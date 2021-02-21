package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.appmodel.DataObjectPro;
import nl.appmodel.Pro;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
@Slf4j
@Path("/api/lookup/{name}")
public class LookupService {
    @Inject Session s;
    @GET
    @Cache(maxAge = 3600)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Pro calls", description = "LookupService operations.")
    public Response hello(@PathParam("name") String name) {
        try {
            s.setDefaultReadOnly(true);
            var sql = "SELECT pro_id as id,p.views as views,thumbs,header,embed,status from promyis p " +
                      "join pro pp on pp.id =p.pro_id " +
                      "join pro_tags pt on pt.pro=pp.id " +
                      "join tags t on t.id=pt.tag " +
                      "where pp.status=2 " +
                      "and t.name=:name";
            Query<Pro> q = s.createNativeQuery(sql, Pro.class);
            q.setParameter("name", name);
            q.setReadOnly(true);
            q.setMaxResults(32);
            q.setHint("org.hibernate.cacheable", true);
            q.setCacheable(true);
            List<Pro> list = q.list();
            val x = list.stream().map(l -> new DataObjectPro(l.getId(), 1, Base64.fromId(l.getId()), l.getHeader(), l.getEmbed()))
                        .toArray();
            return Response.ok(x).build();
        } catch (Exception e) {
            log.error("ERROR", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
