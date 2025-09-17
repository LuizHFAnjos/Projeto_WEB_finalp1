package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.controller;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.DTO.LivroDTO;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroApiController {

    private final LivroService livroService;

    @GetMapping("/search")
    public List<LivroDTO> buscarLivros(@RequestParam("nome") String nome) {
        return livroService.buscarPorNome(nome);
    }
}