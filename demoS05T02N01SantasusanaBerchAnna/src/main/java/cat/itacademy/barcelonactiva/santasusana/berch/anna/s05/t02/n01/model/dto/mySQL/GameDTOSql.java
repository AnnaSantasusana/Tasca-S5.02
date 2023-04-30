package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GameDTOSql {

    @JsonIgnore
    private long id;
    private int dice1;
    private int dice2;
    private String result;

}
