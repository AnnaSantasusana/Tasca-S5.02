package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PlayerDTOSql {

    private long id;
    private String name;
    private LocalDateTime registrationDate;
    private String successRate;
    public PlayerDTOSql(String playerName) {
        this.name = playerName;
    }

}
