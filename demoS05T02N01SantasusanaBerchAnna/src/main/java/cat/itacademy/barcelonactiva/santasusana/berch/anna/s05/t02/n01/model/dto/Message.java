package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Message {

    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
