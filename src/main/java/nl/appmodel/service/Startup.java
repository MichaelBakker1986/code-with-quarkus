package nl.appmodel.service;

import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.event.Observes;
@Slf4j
@io.quarkus.runtime.Startup
public class Startup {
    @ConfigProperty(name = "quarkus.live-reload.url", defaultValue = "http://appmodel.nl/api")
    String url;
    void onStart(@Observes StartupEvent event) {
        log.info("App started at {}", url);
    }
}
