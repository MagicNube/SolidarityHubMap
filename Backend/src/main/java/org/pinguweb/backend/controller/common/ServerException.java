package org.pinguweb.backend.controller.common;

import org.springframework.data.jpa.repository.JpaRepository;

public class ServerException {
    public static boolean isServerClosed(JpaRepository<?, ?> repository) {
        return repository == null;
    }
}
