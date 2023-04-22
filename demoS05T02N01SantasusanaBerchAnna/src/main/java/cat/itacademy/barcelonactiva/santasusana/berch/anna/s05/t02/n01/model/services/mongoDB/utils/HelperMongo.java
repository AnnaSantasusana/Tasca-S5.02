package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mongoDB.utils;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.GameMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.PlayerMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.PlayerDTOMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.DuplicatedPlayerNameException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.EmptyPlayerListException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mongoDB.IPlayerMongoRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.ModelMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class HelperMongo {

    @Autowired
    private IPlayerMongoRepo playerRepo;

    public static final int MAX_VALUE = 6;

    public PlayerDTOMongo verifyPlayerDTOName(PlayerDTOMongo playerDTO) {
        boolean playerNameExists = playerRepo.existsByName(playerDTO.getName());
        if(playerNameExists && (playerDTO.getName() != null || !playerDTO.getName().trim().isEmpty())) {
            throw new DuplicatedPlayerNameException("This name already exists, please choose another one.");
        }
        if (playerDTO.getName() == null || playerDTO.getName().trim().isEmpty()) {
            playerDTO.setName("Unknown");
        }
        return playerDTO;
    }

    public PlayerMongo getPlayerById(ObjectId playerId) {
        Optional<PlayerMongo> player = playerRepo.findById(playerId);
        if(player.isPresent()) {
            return player.get();
        } else {
            throw new PlayerNotFoundException("Player not found");
        }
    }

    public String calculateSuccessRate(long wins, long totalGames) {
        if (totalGames > 0) {
            double successRate = (double) wins / totalGames * 100;
            return String.format(Locale.forLanguageTag("es-ES"), "%.2f", successRate);
        } else {
            return "No games saved";
        }
    }

    public PlayerDTOMongo getPlayerWithSuccessRate (PlayerMongo player){
        long totalGames = player.getGames().size();
        long wins = player.getGames().stream().filter(g -> g.getResult().equals("Won")).count();
        PlayerDTOMongo playerToShow = ModelMapper.singleInstance().toPlayerDTOMongo(player);
        playerToShow.setSuccessRate(calculateSuccessRate(wins, totalGames));
        return playerToShow;

    }

    public GameMongo rollDices(){
        GameMongo game = new GameMongo();
        int dice1 = (int) (Math.random() * MAX_VALUE) + 1;
        int dice2 = (int) (Math.random() * MAX_VALUE) + 1;
        String result = (dice1 + dice2 == 7) ? "Won" : "Lost";
        game.setDice1(dice1);
        game.setDice2(dice2);
        game.setResult(result);
        return game;
    }

    public List<PlayerMongo> isListFull(List<PlayerMongo> players) {
        if (players == null || players.isEmpty()) {
            throw new EmptyPlayerListException("No players found");
        }
        return players;
    }
}

