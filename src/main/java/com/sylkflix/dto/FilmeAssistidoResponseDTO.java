package com.sylkflix.dto;

import java.time.LocalDateTime;

public class FilmeAssistidoResponseDTO {

    private Long id;
    private Integer tmdbId;
    private String titulo;
    private String posterPath;
    private Integer avaliacao;
    private LocalDateTime dataAdicionado;

    public FilmeAssistidoResponseDTO() {
    }

    public FilmeAssistidoResponseDTO(Long id, Integer tmdbId, String titulo, String posterPath, Integer avaliacao, LocalDateTime dataAdicionado) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.titulo = titulo;
        this.posterPath = posterPath;
        this.avaliacao = avaliacao;
        this.dataAdicionado = dataAdicionado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDateTime getDataAdicionado() {
        return dataAdicionado;
    }

    public void setDataAdicionado(LocalDateTime dataAdicionado) {
        this.dataAdicionado = dataAdicionado;
    }
}