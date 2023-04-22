package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlayerDTOMongo {

    private ObjectId id;
    private String name;
    private LocalDateTime registrationDate;
    private String successRate;
}
