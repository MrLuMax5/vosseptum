package de.vosseptum.nosql.resources;

import de.vosseptum.nosql.collection.MongoJournalist;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.response.EditorialBoardResponse;
import de.vosseptum.sql.entity.response.JournalistWithoutConnectionResponse;
import de.vosseptum.nosql.dao.ConnectionDAO;
import de.vosseptum.nosql.dao.JournalistWithEmailDAO;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/mongo/journalist")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MongoJournalistResource {
	Random random = new Random();

	@GET
	public List<MongoJournalist> list() {
		return MongoJournalist.listAll();
	}

	@POST
	@Produces("application/json")
	public Response byId(long id) {
		MongoJournalist journalist = MongoJournalist.find("sql_id", id).firstResult();
		return Response.ok(JournalistWithoutConnectionResponse.fromMongoDbEntry(JournalistWithEmailDAO.from(journalist))).status(200).build();
	}

	@GET
	@Path("/count")
	public long count() {
		return MongoJournalist.count();
	}

	@POST
	@Path("/add")
	public Response add(Journalist journalist) {
		journalist.id = this.createNewJournalistWithId(journalist);
		MongoJournalist mongoJournalist = MongoJournalist.from(journalist);
		mongoJournalist.persist();
		return Response.ok(mongoJournalist).status(200).build();
	}

	@Transactional
	public Long createNewJournalistWithId(Journalist journalist) {
		Long id = journalist.id;
		if (journalist.id == null) {
			Journalist newJournalist = new Journalist();
			newJournalist.location = journalist.location;
			newJournalist.email = journalist.email;
			newJournalist.password = journalist.password;
			newJournalist.persist();
			id = newJournalist.id;
		}
		return id;
	}

	@POST
	@Path("/connections")
	public Response getConnections(long id) {
		MongoJournalist queriedJournalist = MongoJournalist.find("sql_id", id).firstResult();
		Set<JournalistWithoutConnectionResponse> connections = queriedJournalist.connections
				.stream()
				.map(JournalistWithoutConnectionResponse::fromMongo)
				.collect(Collectors.toSet());
		return Response.ok(connections).status(200).build();
	}

	@POST
	@Path("/connections/add")
	public Response addConnection(Journalist journalist) {
		MongoJournalist queriedJournalist = MongoJournalist.find("sql_id", journalist.id).firstResult();
		MongoJournalist existingJournalist = MongoJournalist.find("email", journalist.email).firstResult();
		if (!(existingJournalist instanceof MongoJournalist)) {
			return Response.ok(false).status(200).build();
		}
		queriedJournalist.connections.add(ConnectionDAO.from(existingJournalist));
		queriedJournalist.update();
		existingJournalist.connections.add(ConnectionDAO.from(queriedJournalist));
		existingJournalist.update();
		return Response.ok(true).status(200).build();
	}

	@POST
	@Path("/editorial-board/byJournalistId")
	public Response getBoardsOfJournalist(long id) {
		MongoJournalist queriedJournalist = MongoJournalist.find("sql_id", id).firstResult();
		List<EditorialBoardResponse> boards = queriedJournalist.boards
				.stream()
				.map(EditorialBoardResponse::fromMongo)
				.toList();
		return Response.ok(boards).status(200).build();
	}
}
