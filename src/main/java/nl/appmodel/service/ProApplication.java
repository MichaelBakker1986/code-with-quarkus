package nl.appmodel.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import javax.ws.rs.core.Application;
@Slf4j
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://appmodel.org"),
                @Server(url = "https://127.0.0.1:8083"),
                @Server(url = "https://127.0.0.1:8081"),
                @Server(url = "https://127.0.0.1:8080")
        },
        tags = {
        },
        info = @Info(
                title = "ProSite API",
                version = "1.0.0",
                contact = @Contact(
                        name = "API Support",
                        url = "http://appmodel.nl",
                        email = "michael@appmodel.nl"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ProApplication extends Application {
}
