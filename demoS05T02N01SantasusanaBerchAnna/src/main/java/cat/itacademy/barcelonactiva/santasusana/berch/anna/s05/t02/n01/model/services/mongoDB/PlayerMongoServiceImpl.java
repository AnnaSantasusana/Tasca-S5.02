package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mongoDB;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.GameMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.PlayerMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.GameDTOMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.PlayerDTOMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mongoDB.IPlayerMongoRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.ModelMapper;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mongoDB.utils.HelperMongo;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlayerMongoServiceImpl implements IPlayerMongoService{

    @Autowired
    IPlayerMongoRepo playerRepo;
    @Autowired
    HelperMongo helper;
    public static final String NO_GAMES = "No games saved";

    @Override
    public PlayerDTOMongo createPlayer(PlayerDTOMongo playerDTO) {
        PlayerDTOMongo verifiedPlayer = helper.verifyPlayerDTOName(playerDTO);
        verifiedPlayer.setRegistrationDate(LocalDateTime.now());
        PlayerMongo player = ModelMapper.singleInstance().toPlayerMongo(verifiedPlayer);
        playerRepo.save(player);
        playerDTO.setId(player.getId());
        playerDTO.setSuccessRate(NO_GAMES);
        return playerDTO;
    }

    @Override
    public PlayerDTOMongo modifyPlayerName(ObjectId playerId, PlayerDTOMongo playerDTO) {
        PlayerMongo playerDatabase = helper.getPlayerById(playerId);
        PlayerDTOMongo verifiedPlayer = helper.verifyPlayerDTOName(playerDTO);
        playerDatabase.setName(verifiedPlayer.getName());
        return helper.getPlayerWithSuccessRate(playerRepo.save(playerDatabase));
    }

    @Override
    public GameDTOMongo createGame(ObjectId playerId) {
        PlayerMongo playerDatabase = helper.getPlayerById(playerId);
        GameMongo game = helper.rollDices();
        playerDatabase.getGames().add(game);
        playerRepo.save(playerDatabase);
        return ModelMapper.singleInstance().toGameDTOMongo(game);
    }

    @Override
    @Transactional
    public void deleteGames(ObjectId playerId) {
        PlayerMongo player = helper.getPlayerById(playerId);
        player.getGames().clear();
        playerRepo.save(player);
    }

    @Override
    public List<PlayerDTOMongo> playersWithSuccessRate() {
        List<PlayerMongo> players = helper.isListFull(playerRepo.findAll());
        List<PlayerDTOMongo> playerDTOs = new ArrayList<>(players.size());

        for (PlayerMongo player: players) {
            playerDTOs.add(helper.getPlayerWithSuccessRate(player));
        }
        return playerDTOs;
    }

    @Override
    public List<GameDTOMongo> allGamesByPlayer(ObjectId playerId) {
        PlayerMongo player = helper.getPlayerById(playerId);
        return ModelMapper.singleInstance().toGameDTOMongoList(player.getGames());
    }

    @Override
    public String getAverageSuccessRate() {
        List<PlayerDTOMongo> players = playersWithSuccessRate();
        long numPlayersPlayed = players.stream().filter(p -> !p.getSuccessRate().equals(NO_GAMES)).count();
        double totalSuccessRate = players.stream()
                .filter(p -> !p.getSuccessRate().equals(NO_GAMES))
                .mapToDouble(p -> Double.parseDouble(p.getSuccessRate().replace(',', '.')))
                .sum();
        return String.format(Locale.forLanguageTag("es-ES"), "%.2f", totalSuccessRate / numPlayersPlayed);
    }

    @Override
    public PlayerDTOMongo playerWithWorstSuccessRate() {
        List<PlayerDTOMongo> players = playersWithSuccessRate();
        Optional<PlayerDTOMongo> result = players.stream()
                .filter(p -> !p.getSuccessRate().equals(NO_GAMES))
                .min(Comparator.comparingDouble(p -> Double.parseDouble(p.getSuccessRate().replace(',', '.'))));
        return result.orElse(null);
    }

    @Override
    public PlayerDTOMongo playerWithBetterSuccessRate() {
        List<PlayerDTOMongo> players = playersWithSuccessRate();
        Optional<PlayerDTOMongo> result = players.stream()
                .filter(p -> !p.getSuccessRate().equals(NO_GAMES))
                .max(Comparator.comparingDouble(p -> Double.parseDouble(p.getSuccessRate().replace(',', '.'))));
        return result.orElse(null);
    }
}

