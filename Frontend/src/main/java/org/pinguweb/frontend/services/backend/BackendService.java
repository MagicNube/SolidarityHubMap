package org.pinguweb.frontend.services.backend;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BackendService {

    public static final String BACKEND = "http://localhost:8081";

    public static <T> BackendObject<T> getFromBackend(String url, Class<T> expected) throws RestClientException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<T> respuesta = restTemplate.getForEntity(url, expected);
            return generateObject(respuesta);
        }
        catch (Exception e) {
            return new BackendObject<T>(null, HttpStatus.SERVICE_UNAVAILABLE, null);
        }
    }

    public static <T> BackendObject<T> postToBackend(String url, T object, Class<T> expected) throws RestClientException {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<T> respuesta = restTemplate.postForEntity(url, object, expected);
            return generateObject(respuesta);
            }
        catch (Exception e) {
            return new BackendObject<T>(null, HttpStatus.SERVICE_UNAVAILABLE, null);
        }
    }

    public static <T> HttpStatusCode putToBackend(String url, T object) throws RestClientException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<T> entidad = new HttpEntity<>(object, headers);

            ResponseEntity<Void> respuesta = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entidad,
                    Void.class
            );

            return respuesta.getStatusCode();
        }
        catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    public static <T> HttpStatusCode deleteFromBackend(String url) throws RestClientException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<T> entidad = new HttpEntity<>(headers);

            ResponseEntity<Void> respuesta = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entidad,
                    Void.class
            );

            return respuesta.getStatusCode();
        }
        catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    public static <T> BackendObject<List<T>> getListFromBackend(String url, ParameterizedTypeReference<List<T>> typeRef) throws RestClientException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<T>> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeRef);

            return generateObject(response);
        }
        catch (Exception e) {
            return new BackendObject<>(null, HttpStatus.SERVICE_UNAVAILABLE, null);
        }
    }

    private static <T> BackendObject<T> generateObject(ResponseEntity<T> respuesta) {
        HttpStatusCode statusCode = respuesta.getStatusCode();
        HttpHeaders headers = respuesta.getHeaders();
        T cuerpo = respuesta.getBody();
        return new BackendObject<>(headers, statusCode, cuerpo);
    }
}
