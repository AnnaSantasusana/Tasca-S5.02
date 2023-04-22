package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPlayerSqlRepo extends JpaRepository<PlayerSql, Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    Optional<PlayerSql> findByEmail(String email);
}
