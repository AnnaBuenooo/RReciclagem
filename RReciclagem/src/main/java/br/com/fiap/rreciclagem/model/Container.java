package br.com.fiap.rreciclagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_CONTAINERS")
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private String tipoResiduo;

    @Column(nullable = false)
    private Double capacidadeMaxima;

    private Double nivelAtual;

    private String statusColeta; // VAZIO, ENCHENDO, CHEIO, AGUARDANDO_COLETA
}