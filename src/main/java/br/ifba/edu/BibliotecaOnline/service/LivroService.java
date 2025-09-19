package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.mapper.LivroMapper;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    public void salvar(LivroDTO dto) {
        if(dto.getId() == null){
            //salvando um livro novo
            if(livroRepository.existsByNome(dto.getNome())){
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        } else {
            //editando um livro
            if(livroRepository.existsByNomeAndIdNot(dto.getNome(), dto.getId())){
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        }

        int anoAtual = LocalDate.now().getYear();
        if(dto.getAnoPublicacao() < 1500 || dto.getAnoPublicacao() > anoAtual ){
            throw new AnoPublicacaoInvalidoException("Ano de publicação inválido!");
        }

        LivroEntity entity = livroMapper.toEntity(dto);
        livroRepository.save(entity);
    }

    public List<LivroDTO> listar() {
        return livroRepository.findAll().stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }

    /**
     * Este é o novo método que será chamado pelo nosso Controller da API.
     * Ele usa o método do repositório para buscar por nome ou autor.
     * @param query O termo de busca digitado pelo usuário.
     * @return Uma lista de LivroDTOs que correspondem à busca.
     */
    public List<LivroDTO> buscarPorNomeOuAutor(String query) {
        return livroRepository.findByNomeContainingIgnoreCaseOrAutorContainingIgnoreCase(query, query)
                .stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }

    // CORREÇÃO: O método buscarPorId foi movido para DENTRO da classe
    public LivroDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

} // A chave '}' que fecha a classe LivroService fica aqui no final.