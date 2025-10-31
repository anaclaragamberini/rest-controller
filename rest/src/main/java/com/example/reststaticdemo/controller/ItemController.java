package com.example.reststaticdemo.controller;

import com.example.reststaticdemo.model.Item;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final List<Item> items = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item newItem, UriComponentsBuilder uriBuilder) {
        long id = idCounter.getAndIncrement();
        newItem.setId(id);
        items.add(newItem);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/api/items/{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(newItem, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> found = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        return found
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchByName(@RequestParam(name = "name", required = false) String name) {
        List<Item> result;
        if (name == null || name.isBlank()) {
            result = items;
        } else {
            String q = name.toLowerCase();
            result = items.stream()
                    .filter(i -> i.getName() != null && i.getName().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean removed = items.removeIf(i -> i.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}