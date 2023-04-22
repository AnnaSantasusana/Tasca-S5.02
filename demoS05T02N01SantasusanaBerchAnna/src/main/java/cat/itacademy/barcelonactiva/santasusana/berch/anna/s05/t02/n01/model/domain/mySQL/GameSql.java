package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "games")
@Entity
public class GameSql {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int dice1;

    @Column(nullable = false)
    private int dice2;

    @Column(nullable = false)
    private String result;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerSql player;


    @Override
    public String toString() {
        return "GameMysql{" +
                "id=" + id +
                ", dice1=" + dice1 +
                ", dice2=" + dice2 +
                ", result='" + result + '\'' +
                ", playerId=" + (player == null ? "null" : player.getId()) +
                '}';
    }

}

