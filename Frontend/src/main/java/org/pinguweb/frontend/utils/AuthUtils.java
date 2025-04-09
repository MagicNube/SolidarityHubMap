package org.pinguweb.frontend.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Timer;
import java.util.TimerTask;

public class AuthUtils {

    public static void checkAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            System.out.println("Authentication required");
            showRedirectDialog("Redirigiendo al login...", "login");
        }
    }

    public static void logout() {
        SecurityContextHolder.clearContext();
        showRedirectDialog("Cerrando sesión. Redirigiendo al login...", "login");
    }

    private static void showRedirectDialog(String message, String route) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.add(message);
        dialog.open();

        UI currentUI = UI.getCurrent();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("RUNEANDO");
                System.out.println(currentUI == null);
                if (currentUI != null) {
                    currentUI.access(() -> {
                        dialog.close();
                        currentUI.navigate(route);
                        System.out.println("Ruteado to login");
                    });
                }
            }
        }, 2000);
    }
}
