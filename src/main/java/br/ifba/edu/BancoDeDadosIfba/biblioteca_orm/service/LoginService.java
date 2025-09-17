package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.service;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.DTO.LoginRequest;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.entities.Usuario;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.excecao.LoginIncorretoException;
import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importe esta classe
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injete o PasswordEncoder

    // Autenticação do usuário
    public Usuario authenticate (LoginRequest request){

        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                // Lança a exceção se o e-mail não for encontrado
                .orElseThrow(LoginIncorretoException::new);

        // **AQUI ESTÁ A CORREÇÃO PRINCIPAL**
        // Usa o passwordEncoder para verificar se a senha digitada corresponde à senha
        // criptografada no banco de dados.
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new LoginIncorretoException();
        }

        // Verificar na autenticação se usuário é admin ou user.
        boolean isAdmin = usuario.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));

        if(isAdmin){
            System.out.println("Admin logado!");
        }else{
            System.out.println("User logado!");
        }

        return usuario;
    }
}