package org.pinguweb.frontend.services.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackendObject<T> {
    HttpHeaders headers;
    HttpStatusCode statusCode;
    T data;
}
