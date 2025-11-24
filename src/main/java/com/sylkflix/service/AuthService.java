package com.sylkflix.service;

import com.sylkflix.dto.AuthRequestDTO;
import com.sylkflix.dto.AuthResponseDTO;
import com.sylkflix.dto.RegisterRequestDTO;
import com.sylkflix.model.Usuario;
import com.sylkflix.repository.UsuarioRepository;
import com.sylkflix.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO registerDTO) {
        // ✅ Verificar se email já existe
        if (usuarioRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Este email já está cadastrado!");
        }

        // Criar novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(registerDTO.getNome());
        usuario.setEmail(registerDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(registerDTO.getSenha()));

        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Gerar token JWT
        String token = tokenProvider.generateTokenFromEmail(savedUsuario.getEmail());

        return new AuthResponseDTO(
                token,
                savedUsuario.getId(),
                savedUsuario.getNome(),
                savedUsuario.getEmail(),
                savedUsuario.getProfilePicture()
        );
    }

    public AuthResponseDTO login(AuthRequestDTO loginDTO) {
        // Autenticar usuário
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getSenha()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gerar token JWT
        String token = tokenProvider.generateToken(authentication);

        // Buscar dados do usuário
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new AuthResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getProfilePicture()
        );
    }

    // ✅ NOVO: Atualizar nome do usuário
    @Transactional
    public AuthResponseDTO updateNome(String novoNome) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new RuntimeException("Nome não pode ser vazio");
        }

        if (novoNome.length() < 2 || novoNome.length() > 100) {
            throw new RuntimeException("Nome deve ter entre 2 e 100 caracteres");
        }

        usuario.setNome(novoNome.trim());
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        // Gerar novo token com informações atualizadas
        String token = tokenProvider.generateTokenFromEmail(updatedUsuario.getEmail());

        return new AuthResponseDTO(
                token,
                updatedUsuario.getId(),
                updatedUsuario.getNome(),
                updatedUsuario.getEmail(),
                updatedUsuario.getProfilePicture()
        );
    }

    @Transactional
    public AuthResponseDTO updateProfilePicture(String profilePicture) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setProfilePicture(profilePicture);
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        String token = tokenProvider.generateTokenFromEmail(updatedUsuario.getEmail());

        return new AuthResponseDTO(
                token,
                updatedUsuario.getId(),
                updatedUsuario.getNome(),
                updatedUsuario.getEmail(),
                updatedUsuario.getProfilePicture()
        );
    }

    // ✅ NOVO: Deletar conta (cascata automática deleta assistidos)
    @Transactional
    public void deleteAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // O CascadeType.ALL na entidade Usuario vai deletar os assistidos automaticamente
        usuarioRepository.delete(usuario);
    }
}