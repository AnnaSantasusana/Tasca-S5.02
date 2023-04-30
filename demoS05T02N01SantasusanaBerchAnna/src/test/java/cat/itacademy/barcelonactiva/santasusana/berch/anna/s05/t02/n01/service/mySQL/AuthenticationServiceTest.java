package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.service.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.Role;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.AuthenticationRequest;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.JwtService;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.auth.AuthenticationService;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.utils.HelperSql;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private IPlayerSqlRepo playerSqlRepo;
    @Mock
    private HelperSql helperSql;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @DisplayName("Test to register a player")
    @Test
    public void testRegisterSql() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Lluís")
                .email("llsc@gmail.com")
                .password("12345")
                .build();

        PlayerSql expectedPlayer = PlayerSql.builder()
                .name("Lluís")
                .email("llsc@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        when(helperSql.verifyName(request.getName())).thenReturn("Lluís");
        when(helperSql.verifyEmail(request.getEmail())).thenReturn("llsc@gmail.com");
        when(passwordEncoder.encode(request.getPassword())).thenReturn("12345");
        when(jwtService.generateToken(expectedPlayer)).thenReturn("jwt_token");
        when(playerSqlRepo.save(any(PlayerSql.class))).thenReturn(expectedPlayer);

        AuthenticationResponse response = authenticationService.registerSql(request);

        assertEquals("jwt_token", response.getToken());
    }

    @DisplayName("Test to authenticate a player")
    @Test
    public void testAuthenticateSql() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("llsc@gmail.com")
                .password("12345")
                .build();

        PlayerSql player = PlayerSql.builder()
                .id(1)
                .name("Lluís")
                .registrationDate(LocalDateTime.now())
                .games(new ArrayList<>())
                .email("llsc@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        when(playerSqlRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(player));
        when(jwtService.generateToken(player)).thenReturn("jwt_token");

        AuthenticationResponse response = authenticationService.authenticateSql(request);

        assertEquals("jwt_token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

    }
}
