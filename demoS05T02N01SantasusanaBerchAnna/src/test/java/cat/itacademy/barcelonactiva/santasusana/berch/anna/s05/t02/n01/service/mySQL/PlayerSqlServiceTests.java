package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.service.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.GameSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.GameDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.PlayerSqlServiceImpl;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services.mySQL.utils.HelperSql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;

import static cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PlayerSqlServiceTests {

    @Mock
    private IPlayerSqlRepo playerRepo;
    @Mock
    private HelperSql helper;
    @InjectMocks
    private PlayerSqlServiceImpl playerService;

    PlayerSql player;
    PlayerSql player2;
    GameSql game1;
    GameSql game2;
    GameSql game3;
    PlayerDTOSql playerWithPercentage;
    PlayerDTOSql player2WithPercentage;

    @BeforeEach
    public void setup() {

        // Create test data
        //Player:
        player = PlayerSql.builder().id(1L).name("Lluís").registrationDate(LocalDateTime.now()).games(new ArrayList<>())
                .email("llsc@gmail.com").password("12345").role(USER).build();
        game1 = GameSql.builder().dice1(4).dice2(3).result("Won").build();
        game2 = GameSql.builder().dice1(5).dice2(3).result("Lost").build();

        player.getGames().add(game1);
        player.getGames().add(game2);
        playerRepo.save(player);

        playerWithPercentage = PlayerDTOSql.builder().id(1L).name("Lluís").registrationDate(LocalDateTime.now())
                .successRate("50.0").build();

        //Player2:
        player2 = PlayerSql.builder().id(2L).name("Anna").registrationDate(LocalDateTime.now()).games(new ArrayList<>())
                .email("anna_s@gmail.com").password("abcde").role(USER).build();
        game3 = GameSql.builder().dice1(4).dice2(4).result("Lost").build();

        player2.getGames().add(game3);
        playerRepo.save(player2);

        player2WithPercentage = PlayerDTOSql.builder().id(2L).name("Anna").registrationDate(LocalDateTime.now())
                .successRate("00.0").build();
    }

    @Test
    public void testModifyPlayerName() {

        // Create test data
        long playerId = 1L;

        PlayerDTOSql playerDTO = PlayerDTOSql.builder().name("Sergi").build();

        PlayerSql playerUpdated = PlayerSql.builder().id(1L).name("Sergi").registrationDate(LocalDateTime.now())
                .games(new ArrayList<>()).email("sergivaz@gmail.com").password("12345").role(USER).build();

        PlayerDTOSql playerWithPercentage = PlayerDTOSql.builder().id(1L).name("Sergi").registrationDate(LocalDateTime.now())
                .successRate("50.0").build();

        // Set up mocks
        Mockito.when(helper.getPlayerById(playerId)).thenReturn(player);
        Mockito.when(helper.verifyPlayerDTOName(playerDTO)).thenReturn(playerDTO);
        Mockito.when(playerRepo.save(Mockito.any(PlayerSql.class))).thenReturn(playerUpdated);
        Mockito.when(helper.getPlayerWithSuccessRate(playerUpdated)).thenReturn(playerWithPercentage);

        // Call method
        PlayerDTOSql playerDTONameModified = playerService.modifyPlayerName(playerId, playerDTO);

        // Verify
        Assertions.assertThat(playerDTONameModified).isNotNull();
        assertEquals(playerWithPercentage, playerDTONameModified);
    }

    @Test
    public void testCreateGame() {

        // Create test data
        long playerId = 1L;

        GameSql game3 = GameSql.builder().dice1(1).dice2(6).result("Won").build();
        player.getGames().add(game3);
        playerRepo.save(player);

        GameDTOSql game3DTO = GameDTOSql.builder().dice1(1).dice2(6).result("Won").build();

        // Set up mocks
        Mockito.when(helper.getPlayerById(playerId)).thenReturn(player);
        Mockito.when(helper.rollDices()).thenReturn(game3);
        Mockito.when(playerRepo.save(player)).thenReturn(player);

        // Call method
        GameDTOSql result = playerService.createGame(playerId);

        // Verify
        assertNotNull(result);
        assertEquals(game3DTO, result);
    }

    @Test
    public void testDeleteGames() {

        // Set up mocks
        Mockito.when(helper.getPlayerById(1L)).thenReturn(player);

        // Call method
        playerService.deleteGames(1L);

        // Verify
        assertEquals(0, player.getGames().size());
    }

    @Test
    public void testPlayersWithSuccessRate() {

        // Create test data
        List<PlayerSql> players = new ArrayList<>();
        players.add(player);
        players.add(player2);

        Mockito.when(playerRepo.findAll()).thenReturn(players);
        Mockito.when(helper.isListFull(players)).thenReturn(players);

        // Set up mocks
        Mockito.when(helper.getPlayerWithSuccessRate(player)).thenReturn(playerWithPercentage);
        Mockito.when(helper.getPlayerWithSuccessRate(player2)).thenReturn(player2WithPercentage);

        // Call method
        List<PlayerDTOSql> result = playerService.playersWithSuccessRate();

        // Verify
        List<PlayerDTOSql> expected = new ArrayList<>();
        expected.add(playerWithPercentage);
        expected.add(player2WithPercentage);
        assertEquals(expected, result);
    }

    @Test
    public void testAllGamesByPlayer() {

        // Create test data
        long playerId = 1L;

        // Set up mocks
        Mockito.when(helper.getPlayerById(playerId)).thenReturn(player);

        // Call method
        List<GameDTOSql> gameDTOSqlList = playerService.allGamesByPlayer(playerId);

        // Verify
        assertEquals(2, gameDTOSqlList.size());
        assertEquals(game1.getDice1(), gameDTOSqlList.get(0).getDice1());
        assertEquals(game2.getDice1(), gameDTOSqlList.get(1).getDice1());
    }

    @Test
    public void testGetAverageSuccessRate() {

        // Create test data
        List<PlayerSql> players = Arrays.asList(player, player2);

        // Set up mocks
        Mockito.when(playerRepo.findAll()).thenReturn(players);
        Mockito.when(helper.isListFull(players)).thenReturn(players);
        Mockito.when(helper.getPlayerWithSuccessRate(player)).thenReturn(playerWithPercentage);
        Mockito.when(helper.getPlayerWithSuccessRate(player2)).thenReturn(player2WithPercentage);

        // Call method
        String result = playerService.getAverageSuccessRate();

        // Verify
        assertEquals("25,00", result);
    }

    @Test
    public void testPlayerWithWorstSuccessRate() {

        // Create test data
        List<PlayerSql> players = Arrays.asList(player, player2);

        // Set up mocks
        Mockito.when(playerRepo.findAll()).thenReturn(players);
        Mockito.when(helper.isListFull(players)).thenReturn(players);
        Mockito.when(helper.getPlayerWithSuccessRate(player)).thenReturn(playerWithPercentage);
        Mockito.when(helper.getPlayerWithSuccessRate(player2)).thenReturn(player2WithPercentage);

        // Call method
        PlayerDTOSql worstPlayer = playerService.playerWithWorstSuccessRate();

        // Verify
        assertEquals(player2WithPercentage, worstPlayer);
    }

    @Test
    public void testPlayerWithBetterSuccessRate() {

        // Create test data
        List<PlayerSql> players = Arrays.asList(player, player2);

        // Set up mocks
        Mockito.when(playerRepo.findAll()).thenReturn(players);
        Mockito.when(helper.isListFull(players)).thenReturn(players);
        Mockito.when(helper.getPlayerWithSuccessRate(player)).thenReturn(playerWithPercentage);
        Mockito.when(helper.getPlayerWithSuccessRate(player2)).thenReturn(player2WithPercentage);

        // Call method
        PlayerDTOSql bestPlayer = playerService.playerWithBetterSuccessRate();

        // Verify
        assertEquals(playerWithPercentage, bestPlayer);
    }

}