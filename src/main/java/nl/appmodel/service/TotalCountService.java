package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
@Slf4j
@Path("/api")
public class TotalCountService {
    public static String format(Number in) {
        DecimalFormat        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols   = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(in);
    }
    @Inject Session s;
    @GET
    @Cache(maxAge = 36000)
    @Path("/total")
    @Produces(MediaType.TEXT_PLAIN)
    public Response total() {

        Object entity = s.createQuery("select SUM(downloaded) from Host")
                         .setReadOnly(true)
                         .setCacheable(true)
                         .setHint("org.hibernate.cacheable", true)
                         .uniqueResult();

        return Response
                .ok(format(Long.valueOf(entity.toString()))).build();
    }
}
