package com.mityanin.workers.service.impl;

import com.mityanin.workers.service.RestService;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@AllArgsConstructor
public class DefaultRestService implements RestService {

    private final RestTemplate restTemplate;

    @Override
    public int getResponseCode(URI uri) {
        return restTemplate.getForEntity(uri, String.class).getStatusCodeValue();
    }
}


