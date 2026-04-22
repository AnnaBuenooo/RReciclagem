package br.com.fiap.rreciclagem.service;

import br.com.fiap.rreciclagem.dto.ContainerNivelRequest;
import br.com.fiap.rreciclagem.exception.ResourceNotFoundException;
import br.com.fiap.rreciclagem.model.Container;
import br.com.fiap.rreciclagem.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository containerRepository;
    // Em um app real, injetaríamos um NotificacaoService para disparar o alerta
    // private final NotificacaoService notificacaoService;

    @Transactional
    public Container atualizarNivel(Long id, ContainerNivelRequest request) {
        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Container não encontrado com id: " + id));

        container.setNivelAtual(request.getNivelAtual());

        // Lógica de Negócio: MVP do "Alerta automático"
        // Se o nível atual for >= 90% da capacidade, marca como "CHEIO"
        double limite = container.getCapacidadeMaxima() * 0.9;

        if (container.getNivelAtual() >= limite) {
            if (!"CHEIO".equals(container.getStatusColeta())) {
                container.setStatusColeta("CHEIO");
                // Aqui seria o ponto de disparo do alerta
                // Ex: notificacaoService.dispararAlertaColeta(container);
                System.out.println("ALERTA: Container " + id + " atingiu o limite. Coleta necessária.");
            }
        } else if (container.getNivelAtual() <= (container.getCapacidadeMaxima() * 0.1)) {
            container.setStatusColeta("VAZIO");
        } else {
            container.setStatusColeta("ENCHENDO");
        }

        return containerRepository.save(container);
    }

    // Endpoint para um sistema de dashboard de coleta
    public List<Container> getContainersProntosParaColeta() {
        return containerRepository.findContainersAtingindoLimite();
    }
}