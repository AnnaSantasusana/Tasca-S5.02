package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameMongo {

    private int dice1;
    private int dice2;
    private String result;

}
