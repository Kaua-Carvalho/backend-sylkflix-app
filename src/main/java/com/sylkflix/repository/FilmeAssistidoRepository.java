package com.sylkflix.repository;

import com.sylkflix.model.FilmeAssistido;
import com.sylkflix.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmeAssistidoRepository extends JpaRepository<FilmeAssistido, Long> {

    List<FilmeAssistido> findByUsuarioOrderByDataAdicionadoDesc(Usuario usuario);

    Optional<FilmeAssistido> findByUsuarioAndTmdbId(Usuario usuario, Integer tmdbId);

    Boolean existsByUsuarioAndTmdbId(Usuario usuario, Integer tmdbId);

    void deleteByUsuarioAndTmdbId(Usuario usuario, Integer tmdbId);
}