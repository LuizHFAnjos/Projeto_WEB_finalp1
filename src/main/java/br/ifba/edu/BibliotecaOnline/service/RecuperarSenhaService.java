package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.EmailRecuperacaoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.SenhaNovaDTO;
import br.ifba.edu.BibliotecaOnline.entities.TokenSenha;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.TokenSenhaRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class RecuperarSenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenSenhaRepository tokenSenhaRepository;

    @Autowired
    private EmailService emailService;

    public void enviarCodigo (EmailRecuperacaoDTO emailDTO){
        //Verificar se o email existe
        Optional<Usuario> usuario = usuarioRepository.findByEmail(emailDTO.getEmail());

        //Extraindo usuario de optional para passar no set usuario
        Usuario u = usuario.get();

        //Criar o código de 6 digitos para ser enviado
        String codigo = String.format("%06d", new Random().nextInt(100000));

        //Atribuir o codigo ao atributo codigo e para o usuario da tabela token
        TokenSenha tokenSenha = new TokenSenha();
        tokenSenha.setCodigo(codigo);
        tokenSenha.setUsuario(u);
        tokenSenha.setExpiracao(LocalDateTime.now().plusMinutes(15));

        tokenSenhaRepository.save(tokenSenha);

        emailService.enviarEmail(usuario.get().getEmail(), "Recuperação de senha","Seu código de senha é " + codigo + "ele expira em 15 minutos");



        }



    private void recuperarSenha(SenhaNovaDTO senhaNovaDTO){

    }

}
