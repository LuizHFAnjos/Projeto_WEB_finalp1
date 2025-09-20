package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.mapper.LivroMapper;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // ✅ IMPORT NECESSÁRIO PARA LOG
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // ✅ ANOTAÇÃO PARA HABILITAR O LOG
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final UsuarioRepository usuarioRepository;

    public void salvar(LivroDTO dto) {
        log.info("Iniciando processo para salvar livro: {}", dto.getNome());

        // Validações existentes...
        if (dto.getId() == null) {
            if (livroRepository.existsByNome(dto.getNome())) {
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        } else {
            if (livroRepository.existsByNomeAndIdNot(dto.getNome(), dto.getId())) {
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        }
        int anoAtual = LocalDate.now().getYear();
        if (dto.getAnoPublicacao() < 1500 || dto.getAnoPublicacao() > anoAtual) {
            throw new AnoPublicacaoInvalidoException("Ano de publicação inválido!");
        }

        LivroEntity entity = livroMapper.toEntity(dto);

        // Início da Lógica de Auditoria
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Usuário autenticado encontrado: {}", adminEmail); // ✅ DEBUG LOG

        Usuario admin = usuarioRepository.findByEmail(adminEmail)
                .orElseThrow(() -> {
                    log.error("FALHA NA AUDITORIA: Admin com e-mail {} não foi encontrado no banco de dados.", adminEmail); // ✅ LOG DE ERRO
                    return new UsernameNotFoundException("Admin não encontrado para o e-mail: " + adminEmail);
                });

        entity.setPublicadoPor(admin);
        log.info("Associando livro '{}' ao admin '{}' (ID: {})", entity.getNome(), admin.getNome(), admin.getId()); // ✅ DEBUG LOG

        livroRepository.save(entity);
        log.info("Livro '{}' salvo com sucesso no banco de dados.", entity.getNome());
    }

    // ... resto dos métodos (listar, deletar, etc.) permanecem iguais
    public List<LivroDTO> listar() {
        return livroRepository.findAll().stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }

    public List<LivroDTO> buscarPorNomeOuAutor(String query) {
        return livroRepository.findByNomeContainingIgnoreCaseOrAutorContainingIgnoreCase(query, query)
                .stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LivroDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }
}