package nl.appmodel.service;

import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;
import nl.appmodel.Picture;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.cache.Cache;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Slf4j
@Path("/picture")
public class PictureService {
    @ConfigProperty(name = "fileserver.path")
    String fileServerPath;
    @GET
    @Cache(maxAge = 36000)
    @Path("/{sha}")
    @Produces(MediaType.APPLICATION_JSON)
    @CacheResult(cacheName = "picture")
    public Response page(@PathParam("sha") String sha) {
        return Response
                .ok(Picture.fromSha(java.nio.file.Path.of(fileServerPath), sha))
                .build();
    }
}
