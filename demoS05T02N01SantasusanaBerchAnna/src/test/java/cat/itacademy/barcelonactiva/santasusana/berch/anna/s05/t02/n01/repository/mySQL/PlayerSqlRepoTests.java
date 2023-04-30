package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.repository.mySQL;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mySQL.IPlayerSqlRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.Role.USER;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerSqlRepoTests {

    @Autowired
    private IPlayerSqlRepo playerRepo;
    private PlayerSql player;

    @BeforeEach
    public void setup() {
        //Arrange
        player = PlayerSql.builder()
                .name("Anna")
                .registrationDate(LocalDateTime.now())
                .games(new ArrayList<>())
                .email("anna_s@gmail.com")
                .password("abcde")
                .role(USER)
                .build();
    }

    @DisplayName("Test to save a player")
    @Test
    public void testSavePlayer() {
        //Act
        PlayerSql savedPlayer = playerRepo.save(player);

        //Assert
        Assertions.assertThat(savedPlayer).isNotNull();
        Assertions.assertThat(savedPlayer.getId()).isGreaterThan(0);
    }

    @DisplayName("Test to list the players")
    @Test
    public void testGetAll() {
        //Arrange
        PlayerSql player2 = PlayerSql.builder()
                .name("Marc")
                .registrationDate(LocalDateTime.now())
                .games(new ArrayList<>())
                .email("marc18@gmail.com")
                .password("1234")
                .role(USER)
                .build();

        //Act
        playerRepo.save(player);
        playerRepo.save(player2);

        List<PlayerSql> playerList = playerRepo.findAll();

        //Assert
        Assertions.assertThat(playerList).isNotNull();
        Assertions.assertThat(playerList.size()).isEqualTo(2);
    }

    @DisplayName("Test to get a player by ID")
    @Test
    public void testGetPlayerById() {

        //Act
        playerRepo.save(player);

        PlayerSql playerDatabase = playerRepo.findById(player.getId()).get();

        //Assert
        Assertions.assertThat(playerDatabase).isNotNull();
    }

    @DisplayName("Test to get a player by email")
    @Test
    public void testFindByEmail() {

        //Act
        playerRepo.save(player);

        PlayerSql player2 = playerRepo.findByEmail(player.getEmail()).get();

        //Assert
        Assertions.assertThat(player2).isNotNull();
    }

    @DisplayName("Test to update a player's name")
    @Test
    public void testUpdatePlayerName() {

        //Act
        playerRepo.save(player);

        PlayerSql playerDatabase = playerRepo.findById(player.getId()).get();
        playerDatabase.setName("Elisabet");
        PlayerSql updatedPlayer = playerRepo.save(playerDatabase);

        //Assert
        Assertions.assertThat(updatedPlayer.getName()).isEqualTo("Elisabet");
    }

    @DisplayName("Verify that a player exists by name in the database")
    @Test
    public void testExistsByName() {

        //Act
        playerRepo.save(player);

        boolean exists = playerRepo.existsByName(player.getName());

        //Assert
        Assertions.assertThat(exists).isTrue();
    }

    @DisplayName("Verify that a player exists by email in the database")
    @Test
    public void testExistsByEmail() {

        //Act
        playerRepo.save(player);

        boolean exists = playerRepo.existsByEmail(player.getEmail());

        //Assert
        Assertions.assertThat(exists).isTrue();
    }
}
