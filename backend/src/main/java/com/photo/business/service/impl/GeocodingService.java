package com.photo.business.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse?format=json&lat={lat}&lon={lon}";

    public String getLocation(Double latitude, Double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String location = "Unknown location";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(NOMINATIM_URL, Map.class, latitude, longitude);
            Map body = response.getBody();

            if (body != null) {
                Map address = (Map) body.get("address");
                if (address != null) {
                    StringBuilder locationBuilder = new StringBuilder();
                    if (address.containsKey("city")) {
                        locationBuilder.append(address.get("city"));
                    } else if (address.containsKey("village")) {
                        locationBuilder.append(address.get("village"));
                    } else if (address.containsKey("town")) {
                        locationBuilder.append(address.get("town"));
                    } else if (address.containsKey("hamlet")) {
                        locationBuilder.append(address.get("hamlet"));
                    } else if (address.containsKey("county")) {
                        locationBuilder.append(address.get("county"));
                    } else if (address.containsKey("state")) {
                        locationBuilder.append(address.get("state"));
                    }

                    if (locationBuilder.length() > 0) {
                        location = locationBuilder.toString();
                    }
                }
            }
        } catch (Exception e) {
        }

        return location;
    }
}
