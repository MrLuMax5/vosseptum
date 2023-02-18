package de.vosseptum.nosql.collection;

import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.nosql.dao.BoardDTO;
import de.vosseptum.nosql.dao.ConnectionDAO;
import de.vosseptum.nosql.dao.InterestedTopicsDAO;
import de.vosseptum.nosql.dao.WrittenArticlesDAO;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;


@MongoEntity(collection="journalist")
@NoArgsConstructor
public class MongoJournalist extends PanacheMongoEntity {
	public Long sql_id;
	public String email;
	public String password;
	public String location;
	public Set<WrittenArticlesDAO> articles = new HashSet<>();
	public Set<InterestedTopicsDAO> topics = new HashSet<>();
	public Set<ConnectionDAO> connections = new HashSet<>();
	public Set<BoardDTO> boards = new HashSet<>();

	public static MongoJournalist from(Journalist sqlJournalist) {
		MongoJournalist mongoJournalist = new MongoJournalist();
		mongoJournalist.sql_id = sqlJournalist.id;
		mongoJournalist.email = sqlJournalist.email;
		mongoJournalist.password = sqlJournalist.password;
		mongoJournalist.location = sqlJournalist.location;
		return mongoJournalist;
	}

}
