package com.botquest.BotQuestAPI.controllers;

import com.botquest.BotQuestAPI.dtos.ChamadoDto;
import com.botquest.BotQuestAPI.models.ChamadoModel;
import com.botquest.BotQuestAPI.repositories.ChamadoRepository;
import com.botquest.BotQuestAPI.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Define o controlador para a entidade ChamadoModel
@RestController
@RequestMapping(value = "/chamado", produces = {"application/json"})
public class ChamadoController {
    // Injeta a dependência do repositório ChamadoRepository
    @Autowired
    ChamadoRepository chamadoRepository;

    // Injeta a dependência do repositório UsuarioRepository
    @Autowired
    UsuarioRepository usuarioRepository;

    // Endpoint para listar todos os chamados
    @GetMapping
    public ResponseEntity<List<ChamadoModel>> listarChamados() {
        // Recupera a lista de todos os chamados no banco de dados
        List<ChamadoModel> chamados = chamadoRepository.findAll();
        // Retorna a lista de chamados com o código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(chamados);
    }

    // Endpoint para buscar um chamado por ID
    @GetMapping("/{idChamado}")
    public ResponseEntity<Object> buscarChamado(@PathVariable(value = "idChamado") UUID id) {
        // Busca o chamado no banco de dados pelo ID fornecido
        Optional<ChamadoModel> chamadoBuscado = chamadoRepository.findById(id);

        // Verifica se o chamado foi encontrado
        if (chamadoBuscado.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se o chamado não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chamado não encontrado");
        }

        // Retorna o chamado encontrado com código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(chamadoBuscado.get());
    }

    // Endpoint para cadastrar um novo chamado
    @PostMapping
    public ResponseEntity<Object> cadastrarChamado(@RequestBody @Valid ChamadoDto chamadoDto) {
        // Cria uma nova instância de ChamadoModel
        ChamadoModel chamado = new ChamadoModel();

        // Copia as propriedades do DTO para o modelo
        BeanUtils.copyProperties(chamadoDto, chamado);

        // Busca o usuário no banco de dados pelo ID fornecido no DTO
        var usuario = usuarioRepository.findById(chamadoDto.id_usuario());

        // Verifica se o usuário foi encontrado
        if (usuario.isPresent()) {
            // Define o usuário do chamado
            chamado.setUsuario(usuario.get());
        } else {
            // Retorna uma resposta com código de status BAD_REQUEST se o usuário não foi encontrado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id_usuario não encontrado");
        }

        // Salva o chamado no banco de dados e retorna com código de status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(chamadoRepository.save(chamado));
    }

    // Endpoint para editar um chamado por ID
    @PutMapping("/{idChamado}")
    public ResponseEntity<Object> editarChamado(@PathVariable(value = "idChamado") UUID id, @RequestBody @Valid ChamadoDto chamadoDto) {
        // Busca o chamado no banco de dados pelo ID fornecido
        Optional<ChamadoModel> chamadoBuscado = chamadoRepository.findById(id);

        // Verifica se o chamado foi encontrado
        if (chamadoBuscado.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se o chamado não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chamado não encontrado");
        }

        // Obtém o chamado encontrado
        ChamadoModel chamado = chamadoBuscado.get();

        // Verifica se o ID do usuário foi fornecido no DTO
        if (chamadoDto.id_usuario() != null) {
            // Busca o usuário no banco de dados pelo ID fornecido no DTO
            var usuarioOptional = usuarioRepository.findById(chamadoDto.id_usuario());

            // Verifica se o usuário foi encontrado
            if (usuarioOptional.isPresent()) {
                // Define o usuário do chamado
                chamado.setUsuario(usuarioOptional.get());
            } else {
                // Retorna uma resposta com código de status BAD_REQUEST se o usuário não foi encontrado
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id_usuario não encontrado");
            }
        }

        // Copia as propriedades do DTO para o chamado
        BeanUtils.copyProperties(chamadoDto, chamado);

        // Salva o chamado atualizado no banco de dados e retorna com código de status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(chamadoRepository.save(chamado));
    }

    // Endpoint para deletar um chamado por ID
    @DeleteMapping("/{idChamado}")
    public ResponseEntity<Object> deletarChamado(@PathVariable(value = "idChamado") UUID id) {
        // Busca o chamado no banco de dados pelo ID fornecido
        Optional<ChamadoModel> chamadoBuscado = chamadoRepository.findById(id);

        // Verifica se o chamado foi encontrado
        if (chamadoBuscado.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se o chamado não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chamado não encontrado");
        }

        // Remove o chamado do banco de dados e retorna com código de status NO_CONTENT
        chamadoRepository.delete(chamadoBuscado.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Chamado deletado!");
    }
}
