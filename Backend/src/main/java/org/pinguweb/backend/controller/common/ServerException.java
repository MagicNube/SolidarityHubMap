package org.pinguweb.backend.controller.common;

import org.springframework.data.jpa.repository.JpaRepository;

public class ServerException {
    public static boolean isServerConnected(JpaRepository<?, ?> repository) {
        return repository != null;
    }
}
