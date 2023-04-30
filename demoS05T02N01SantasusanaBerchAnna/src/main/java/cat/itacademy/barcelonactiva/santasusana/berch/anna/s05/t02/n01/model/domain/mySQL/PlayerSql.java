package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * La implementación de la interfaz UserDetails en la clase PlayerSql permite que los objetos de tipo PlayerSql se utilicen directamente
 * en la configuración de Spring Security para autenticar y autorizar a los usuarios de la aplicación. Además, al proporcionar métodos
 * para obtener el nombre de usuario, la contraseña y los roles del usuario, se asegura que Spring Security tenga la información necesaria
 * para autenticar correctamente al usuario.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "players")
@Entity
public class PlayerSql implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @CreationTimestamp
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameSql> games = new ArrayList<>();
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    public PlayerSql(String name) {
        this.name = name;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
