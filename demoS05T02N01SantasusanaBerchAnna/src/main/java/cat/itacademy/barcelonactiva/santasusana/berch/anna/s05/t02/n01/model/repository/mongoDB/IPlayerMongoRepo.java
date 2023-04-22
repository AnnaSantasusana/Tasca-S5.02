package cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.repository.mongoDB;

import cat.itacademy.barcelonactiva.santasusana.berch.anna.s05.t02.n01.model.domain.mongoDB.PlayerMongo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerMongoRepo extends MongoRepository<PlayerMongo, ObjectId> {

    boolean existsByName(String name);

}
