package com.botquest.BotQuestAPI.controllers;// Importa as anotações e classes necessárias

import com.botquest.BotQuestAPI.dtos.ImagemDto;
import com.botquest.BotQuestAPI.dtos.UsuarioDto;
import com.botquest.BotQuestAPI.models.ChamadoModel;
import com.botquest.BotQuestAPI.models.SetorModel;
import com.botquest.BotQuestAPI.models.UsuarioModel;
import com.botquest.BotQuestAPI.repositories.SetorRepository;
import com.botquest.BotQuestAPI.repositories.UsuarioRepository;

import com.botquest.BotQuestAPI.services.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Define o controlador para a entidade UsuarioModel
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/usuario", produces = {"application/json"})
public class UsuarioController {
    // Injeta a dependência do repositório UsuarioRepository
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SetorRepository setorRepository;

    @Autowired
    FileUploadService fileUploadService;

    // Endpoint para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        // Recupera a lista de todos os usuários no banco de dados
        // Retorna a lista de usuários com o código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    // Endpoint para buscar um usuário por ID
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        // Busca o usuário no banco de dados pelo ID fornecido
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        // Verifica se o usuário foi encontrado
        if (usuarioBuscado.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se o usuário não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
        }

        // Retorna o usuário encontrado com código de status OK
        return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado.get());
    }

    @GetMapping("/{idUsuario}/chamado")
    public ResponseEntity<Object> listarChamadosDoUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        UsuarioModel usuario = usuarioBuscado.get();
        List<ChamadoModel> chamados = usuario.getChamados();

        return ResponseEntity.status(HttpStatus.OK).body(chamados);
    }

    // Endpoint para cadastrar um novo usuário
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> cadastrarUsuario(@ModelAttribute @Valid UsuarioDto usuarioDto) {
        if(usuarioRepository.findByEmail(usuarioDto.email()) != null) {
            // Verifica se o email já está cadastrado e retorna uma resposta de erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esse email já está cadastrado");
        }

        // Cria uma nova instância de UsuarioModel
        UsuarioModel usuario = new UsuarioModel();

        // Copia as propriedades do DTO para o modelo
        BeanUtils.copyProperties(usuarioDto, usuario);

        String urlImagem;

        try{
            urlImagem = fileUploadService.fazerUpload(usuarioDto.url_img());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        usuario.setUrl_img(urlImagem);

        // Criptografa a senha
        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDto.senha());

        Optional<SetorModel> setorUsuario = setorRepository.findById(usuarioDto.id_setor());

        if (setorUsuario.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("setor não encontrado");

        usuario.setSetorModel(setorUsuario.get());

        // Define a senha criptografada no modelo de usuário
        usuario.setSenha(senhaCriptografada);

        // Salva o usuário no banco de dados e retorna com código de status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }

    // Endpoint para editar um usuário por ID
    @PutMapping(value = "/{idUsuario}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> editarUsuario(@PathVariable(value = "idUsuario") UUID id, @ModelAttribute @Valid UsuarioDto usuarioDto) {
        // Busca o usuário no banco de dados pelo ID fornecido
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        // Verifica se o usuário foi encontrado
        if (usuarioBuscado.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se o usuário não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
        }

        // Obtém o usuário encontrado
        UsuarioModel usuario = usuarioBuscado.get();

        // Copia as propriedades do DTO para o modelo
        BeanUtils.copyProperties(usuarioDto, usuario);

        String urlImagem;

        try{
            urlImagem = fileUploadService.fazerUpload(usuarioDto.url_img());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        usuario.setUrl_img(urlImagem);

        // Criptografa a senha
        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDto.senha());

        // Define a senha criptografada no modelo de usuário
        usuario.setSenha(senhaCriptografada);

        // Salva o usuário atualizado no banco de dados e retorna com código de status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }


    //método para editar apenas a imagem do usuário
    @PutMapping(value = "/editar-imagem/{idUsuario}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> editarImagemUsuario(@PathVariable(value = "idUsuario") UUID id, @RequestParam("url_img") MultipartFile file) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        UsuarioModel usuario = usuarioBuscado.get();
        String urlImagem;

        try {
            urlImagem = fileUploadService.fazerUpload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usuario.setUrl_img(urlImagem);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuario));
    }


    // Endpoint para deletar um usuário por ID
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        // Busca o usuário no banco de dados pelo ID fornecido
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        // Verifica se o usuário foi encontrado
        if (usuarioBuscado.isEmpty()) {
            // Retorna uma resposta com código de status NOT_FOUND se o usuário não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        // Remove o usuário do banco de dados e retorna com código de status NO_CONTENT
        usuarioRepository.delete(usuarioBuscado.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário deletado!");
    }
}


