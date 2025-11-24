package com.sylkflix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "filmes_assistidos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "tmdb_id"}))
public class FilmeAssistido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;

    @NotNull(message = "ID do filme (TMDB) é obrigatório")
    @Column(name = "tmdb_id", nullable = false)
    private Integer tmdbId;

    @NotNull(message = "Título é obrigatório")
    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(name = "poster_path", length = 255)
    private String posterPath;

    @Min(value = 1, message = "Avaliação mínima é 1")
    @Max(value = 5, message = "Avaliação máxima é 5")
    @Column(nullable = false)
    private Integer avaliacao;

    @CreationTimestamp
    @Column(name = "data_adicionado", nullable = false, updatable = false)
    private LocalDateTime dataAdicionado;

    // Construtores
    public FilmeAssistido() {
    }

    public FilmeAssistido(Long id, Usuario usuario, Integer tmdbId, String titulo, String posterPath, Integer avaliacao, LocalDateTime dataAdicionado) {
        this.id = id;
        this.usuario = usuario;
        this.tmdbId = tmdbId;
        this.titulo = titulo;
        this.posterPath = posterPath;
        this.avaliacao = avaliacao;
        this.dataAdicionado = dataAdicionado;
    }

    public FilmeAssistido(Usuario usuario, Integer tmdbId, String titulo, String posterPath, Integer avaliacao) {
        this.usuario = usuario;
        this.tmdbId = tmdbId;
        this.titulo = titulo;
        this.posterPath = posterPath;
        this.avaliacao = avaliacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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