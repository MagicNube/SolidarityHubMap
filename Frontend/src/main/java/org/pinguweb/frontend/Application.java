package org.pinguweb.frontend;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.pinguweb.frontend.services.BackendDTOObservableService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
@Theme("my-theme")
@Push
public class Application implements AppShellConfigurator {

    private BackendDTOObservableService service;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void generateBackendDTOService(){
        service = BackendDTOObservableService.GetInstancia();
    }

    @PreDestroy
    public void onShutdown() {
        service.shutdown();
        SecurityContextHolder.clearContext();
    }
}
