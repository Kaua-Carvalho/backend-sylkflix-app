package com.sylkflix.controller;

import com.sylkflix.dto.AuthRequestDTO;
import com.sylkflix.dto.AuthResponseDTO;
import com.sylkflix.dto.RegisterRequestDTO;
import com.sylkflix.dto.UpdateNomeDTO;
import com.sylkflix.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário e retorna o token JWT")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerDTO) {
        try {
            AuthResponseDTO response = authService.register(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login",
            description = "Autentica um usuário e retorna o token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO loginDTO) {
        try {
            AuthResponseDTO response = authService.login(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PutMapping("/update-nome")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar nome do usuário",
            description = "Atualiza o nome de exibição do usuário autenticado")
    public ResponseEntity<?> updateNome(@Valid @RequestBody UpdateNomeDTO updateNomeDTO) {
        try {
            AuthResponseDTO response = authService.updateNome(updateNomeDTO.getNome());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/delete-account")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deletar conta",
            description = "Remove permanentemente a conta do usuário e todos os seus dados")
    public ResponseEntity<?> deleteAccount() {
        try {
            authService.deleteAccount();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Conta deletada com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/profile-picture")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar foto de perfil",
            description = "Atualiza a foto de perfil do usuário autenticado")
    public ResponseEntity<?> updateProfilePicture(@RequestBody Map<String, String> body) {
        try {
            String profilePicture = body.get("profilePicture"); // Ex: "Profile1"
            AuthResponseDTO response = authService.updateProfilePicture(profilePicture);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}