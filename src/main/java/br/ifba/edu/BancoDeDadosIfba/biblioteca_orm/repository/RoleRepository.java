package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.repository;

import br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
