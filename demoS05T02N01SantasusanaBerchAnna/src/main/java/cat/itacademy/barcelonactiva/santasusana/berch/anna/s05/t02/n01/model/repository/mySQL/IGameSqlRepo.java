package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.GameSql;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGameSqlRepo extends JpaRepository<GameSql, Long> {
}
