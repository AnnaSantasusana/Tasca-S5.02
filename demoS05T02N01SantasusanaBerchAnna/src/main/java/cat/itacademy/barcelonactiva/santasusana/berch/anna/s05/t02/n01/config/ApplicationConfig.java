package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.config;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta clase es una configuración de Spring que define ciertos beans y componentes para la aplicación.
 */
@Configuration
@RequiredArgsConstructor
@ComponentScan("cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services")
public class ApplicationConfig {

    private final IPlayerSqlRepo playerSqlRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> playerSqlRepo.findByEmail(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player with this email address was not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
