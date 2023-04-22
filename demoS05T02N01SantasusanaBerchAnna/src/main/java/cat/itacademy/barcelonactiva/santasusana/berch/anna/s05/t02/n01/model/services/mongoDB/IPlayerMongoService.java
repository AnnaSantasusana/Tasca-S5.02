package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mongoDB;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.GameDTOMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.PlayerDTOMongo;
import org.bson.types.ObjectId;
import java.util.List;

public interface IPlayerMongoService {

    PlayerDTOMongo createPlayer(PlayerDTOMongo playerDTO);
    PlayerDTOMongo modifyPlayerName(ObjectId playerId, PlayerDTOMongo playerDTO);
    GameDTOMongo createGame(ObjectId playerId);
    void deleteGames(ObjectId playerId);
    List<PlayerDTOMongo> playersWithSuccessRate();
    List<GameDTOMongo> allGamesByPlayer(ObjectId playerId);
    String getAverageSuccessRate();
    PlayerDTOMongo playerWithWorstSuccessRate();
    PlayerDTOMongo playerWithBetterSuccessRate();
}
