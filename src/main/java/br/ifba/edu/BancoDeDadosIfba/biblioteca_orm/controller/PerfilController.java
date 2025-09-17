package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.controller;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.entities.Usuario;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importe o Model
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class PerfilController {

    private final UsuarioRepository usuarioRepository;

    // Injeção de dependência do repositório
    public PerfilController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ... (seu método @GetMapping("/meu-perfil") continua o mesmo) ...
    @GetMapping("/meu-perfil")
    public String redirecionarPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (isAdmin) {
            return "redirect:/perfil-admin";
        } else {
            return "redirect:/perfil-usuario";
        }
    }


    // MÉTODO ATUALIZADO
    @GetMapping("/perfil-admin")
    public String paginaPerfilAdmin(Model model) {
        // Pega o email do usuário logado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário no banco
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // Adiciona o objeto usuário ao modelo
        model.addAttribute("usuario", usuario);
        return "perfil-admin";
    }

    // MÉTODO ATUALIZADO
    @GetMapping("/perfil-usuario")
    public String paginaPerfilUsuario(Model model) {
        // Pega o email do usuário logado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário no banco
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // Adiciona o objeto usuário ao modelo
        model.addAttribute("usuario", usuario);
        return "perfil-usuario";
    }
}