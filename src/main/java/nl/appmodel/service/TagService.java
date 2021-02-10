package nl.appmodel.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import nl.appmodel.QuarkusHibernateUtil;
import nl.appmodel.Tags;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Slf4j
@Path("/api/tags")
public class TagService {
    @Inject QuarkusHibernateUtil util;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String tags() {
        return util.session("tags", s -> {
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
            var query1 = s.createNativeQuery(
                    "select id,LOWER(name) as name From prosite.most_popular mt inner join tags t on mt.tag_id = t.id  order by mt.popularity desc",
                    Tags.class);
            query1.setMaxResults(10);
            query1.setReadOnly(true);
            query1.setCacheable(true);
            return new Gson().toJson(query1.list());
        });
    }
}
