package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.controllers.auth;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.AuthenticationRequest;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.auth.RegisterRequest;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerSql(@RequestBody RegisterRequest request) throws Exception {
        try {
            return ResponseEntity.ok(service.registerSql(request));
        } catch (Exception e) {
            throw new Exception("Error while registering the player", e);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateSql(@RequestBody AuthenticationRequest request) throws Exception {
        try {
            return ResponseEntity.ok(service.authenticateSql(request));
        } catch (PlayerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while authenticating the player", e);
        }
    }

}

