package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import nl.appmodel.QuarkusHibernateUtil;
import nl.appmodel.Tags;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
@Slf4j
@Path("/api/tags")
public class TagService {
    /*val cb   = s.getCriteriaBuilder();
 val cr   = cb.createQuery(MostPopularTags.class);
 val root = cr.from(MostPopularTags.class);
 cr.select(root);
 cr.orderBy(cb.desc(root.get("popularity")));
 Query<MostPopularTags> query = s.createQuery(cr);
 query.setMaxResults(20);
 query.setReadOnly(true);
 query.setCacheable(true);
 var list   = query.list();*/
    @Inject QuarkusHibernateUtil util;
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response tags() {
        try {
            return Response.ok(util.session("tags", s -> {
                var query1 = s.createNativeQuery(
                        "select id,LOWER(name) as name From prosite.most_popular mt inner join tags t on mt.tag_id = t.id  order by mt.popularity desc",
                        Tags.class);
                query1.setMaxResults(10);
                query1.setReadOnly(true);
                query1.setCacheable(true);
                return query1.list();
            })).build();
        } catch (Exception e) {
            log.error("ERROR", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
