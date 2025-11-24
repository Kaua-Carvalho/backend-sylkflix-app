package com.sylkflix.controller;

import com.sylkflix.dto.FilmeAssistidoDTO;
import com.sylkflix.dto.FilmeAssistidoResponseDTO;
import com.sylkflix.service.FilmeAssistidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assistidos")
@Tag(name = "Filmes Assistidos", description = "Endpoints para gerenciar filmes assistidos do usuário")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FilmeAssistidoController {

    @Autowired
    private FilmeAssistidoService filmeAssistidoService;

    @PostMapping
    @Operation(summary = "Adicionar filme aos assistidos",
            description = "Adiciona um novo filme à lista de assistidos do usuário autenticado")
    public ResponseEntity<?> addAssistido(@Valid @RequestBody FilmeAssistidoDTO dto) {
        try {
            FilmeAssistidoResponseDTO response = filmeAssistidoService.addAssistido(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos os assistidos",
            description = "Retorna todos os filmes assistidos do usuário autenticado")
    public ResponseEntity<List<FilmeAssistidoResponseDTO>> getAllAssistidos() {
        List<FilmeAssistidoResponseDTO> assistidos = filmeAssistidoService.getAllAssistidos();
        return ResponseEntity.ok(assistidos);
    }

    @GetMapping("/check/{tmdbId}")
    @Operation(summary = "Verificar se filme é assistido",
            description = "Verifica se um filme específico está nos assistidos do usuário")
    public ResponseEntity<Map<String, Object>> isAssistido(@PathVariable Integer tmdbId) {
        try {
            FilmeAssistidoResponseDTO assistido = filmeAssistidoService.getAssistidoByTmdbId(tmdbId);
            Map<String, Object> response = new HashMap<>();
            response.put("isAssistido", true);
            response.put("assistidoId", assistido.getId());
            response.put("avaliacao", assistido.getAvaliacao());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("isAssistido", false);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/{id}/avaliacao")
    @Operation(summary = "Atualizar avaliação",
            description = "Atualiza a avaliação (1-5 estrelas) de um filme assistido")
    public ResponseEntity<?> updateAvaliacao(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> payload) {
        try {
            Integer novaAvaliacao = payload.get("avaliacao");
            FilmeAssistidoResponseDTO response = filmeAssistidoService.updateAvaliacao(id, novaAvaliacao);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover dos assistidos",
            description = "Remove um filme da lista de assistidos do usuário")
    public ResponseEntity<?> removeAssistido(@PathVariable Long id) {
        try {
            filmeAssistidoService.removeAssistido(id);
            return ResponseEntity.ok(Map.of("message", "Filme removido dos assistidos com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}