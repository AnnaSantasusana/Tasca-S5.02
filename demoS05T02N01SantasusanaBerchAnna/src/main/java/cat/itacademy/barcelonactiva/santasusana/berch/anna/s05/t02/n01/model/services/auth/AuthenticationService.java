package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.auth;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.Role;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.AuthenticationRequest;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.JwtService;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.utils.HelperSql;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IPlayerSqlRepo playerSqlRepo;
    private final HelperSql helperSql;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse registerSql(RegisterRequest request) {
       var player = PlayerSql.builder()
                .name(helperSql.verifyName(request.getName()))
                .email(helperSql.verifyEmail(request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        playerSqlRepo.save(player);
        var jwtToken = jwtService.generateToken(player);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateSql(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var player = playerSqlRepo.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(player);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

