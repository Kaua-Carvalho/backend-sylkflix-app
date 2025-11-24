package com.sylkflix.service;

import com.sylkflix.model.Usuario;
import com.sylkflix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Buscar usuário por email
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
    }

    // Buscar usuário por ID
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com id: " + id));
    }

    // Obter usuário autenticado
    public Usuario getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
    }

    // Verificar se email já existe
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Salvar usuário
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}