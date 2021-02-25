package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Slf4j
@Path("/api/mat")
public class SimpleMatService {
    @GET
    @Cache(maxAge = 36000)
    @Produces(MediaType.TEXT_PLAIN)
    public Response total() {
     /*   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        var src    = Imgcodecs.imread("\\\\SERVER\\flat\\DUFMP", Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println(String.valueOf(src.rows()));*/
        return Response
                .ok("hi").build();
    }
}
