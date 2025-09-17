package br.ifba.edu.BancoDeDadosIfba.biblioteca_orm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a recursos estáticos e páginas de login/cadastro
                .requestMatchers(
                    "/css/**", 
                    "/js/**", 
                    "/assets/**",
                    "/img/**",
                    "/api/cadastro", 
                    "/login"
                ).permitAll()
                
                // <-- REGRA DE SEGURANÇA CRÍTICA
                // Apenas usuários com a permissão 'ADMIN' podem acessar estas URLs.
                .requestMatchers("/admin/**", "/perfil-admin").hasRole("ADMIN")

                // <-- REGRA DE SEGURANÇA CRÍTICA
                // Apenas usuários com a permissão 'USER' podem acessar estas URLs.
                .requestMatchers("/perfil-usuario", "/favoritos").hasRole("USER")

                // Qualquer outra requisição precisa de autenticação (estar logado)
                .anyRequest().authenticated()

                
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/cadastro"));

        return http.build();
    }
}