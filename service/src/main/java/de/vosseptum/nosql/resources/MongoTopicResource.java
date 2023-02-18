package de.vosseptum.nosql.resources;

import de.vosseptum.nosql.collection.MongoJournalist;
import de.vosseptum.nosql.collection.MongoTopic;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.response.InterestedJournalist;
import de.vosseptum.nosql.dao.InterestedTopicsDAO;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/mongo/topic")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MongoTopicResource {

	@GET
	public List<MongoTopic> list() {
		return MongoTopic.listAll();
	}

	@GET
	@Path("/count")
	public long count() {
		return MongoTopic.count();
	}

	@POST
	@Path("byJournalistId")
	public Response getInterestedTopicsByJournalist(long id) {
		PanacheMongoEntityBase journalistBySQLId = MongoJournalist.find("sql_id", id)
				.firstResult();
		if (journalistBySQLId instanceof  MongoJournalist journalist) {
			return Response.ok(journalist.topics).status(200).build();
		}
		return Response.ok(List.of()).status(200).build();
	}

	@POST
	@Path("byJournalistIds")
	public Response getInterestedTopicsByJournalist(List<Journalist> journalists) {
		List<InterestedTopicsDAO> byJournalistIds = new ArrayList<>();
		for (Journalist sqlJournalist: journalists) {
			PanacheMongoEntityBase journalistBySQLId = MongoJournalist.find("sql_id", sqlJournalist.id)
					.firstResult();
			if (journalistBySQLId instanceof  MongoJournalist journalist) {
				byJournalistIds.addAll(journalist.topics);
			}
		}
		return Response.ok(byJournalistIds).status(200).build();
	}

	@POST
	@Path("addInterestedJournalist")
	public Response addInterestedJournalist(InterestedJournalist journalistRequest) {
		PanacheMongoEntityBase topicInDB = MongoTopic.find("subject", journalistRequest.topic())
				.firstResult();
		if (topicInDB instanceof MongoTopic topic) {
			PanacheMongoEntityBase journalistBySQLId = MongoJournalist.find("sql_id", journalistRequest.id())
					.firstResult();
			if (journalistBySQLId instanceof  MongoJournalist journalistInDB) {
				journalistInDB.topics.add(InterestedTopicsDAO.from(topic));
				journalistInDB.update();
				return Response.ok(true).status(200).build();
			}
		}
		return Response.ok(false).status(200).build();
	}

}
