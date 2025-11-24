package com.sylkflix.dto;

public class AuthResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String nome;
    private String email;
    private String profilePicture;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String tipo, Long id, String nome, String email) {
        this.token = token;
        this.tipo = tipo;
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public AuthResponseDTO(String token, Long id, String nome, String email, String profilePicture) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}