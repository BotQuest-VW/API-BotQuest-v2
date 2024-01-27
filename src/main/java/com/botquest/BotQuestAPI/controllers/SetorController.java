package com.botquest.BotQuestAPI.controllers;

import com.botquest.BotQuestAPI.dtos.SetorDto;
import com.botquest.BotQuestAPI.models.SetorModel;
import com.botquest.BotQuestAPI.models.TipoUsuarioModel;
import com.botquest.BotQuestAPI.repositories.SetorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Define o controlador para a entidade SetorModel
@RestController
@RequestMapping(value = "/setor", produces = {"application/json"})
public class SetorController {
    // Injeta a dependência do repositório SetorRepository
    @Autowired
    SetorRepository setorRepository;

    // Endpoint para listar todos os setores
    @GetMapping
    public ResponseEntity<List<SetorModel>> listarSetor() {
        // Recupera a lista de todos os setores no banco de dados
        List<SetorModel> setores = setorRepository.findAll();
        // Retorna a lista de setores com o código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(setores);
    }

    // Endpoint para buscar um setor por ID
    @GetMapping("/{idSetor}")
    public ResponseEntity<Object> buscarSetor(@PathVariable(value = "idSetor") UUID id){
        // Busca o setor no banco de dados pelo ID fornecido
        Optional<SetorModel> setorBuscado = setorRepository.findById(id);

        // Verifica se o setor foi encontrado
        if (setorBuscado.isEmpty()){
            // Retorna uma resposta com código de status NOT_FOUND se o setor não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este setor não existe");
        }

        // Retorna o setor encontrado com código de status OK
        return  ResponseEntity.status(HttpStatus.OK).body(setorBuscado.get());
    }

    // Endpoint para cadastrar um novo setor
    @PostMapping
    public ResponseEntity<Object> cadastrarSetor(@RequestBody @Valid SetorDto setorDto){
        // Cria uma nova instância de SetorModel
        SetorModel setor = new SetorModel();

        // Copia as propriedades do DTO para o modelo
        BeanUtils.copyProperties(setorDto, setor);

        // Salva o setor no banco de dados e retorna com código de status CREATED
        return  ResponseEntity.status(HttpStatus.CREATED).body(setorRepository.save(setor));
    }

    // Endpoint para editar um setor por ID
    @PutMapping(value = "/{idSetor}")
    public ResponseEntity<Object> editarSetor (@PathVariable(value = "idSetor") UUID id, @ModelAttribute @Valid SetorDto setorDto){
        // Busca o setor no banco de dados pelo ID fornecido
        Optional<SetorModel> setorBuscado = setorRepository.findById(id);

        // Verifica se o setor foi encontrado
        if(setorBuscado.isEmpty()){
            // Retorna uma resposta com código de status NOT_FOUND se o setor não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Setor não encontrado");
        }

        // Obtém o setor encontrado
        SetorModel setor = setorBuscado.get();

        // Copia as propriedades do DTO para o modelo
        BeanUtils.copyProperties(setorDto, setor);

        // Salva o setor atualizado no banco de dados e retorna com código de status CREATED
        return  ResponseEntity.status(HttpStatus.CREATED).body(setorRepository.save(setor));
    }

    // Endpoint para deletar um setor por ID
    @DeleteMapping("/{idSetor}")
    public ResponseEntity<Object> deletarSetor(@PathVariable(value = "idSetor") UUID id){
        // Busca o setor no banco de dados pelo ID fornecido
        Optional<SetorModel> setorBuscado = (setorRepository.findById(id));

        // Verifica se o setor foi encontrado
        if(setorBuscado.isEmpty()){
            // Retorna uma resposta com código de status NOT_FOUND se o setor não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Setor não encontrada");
        }

        // Remove o setor do banco de dados e retorna com código de status NO_CONTENT
        setorRepository.delete(setorBuscado.get());

        return  ResponseEntity.status(HttpStatus.NO_CONTENT).body("Setor deletado!");
    }
}
