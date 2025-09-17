package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.controller;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LivroService livroService;

    @GetMapping({"/", "/home"}) // Responde tanto para a raiz quanto para /home
    public String exibirHome(Model model) {
        // Busca a lista de todos os livros através do service
        model.addAttribute("livrosRecentes", livroService.listar());
        // Você pode adicionar lógicas diferentes para cada carrossel
        model.addAttribute("livrosEmAlta", livroService.listar()); 
        
        // Retorna o nome do arquivo HTML: "home.html"
        return "home";
    }
}