package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.utils;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.GameSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.DuplicatedPlayerNameException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.EmptyPlayerListException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class HelperSql {

    @Autowired
    private IPlayerSqlRepo playerMysqlRepo;

    public static final int MAX_VALUE = 6;

    public PlayerDTOSql verifyPlayerDTOName(PlayerDTOSql playerDTO) {
        boolean playerNameExists = playerMysqlRepo.existsByName(playerDTO.getName());
        if(playerNameExists && (playerDTO.getName() != null || !playerDTO.getName().trim().isEmpty())) {
            throw new DuplicatedPlayerNameException("This name already exists, please choose another one.");
        }
        if (playerDTO.getName() == null || playerDTO.getName().trim().isEmpty()) {
            playerDTO.setName("Unknown");
        }
        return playerDTO;
    }

    public String verifyName(String playerName) {
        boolean playerNameExists = playerMysqlRepo.existsByName(playerName);
        if(playerNameExists && (playerName != null || !playerName.trim().isEmpty())) {
            throw new DuplicatedPlayerNameException("This name already exists, please choose another one.");
        }
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Unknown";
        }
        return playerName;
    }

    public String verifyEmail(String playerEmail) {
        boolean playerEmailExists = playerMysqlRepo.existsByEmail(playerEmail);
        if(playerEmailExists) {
            throw new DuplicatedPlayerNameException("This email already exists, please choose another one.");
        }
        return playerEmail;
    }

    public PlayerSql getPlayerById(long playerId) {
        Optional<PlayerSql> player = playerMysqlRepo.findById(playerId);
        if(player.isPresent()) {
            return player.get();
        } else {
            throw new PlayerNotFoundException("Player not found");
        }
    }

    public GameSql rollDices(){
        GameSql game = new GameSql();
        int dice1 = (int) (Math.random() * MAX_VALUE) + 1;
        int dice2 = (int) (Math.random() * MAX_VALUE) + 1;
        String result = (dice1 + dice2 == 7) ? "Won" : "Lost";
        game.setDice1(dice1);
        game.setDice2(dice2);
        game.setResult(result);
        return game;
    }

    public String calculateSuccessRate(long wins, long totalGames) {
        if (totalGames > 0) {
            double successRate = (double) wins / totalGames * 100;
            return String.format(Locale.forLanguageTag("es-ES"), "%.2f", successRate);
        } else {
            return "No games saved";
        }
    }

    public List<PlayerSql> isListFull(List<PlayerSql> players) {
        if (players == null || players.isEmpty()) {
            throw new EmptyPlayerListException("No players found");
        }
        return players;
    }

    public PlayerDTOSql getPlayerWithSuccessRate (PlayerSql player){
        long totalGames = player.getGames().size();
        long wins = player.getGames().stream().filter(g -> g.getResult().equals("Won")).count();
        PlayerDTOSql playerToShow = ModelMapper.singleInstance().toPlayerDTOSql(player);
        playerToShow.setSuccessRate(calculateSuccessRate(wins, totalGames));
        return playerToShow;

    }
}

