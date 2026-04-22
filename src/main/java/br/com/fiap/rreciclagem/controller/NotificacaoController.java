package br.com.fiap.rreciclagem.controller;

import br.com.fiap.rreciclagem.dto.NotificacaoRequest;
import br.com.fiap.rreciclagem.exception.ResourceNotFoundException;
import br.com.fiap.rreciclagem.model.Notificacao;
import br.com.fiap.rreciclagem.model.Usuario;
import br.com.fiap.rreciclagem.repository.NotificacaoRepository;
import br.com.fiap.rreciclagem.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Endpoint protegido para o usuário logado ver suas notificações.
     * (Feature: Notificações para usuários sobre a destinação correta)
     * GET /api/notificacoes/minhas-notificacoes
     */
    @GetMapping("/minhas-notificacoes")
    public ResponseEntity<List<Notificacao>> getMinhasNotificacoes(
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        List<Notificacao> notificacoes = notificacaoRepository.findByUsuarioId(usuario.getId());
        return ResponseEntity.ok(notificacoes);
    }

    /**
     * Endpoint protegido (ADMIN) para enviar uma notificação (dica) para um usuário.
     * POST /api/notificacoes/admin/enviar
     */
    @PostMapping("/admin/enviar")
    // @PreAuthorize("hasRole('ADMIN')") // Já protegido no SecurityConfig, mas boa prática
    public ResponseEntity<Notificacao> enviarNotificacaoAdmin(
            @Valid @RequestBody NotificacaoRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Notificacao notificacao = Notificacao.builder()
                .titulo(request.getTitulo())
                .mensagem(request.getMensagem())
                .usuario(usuario)
                .dataEnvio(LocalDateTime.now())
                .build();

        Notificacao salva = notificacaoRepository.save(notificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }
}
