package br.ifba.edu.BibliotecaOnline.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenhaNovaDTO {
    private String email;
    private String codigo;
    private String novaSenha;
}
