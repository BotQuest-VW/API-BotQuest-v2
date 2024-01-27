package com.botquest.BotQuestAPI.models;// Importa as anotações necessárias do JPA e Lombok
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

// Marca a classe como uma entidade JPA e implementa a interface Serializable
@Getter
@Setter
@Entity
// Especifica o nome da tabela no banco de dados
@Table(name = "tb_setor")
public class SetorModel implements Serializable {
    // Define um número de série para garantir consistência durante a serialização
    @Serial
    private static final long serialVersionUID = 1L;

    // Define a chave primária da entidade
    @Id
    // Configura a estratégia de geração automática do ID
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Especifica o nome da coluna no banco de dados
    @Column(name = "id", nullable = false)
    private UUID id;

    // Atributos da entidade
    int cod_setor;
    String nome;
}
