package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.controllers.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.Message;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.GameDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.DuplicatedPlayerNameException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.EmptyPlayerListException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.IPlayerSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/players/mysql")
public class PlayerSqlController {

    @Autowired
    private IPlayerSqlService playerService;


    /*@PostMapping
    public ResponseEntity<PlayerDTOSql> createPlayer(@RequestBody PlayerDTOSql player) throws Exception {
        try {
            return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.CREATED);
        } catch (DuplicatedPlayerNameException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while creating a player", e);
        }
    }*/

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerDTOSql> modifyPlayerName(@PathVariable("playerId") long playerId,
                                                         @RequestBody PlayerDTOSql player) throws Exception {
        try {
            return ResponseEntity.ok(playerService.modifyPlayerName(playerId, player));
        } catch (PlayerNotFoundException | DuplicatedPlayerNameException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while updating a player", e);
        }
    }

    @PostMapping("/{playerId}/games")
    public ResponseEntity<GameDTOSql> playerRollDices(@PathVariable("playerId") long playerId) throws Exception {

        try {
            return ResponseEntity.ok(playerService.createGame(playerId));
        } catch (PlayerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while rolling the dices", e);
        }
    }

    @DeleteMapping("/{playerId}/games")
    public ResponseEntity<Message> deletePlayerGames(@PathVariable("playerId") long playerId, WebRequest request) throws Exception {

        try {
            playerService.deleteGames(playerId);
            Message message = new Message(HttpStatus.OK.value(), new Date(), "Games deleted successfully", request.getDescription(false));
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (PlayerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while deleting plays", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTOSql>> getAllPlayersWithSuccessRate() throws Exception {
        try {
            return ResponseEntity.ok(playerService.playersWithSuccessRate());
        } catch (EmptyPlayerListException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while getting all players with their success rate", e);
        }
    }

    @GetMapping("/{id}/games")
    public ResponseEntity<List<GameDTOSql>> getAllSinglePlayerGames(@PathVariable Long id) throws Exception {
        try {
            return ResponseEntity.ok(playerService.allGamesByPlayer(id));
        } catch (PlayerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while getting all games from single player", e);
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<Message> getRanking(WebRequest request) throws Exception {
        try {
            Message message = new Message(HttpStatus.OK.value(), new Date(), "The average success rate is " + playerService.getAverageSuccessRate() + "%", request.getDescription(false));
            return ResponseEntity.ok(message);
        } catch (EmptyPlayerListException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while getting the ranking", e);
        }
    }

    @GetMapping("/ranking/loser")
    public ResponseEntity<PlayerDTOSql> worstPlayer() throws Exception {
        try {
            return ResponseEntity.ok(playerService.playerWithWorstSuccessRate());
        } catch (EmptyPlayerListException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while getting the worst player", e);
        }
    }

    @GetMapping("/ranking/winner")
    public ResponseEntity<PlayerDTOSql> bestPlayer() throws Exception {
        try {
            return ResponseEntity.ok(playerService.playerWithBetterSuccessRate());
        } catch (EmptyPlayerListException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error while getting the best player", e);
        }
    }


}

