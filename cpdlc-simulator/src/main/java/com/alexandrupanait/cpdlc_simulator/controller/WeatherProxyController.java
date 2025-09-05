package com.alexandrupanait.cpdlc_simulator.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URL;

@RestController
@RequestMapping("/api/weather-tile")
public class WeatherProxyController {

    private static final String OPENWEATHER_API_KEY = "";

    @GetMapping("/{layer}/{z}/{x}/{y}.png")
    public ResponseEntity<InputStreamResource> getWeatherTile(
            @PathVariable String layer,
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y
    ) throws Exception {
        // Build the OpenWeatherMap tile URL
        String url = String.format(
            "https://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=%s",
            layer, z, x, y, OPENWEATHER_API_KEY
        );

        InputStream inputStream = new URL(url).openStream();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl("public, max-age=3600");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(inputStream));
    }
}