package com.sylkflix.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FilmeAssistidoDTO {

    @NotNull(message = "ID do filme (TMDB) é obrigatório")
    private Integer tmdbId;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    private String posterPath;

    @NotNull(message = "Avaliação é obrigatória")
    @Min(value = 1, message = "Avaliação mínima é 1")
    @Max(value = 5, message = "Avaliação máxima é 5")
    private Integer avaliacao;

    public FilmeAssistidoDTO() {
    }

    public FilmeAssistidoDTO(Integer tmdbId, String titulo, String posterPath, Integer avaliacao) {
        this.tmdbId = tmdbId;
        this.titulo = titulo;
        this.posterPath = posterPath;
        this.avaliacao = avaliacao;
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
}
