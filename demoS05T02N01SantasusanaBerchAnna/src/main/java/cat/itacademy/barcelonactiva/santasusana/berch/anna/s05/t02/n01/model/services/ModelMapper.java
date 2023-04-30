package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.services;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.GameMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.PlayerMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.GameSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mySQL.PlayerSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.GameDTOMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mongoDB.PlayerDTOMongo;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.GameDTOSql;
import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.dto.mySQL.PlayerDTOSql;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    private final org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

    private static ModelMapper instance;

    private ModelMapper() {}

    public static ModelMapper singleInstance() {
        if (instance == null) {
            instance = new ModelMapper();
        }
        return instance;
    }

    public PlayerDTOSql toPlayerDTOSql(PlayerSql player) {
        return mapper.map(player, PlayerDTOSql.class);
    }

    public PlayerDTOMongo toPlayerDTOMongo(PlayerMongo player) {
        return mapper.map(player, PlayerDTOMongo.class);
    }

    public PlayerMongo toPlayerMongo(PlayerDTOMongo playerDTO) {
        return mapper.map(playerDTO, PlayerMongo.class);
    }

    public GameDTOSql toGameDTOSql(GameSql game) {
        return mapper.map(game, GameDTOSql.class);
    }

    public GameDTOMongo toGameDTOMongo(GameMongo game) {
        return mapper.map(game, GameDTOMongo.class);
    }

    public List<GameDTOSql> toGameDTOSqlList(List<GameSql> gameList) {
        return gameList.stream().map(this::toGameDTOSql).collect(Collectors.toList());
    }

    public List<GameDTOMongo> toGameDTOMongoList(List<GameMongo> gameList) {
        return gameList.stream().map(this::toGameDTOMongo).collect(Collectors.toList());
    }

}

