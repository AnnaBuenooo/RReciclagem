package br.com.fiap.rreciclagem.controller;

import br.com.fiap.rreciclagem.dto.AgendamentoRequest;
import br.com.fiap.rreciclagem.exception.ResourceNotFoundException;
import br.com.fiap.rreciclagem.model.Agendamento;
import br.com.fiap.rreciclagem.model.Usuario;
import br.com.fiap.rreciclagem.repository.AgendamentoRepository;
import br.com.fiap.rreciclagem.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Endpoint protegido para criar um novo agendamento de coleta.
     * O usuário é pego do token JWT.
     * POST /api/agendamentos
     */
    @PostMapping
    public ResponseEntity<Agendamento> criarAgendamento(
            @Valid @RequestBody AgendamentoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Busca o usuário completo pelo email (username do UserDetails)
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Agendamento agendamento = Agendamento.builder()
                .dataAgendamento(request.getDataAgendamento())
                .tipoResiduo(request.getTipoResiduo())
                .status("PENDENTE")
                .usuario(usuario)
                .build();

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * Endpoint protegido para listar os agendamentos do usuário logado.
     * GET /api/agendamentos/meus-agendamentos
     */
    @GetMapping("/meus-agendamentos")
    public ResponseEntity<List<Agendamento>> meusAgendamentos(
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        List<Agendamento> agendamentos = agendamentoRepository.findByUsuarioId(usuario.getId());
        return ResponseEntity.ok(agendamentos);
    }
}