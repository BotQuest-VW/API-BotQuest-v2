package com.botquest.BotQuestAPI.controllers;

import com.botquest.BotQuestAPI.dtos.PerguntaDto;
import com.botquest.BotQuestAPI.dtos.SetorDto;
import com.botquest.BotQuestAPI.models.PerguntaModel;
import com.botquest.BotQuestAPI.models.SetorModel;
import com.botquest.BotQuestAPI.repositories.PerguntaRepository;
import com.botquest.BotQuestAPI.repositories.SetorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Define o controlador para a entidade PerguntaModel
@RestController
@RequestMapping(value = "/pergunta", produces = {"application/json"})
public class PerguntaController {
    // Injeta a dependência do repositório PerguntaRepository
    @Autowired
    PerguntaRepository perguntaRepository;

    // Injeta a dependência do repositório SetorRepository
    @Autowired
    private SetorRepository setorRepository;

    // Endpoint para listar todas as perguntas
    @GetMapping
    public ResponseEntity<List<PerguntaModel>> listarPerguntas() {
        // Retorna a lista de perguntas com o código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(perguntaRepository.findAll());
    }

    // Endpoint para buscar uma pergunta por ID
    @GetMapping("/{idPergunta}")
    public ResponseEntity<Object> buscarPergunta(@PathVariable(value = "idPergunta") UUID id) {
        // Busca a pergunta no banco de dados pelo ID fornecido
        Optional<PerguntaModel> perguntaBuscada = perguntaRepository.findById(id);

        // Verifica se a pergunta foi encontrada
        if (perguntaBuscada.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se a pergunta não foi encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pergunta não encontrada");
        }

        // Retorna a pergunta encontrada com código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(perguntaBuscada.get());
    }

    // Endpoint para cadastrar uma nova pergunta
    @PostMapping
    public ResponseEntity<Object> cadastrarPergunta(@RequestBody @Valid PerguntaDto perguntaDto) {
        // Cria uma nova instância de PerguntaModel
        PerguntaModel pergunta = new PerguntaModel();

        // Copia as propriedades do DTO para o modelo
        BeanUtils.copyProperties(perguntaDto, pergunta);

        // Busca o setor no banco de dados pelo ID fornecido no DTO
        var setor = setorRepository.findById(perguntaDto.id_setor());

        // Verifica se o setor foi encontrado
        if (setor.isPresent()) {
            // Define o setor da pergunta
            pergunta.setSetorModel(setor.get());
        } else {
            // Retorna uma resposta com código de status BAD_REQUEST se o setor não foi encontrado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id_setor não encontrado");
        }

        // Salva a pergunta no banco de dados e retorna com código de status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(perguntaRepository.save(pergunta));
    }

    // Endpoint para editar uma pergunta por ID
    @PutMapping("/{idPergunta}")
    public ResponseEntity<Object> editarPergunta(@PathVariable(value = "idPergunta") UUID id, @RequestBody @Valid PerguntaDto perguntaDto) {
        // Busca a pergunta no banco de dados pelo ID fornecido
        Optional<PerguntaModel> perguntaBuscada = perguntaRepository.findById(id);

        // Verifica se a pergunta foi encontrada
        if (perguntaBuscada.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se a pergunta não foi encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pergunta não encontrada");
        }

        // Obtém a pergunta encontrada
        PerguntaModel pergunta = perguntaBuscada.get();

        // Verifica se o ID do setor foi fornecido no DTO
        if (perguntaDto.id_setor() != null) {
            // Busca o setor no banco de dados pelo ID fornecido no DTO
            var setorOptional = setorRepository.findById(perguntaDto.id_setor());

            // Verifica se o setor foi encontrado
            if (setorOptional.isPresent()) {
                // Define o setor da pergunta
                pergunta.setSetorModel(setorOptional.get());
            } else {
                // Retorna uma resposta com código de status BAD_REQUEST se o setor não foi encontrado
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id_setor não encontrado");
            }
        }

        // Copia as propriedades do DTO para a pergunta
        BeanUtils.copyProperties(perguntaDto, pergunta);

        // Salva a pergunta atualizada no banco de dados e retorna com código de status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(perguntaRepository.save(pergunta));
    }

    // Endpoint para deletar uma pergunta por ID
    @DeleteMapping("/{idPergunta}")
    public ResponseEntity<Object> deletarPergunta(@PathVariable(value = "idPergunta") UUID id) {
        // Busca a pergunta no banco de dados pelo ID fornecido
        Optional<PerguntaModel> perguntaBuscada = perguntaRepository.findById(id);

        // Verifica se a pergunta foi encontrada
        if (perguntaBuscada.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se a pergunta não foi encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pergunta não encontrada");
        }

        // Remove a pergunta do banco de dados e retorna com código de status NO_CONTENT
        perguntaRepository.delete(perguntaBuscada.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pergunta deletada!");
    }
}

