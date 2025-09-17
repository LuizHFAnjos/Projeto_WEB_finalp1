package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.controller;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.DTO.UsuarioCadastroDTO;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.DTO.UsuarioRespostaDTO;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.excecao.EmailJaExisteException;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.service.CadastroService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService){
        this.cadastroService = cadastroService;
    }

    @PostMapping
    public ResponseEntity<?> insert(@Valid @RequestBody UsuarioCadastroDTO dto){
        // LOG PARA VERIFICAR SE O CONTROLLER FOI ACIONADO E QUAIS DADOS CHEGARAM
        System.out.println("--- Endpoint /api/cadastro acionado ---");
        System.out.println("Nome recebido: " + dto.getNome());
        System.out.println("Email recebido: " + dto.getEmail());
        
        try{
            UsuarioRespostaDTO usuarioResposta = cadastroService.insert(dto);
            System.out.println("--- Cadastro realizado com SUCESSO no service ---");
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResposta);
        }catch(EmailJaExisteException e){
            System.out.println("--- ERRO: E-mail já existe ---");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // CAPTURA GERAL DE ERROS
            System.err.println("!!! OCORREU UM ERRO INESPERADO NO CADASTRO !!!");
            e.printStackTrace(); // Isso vai imprimir o erro detalhado no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
}

