package com.sylkflix.service;

import com.sylkflix.dto.FilmeAssistidoDTO;
import com.sylkflix.dto.FilmeAssistidoResponseDTO;
import com.sylkflix.model.FilmeAssistido;
import com.sylkflix.model.Usuario;
import com.sylkflix.repository.FilmeAssistidoRepository;
import com.sylkflix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmeAssistidoService {

    @Autowired
    private FilmeAssistidoRepository filmeAssistidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private FilmeAssistidoResponseDTO toResponseDTO(FilmeAssistido filme) {
        return new FilmeAssistidoResponseDTO(
                filme.getId(),
                filme.getTmdbId(),
                filme.getTitulo(),
                filme.getPosterPath(),
                filme.getAvaliacao(),
                filme.getDataAdicionado()
        );
    }

    @Transactional
    public FilmeAssistidoResponseDTO addAssistido(FilmeAssistidoDTO dto) {
        Usuario usuario = getAuthenticatedUser();

        if (filmeAssistidoRepository.existsByUsuarioAndTmdbId(usuario, dto.getTmdbId())) {
            throw new RuntimeException("Filme já está nos assistidos!");
        }

        FilmeAssistido filme = new FilmeAssistido(
                usuario,
                dto.getTmdbId(),
                dto.getTitulo(),
                dto.getPosterPath(),
                dto.getAvaliacao()
        );

        FilmeAssistido savedFilme = filmeAssistidoRepository.save(filme);
        return toResponseDTO(savedFilme);
    }

    public List<FilmeAssistidoResponseDTO> getAllAssistidos() {
        Usuario usuario = getAuthenticatedUser();
        List<FilmeAssistido> filmes = filmeAssistidoRepository.findByUsuarioOrderByDataAdicionadoDesc(usuario);

        return filmes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FilmeAssistidoResponseDTO updateAvaliacao(Long id, Integer novaAvaliacao) {
        Usuario usuario = getAuthenticatedUser();

        FilmeAssistido filme = filmeAssistidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme assistido não encontrado"));

        if (!filme.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este filme");
        }

        if (novaAvaliacao < 1 || novaAvaliacao > 5) {
            throw new RuntimeException("Avaliação deve estar entre 1 e 5");
        }

        filme.setAvaliacao(novaAvaliacao);
        FilmeAssistido updatedFilme = filmeAssistidoRepository.save(filme);

        return toResponseDTO(updatedFilme);
    }

    @Transactional
    public void removeAssistido(Long id) {
        Usuario usuario = getAuthenticatedUser();

        FilmeAssistido filme = filmeAssistidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme assistido não encontrado"));

        if (!filme.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para remover este filme");
        }

        filmeAssistidoRepository.delete(filme);
    }

    public boolean isAssistido(Integer tmdbId) {
        Usuario usuario = getAuthenticatedUser();
        return filmeAssistidoRepository.existsByUsuarioAndTmdbId(usuario, tmdbId);
    }

    public FilmeAssistidoResponseDTO getAssistidoByTmdbId(Integer tmdbId) {
        Usuario usuario = getAuthenticatedUser();
        FilmeAssistido filme = filmeAssistidoRepository.findByUsuarioAndTmdbId(usuario, tmdbId)
                .orElseThrow(() -> new RuntimeException("Filme não está nos assistidos"));
        return toResponseDTO(filme);
    }
}