package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.GameSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.GameDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IGameSqlRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.ModelMapper;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.utils.HelperSql;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlayerSqlServiceImpl implements IPlayerSqlService {

    @Autowired
    private IPlayerSqlRepo playerRepo;
    @Autowired
    private IGameSqlRepo gameRepo;
    @Autowired
    HelperSql helper;
    public static final String NO_GAMES = "No games saved";

    /*
    @Override
    public PlayerDTOSql createPlayer(PlayerDTOSql playerDTO) {
        PlayerDTOSql verifiedPlayer = helper.verifyPlayerDTOName(playerDTO);
        verifiedPlayer.setRegistrationDate(LocalDateTime.now());
        PlayerSql playerEntity = ModelMapper.singleInstance().toPlayerSql(verifiedPlayer);
        playerRepo.save(playerEntity);
        playerDTO.setId(playerEntity.getId());
        playerDTO.setSuccessRate(NO_GAMES);
        return playerDTO;
    }*/

    @Override
    public PlayerDTOSql modifyPlayerName(long playerId, PlayerDTOSql playerDTO) {
        PlayerSql playerDatabase = helper.getPlayerById(playerId);
        PlayerDTOSql verifiedPlayer = helper.verifyPlayerDTOName(playerDTO);
        playerDatabase.setName(verifiedPlayer.getName());
        return helper.getPlayerWithSuccessRate(playerRepo.save(playerDatabase));
    }

    @Override
    public GameDTOSql createGame(long playerId) {
        PlayerSql player = helper.getPlayerById(playerId);
        GameSql game = helper.rollDices();
        game.setPlayer(player);
        player.getGames().add(game);
        GameSql gameSaved = gameRepo.save(game);
        playerRepo.save(player);
        return ModelMapper.singleInstance().toGameDTOSql(gameSaved);
    }

    @Override
    @Transactional
    public void deleteGames(long playerId) {
        PlayerSql player = helper.getPlayerById(playerId);
        player.getGames().clear();
    }

    @Override
    public List<PlayerDTOSql> playersWithSuccessRate() {
        List<PlayerSql> players = helper.isListFull(playerRepo.findAll());
        List<PlayerDTOSql> playerDTOs = new ArrayList<>(players.size());

        for (PlayerSql player: players) {
            playerDTOs.add(helper.getPlayerWithSuccessRate(player));
        }
        return playerDTOs;
    }

    @Override
    public List<GameDTOSql> allGamesByPlayer(Long playerId) {
        PlayerSql player = helper.getPlayerById(playerId);
        return ModelMapper.singleInstance().toGameDTOSqlList(player.getGames());
    }

    @Override
    public String getAverageSuccessRate() {
        List<PlayerDTOSql> players = playersWithSuccessRate();
        long numPlayersPlayed = players.stream().filter(p -> !p.getSuccessRate().equals(NO_GAMES)).count();
        double totalSuccessRate = players.stream()
                .filter(p -> !p.getSuccessRate().equals(NO_GAMES))
                .mapToDouble(p -> Double.parseDouble(p.getSuccessRate().replace(',', '.')))
                .sum();
        return String.format(Locale.forLanguageTag("es-ES"), "%.2f", totalSuccessRate / numPlayersPlayed);
    }

    @Override
    public PlayerDTOSql playerWithWorstSuccessRate() {
        List<PlayerDTOSql> players = playersWithSuccessRate();
        Optional<PlayerDTOSql> result = players.stream()
                .filter(p -> !p.getSuccessRate().equals(NO_GAMES))
                .min(Comparator.comparingDouble(p -> Double.parseDouble(p.getSuccessRate().replace(',', '.'))));
        return result.orElse(null);
    }

    @Override
    public PlayerDTOSql playerWithBetterSuccessRate() {
        List<PlayerDTOSql> players = playersWithSuccessRate();
        Optional<PlayerDTOSql> result = players.stream()
                .filter(p -> !p.getSuccessRate().equals(NO_GAMES))
                .max(Comparator.comparingDouble(p -> Double.parseDouble(p.getSuccessRate().replace(',', '.'))));
        return result.orElse(null);
    }
}

