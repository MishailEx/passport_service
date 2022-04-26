package ru.job4j.passport.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.passport.service.model.Passport;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class PassportController {

    @Autowired
    private RestTemplate rest;

    @Value("${api.url}")
    private String api;

    @Value("${api.id.url}")
    private String apiId;

    @GetMapping("/find")
    public List<Passport> findAll(@RequestParam(required = false) String serial) {
        List<Passport> rsl = new ArrayList<>();
        if (serial != null) {
            rsl = rest.exchange(
                    api + "find/?serial=" + serial,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Passport>>() {
                    }
            ).getBody();
        } else {
            rsl = rest.exchange(
                    api + "find",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Passport>>() {
                    }
            ).getBody();
        }
        return rsl;
    }

    @PostMapping("/")
    public ResponseEntity<Passport> create(@RequestBody Passport passport) {
        Passport rsl = rest.postForObject(api, passport, Passport.class);
        return new ResponseEntity<>(
                rsl,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Passport passport) {
        rest.put(api, passport);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        rest.delete(apiId + id, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unavaliabe")
    public List<Passport> unavaliabe() {
        return rest.exchange(
                api + "unavaliabe",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
    }

    @GetMapping("/find-replaceable")
    public List<Passport> findReplaceable() {
        return rest.exchange(
                api + "find-replaceable",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
    }
}
