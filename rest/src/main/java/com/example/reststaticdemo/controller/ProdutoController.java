package com.example.reststaticdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private Map<Integer, String> produtos = new HashMap<>();
    private int contador = 1;

    @PostMapping
    public ResponseEntity<String> criarProduto(@RequestBody String nome) {
        produtos.put(contador, nome);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Produto criado com ID: " + contador++);
    }

    @GetMapping
    public ResponseEntity<Map<Integer, String>> listarProdutos() {
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> buscarPorId(@PathVariable int id) {
        if (produtos.containsKey(id)) {
            return ResponseEntity.ok(produtos.get(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<String>> filtrarPorNome(@RequestParam String nome) {
        List<String> resultado = new ArrayList<>();
        for (String p : produtos.values()) {
            if (p.toLowerCase().contains(nome.toLowerCase())) {
                resultado.add(p);
            }
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable int id) {
        if (produtos.containsKey(id)) {
            produtos.remove(id);
            return ResponseEntity.ok("Produto removido com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }

}
