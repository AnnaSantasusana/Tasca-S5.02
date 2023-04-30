package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.controller;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.controllers.mySQL.PlayerSqlController;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.Message;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.GameDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.IPlayerSqlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerSqlControllerTest {

    @Mock
    private IPlayerSqlService playerService;

    @InjectMocks
    private PlayerSqlController playerController;

    @Test
    public void testModifyPlayerName() throws Exception {
        // Arrange
        long playerId = 1L;
        String newName = "Maria Antònia";
        PlayerDTOSql player = PlayerDTOSql.builder().name(newName).build();
        PlayerDTOSql modifiedPlayer = PlayerDTOSql.builder().id(playerId).name(newName).build();

        when(playerService.modifyPlayerName(playerId, player)).thenReturn(modifiedPlayer);

        // Act
        ResponseEntity<PlayerDTOSql> response = playerController.modifyPlayerName(playerId, player);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(modifiedPlayer, response.getBody());
    }

    @Test
    public void testPlayerRollDices() throws Exception {
        // Arrange
        long playerId = 1L;
        GameDTOSql game = GameDTOSql.builder().id(1L).dice1(4).dice2(3).result("Won").build();

        when(playerService.createGame(playerId)).thenReturn(game);

        // Act
        ResponseEntity<GameDTOSql> response = playerController.playerRollDices(playerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, response.getBody());
    }

    @Test
    public void testDeletePlayerGames() throws Exception {
        // Arrange
        long playerId = 1L;
        WebRequest request = mock(WebRequest.class);

        doNothing().when(playerService).deleteGames(playerId);

        // Act
        ResponseEntity<Message> response = playerController.deletePlayerGames(playerId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Games deleted successfully", response.getBody().getMessage());
    }

    @Test
    public void testGetAllPlayersWithSuccessRate() throws Exception {
        // Arrange
        List<PlayerDTOSql> expectedPlayers = new ArrayList<>();
        expectedPlayers.add(PlayerDTOSql.builder().id(1L).name("Lluís").successRate("50.0").build());
        expectedPlayers.add(PlayerDTOSql.builder().id(2L).name("Anna").successRate("00.0").build());

        when(playerService.playersWithSuccessRate()).thenReturn(expectedPlayers);

        // Act
        ResponseEntity<List<PlayerDTOSql>> response = playerController.getAllPlayersWithSuccessRate();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPlayers, response.getBody());
    }

    @Test
    public void testGetAllSinglePlayerGames() throws Exception {
        // Arrange
        long playerId = 1L;
        List<GameDTOSql> expectedGames = new ArrayList<>();
        expectedGames.add(new GameDTOSql());
        expectedGames.add(new GameDTOSql());
        when(playerService.allGamesByPlayer(playerId)).thenReturn(expectedGames);

        ResponseEntity<List<GameDTOSql>> expectedResponse = new ResponseEntity<>(expectedGames, HttpStatus.OK);

        // Act
        ResponseEntity<List<GameDTOSql>> response = playerController.getAllSinglePlayerGames(playerId);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetRanking() throws Exception {
        // Arrange
        WebRequest request = mock(WebRequest.class);
        String successRate = "75,23";
        String expectedMessage = "The average success rate is " + successRate + "%";
        when(playerService.getAverageSuccessRate()).thenReturn(successRate);
        Message expectedResponse = new Message(HttpStatus.OK.value(), new Date(), expectedMessage, request.getDescription(false));

        // Act
        ResponseEntity<Message> response = playerController.getRanking(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), response.getBody().getMessage());
    }

    @Test
    public void testWorstPlayer() throws Exception {
        // Arrange
        PlayerDTOSql expectedPlayer = PlayerDTOSql.builder().id(1L).name("Lluís")
                .registrationDate(LocalDateTime.now()).successRate("13.0").build();

        when(playerService.playerWithWorstSuccessRate()).thenReturn(expectedPlayer);

        // Act
        ResponseEntity<PlayerDTOSql> response = playerController.worstPlayer();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPlayer, response.getBody());
        verify(playerService, times(1)).playerWithWorstSuccessRate();
    }

    @Test
    public void testBestPlayer() throws Exception {
        // Arrange
        PlayerDTOSql expectedPlayer = PlayerDTOSql.builder().id(2L).name("Xavi")
                .registrationDate(LocalDateTime.now()).successRate("77.5").build();

        when(playerService.playerWithBetterSuccessRate()).thenReturn(expectedPlayer);

        // Act
        ResponseEntity<PlayerDTOSql> response = playerController.bestPlayer();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPlayer, response.getBody());
        verify(playerService, times(1)).playerWithBetterSuccessRate();
    }


}


