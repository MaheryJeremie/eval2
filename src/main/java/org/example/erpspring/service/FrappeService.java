package org.example.erpspring.service;


import org.example.erpspring.model.FrappeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

@Service
public class FrappeService {
    @Autowired
    private RestTemplate restTemplate;
    private final String API_KEY = "your_api_key";
    private final String API_SECRET = "your_api_secret";
    private final String BASE_URL = "http://erpnext.localhost:8000/api";
    public List<Map<String,Object>> getAllEmployees(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + "80d965d9662eb50" + ":" + "53ea0bae5ec88c7");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<FrappeResponse> response = restTemplate.exchange(
                BASE_URL + "/resource/Olona?fields=[\"nom\", \"age\"]",
                HttpMethod.GET,
                entity,
                FrappeResponse.class
        );
        return response.getBody().getData();
    }

}