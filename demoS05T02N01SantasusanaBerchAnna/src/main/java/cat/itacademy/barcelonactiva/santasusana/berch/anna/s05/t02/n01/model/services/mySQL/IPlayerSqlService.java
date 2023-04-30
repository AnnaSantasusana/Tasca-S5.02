package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.GameDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import java.util.List;

public interface IPlayerSqlService {

    //PlayerDTOSql createPlayer(PlayerDTOSql playerDTO);
    PlayerDTOSql modifyPlayerName(long playerId, PlayerDTOSql playerDTO);
    GameDTOSql createGame(long playerId);
    void deleteGames(long playerId);
    List<PlayerDTOSql> playersWithSuccessRate();
    List<GameDTOSql> allGamesByPlayer(Long playerId);
    String getAverageSuccessRate();
    PlayerDTOSql playerWithWorstSuccessRate();
    PlayerDTOSql playerWithBetterSuccessRate();


}
