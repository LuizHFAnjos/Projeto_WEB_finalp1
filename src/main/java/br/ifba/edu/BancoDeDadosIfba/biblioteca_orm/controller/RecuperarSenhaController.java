package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.controller;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.DTO.EmailRecuperacaoDTO;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.DTO.SenhaNovaDTO;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.service.RecuperarSenhaService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recuperar")
public class RecuperarSenhaController {

    @Autowired
    private RecuperarSenhaService recuperarSenhaService;

 //EndPoint para enviar o código
 @PostMapping("/enviarCodigo")
 private ResponseEntity<String> enviarCodigo(@RequestBody EmailRecuperacaoDTO emailRecuperacaoDTO){
     recuperarSenhaService.enviarCodigo(emailRecuperacaoDTO);
     return ResponseEntity.ok("Código enviado!");
 }



}
