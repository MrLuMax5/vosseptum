package edu.imse.sql.entity.resources;

import edu.imse.sql.entity.EditorialBoard;
import edu.imse.sql.entity.Topic;
import edu.imse.sql.entity.bridges.JournalistInBoard;
import edu.imse.sql.entity.bridges.JournalistInterestedTopic;
import edu.imse.sql.entity.response.EditorialBoardResponse;
import edu.imse.sql.entity.services.EditorialBoardService;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("editorial-board")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class EditorialBoardResource {

	@GET
	@Produces("application/json")
	public Response list() {
		List<EditorialBoard> boards = EditorialBoard.listAll();
		return Response.ok(boards).status(200).build();
	}

	@GET
	@Path("/count")
	public long count() {
		return EditorialBoard.count();
	}

	@GET
	@Transactional
	@Path("/fill")
	public Response fill() {
		int size = 20;
		EditorialBoardService service = new EditorialBoardService();
		service.fill(size);
		return Response.ok("Filled with " + size + " Entities").status(200).build();
	}

	@POST
	@Path("byJournalistId")
	public Response getInterestedTopicsByJournalist(long id) {
		List<EditorialBoard> byJournalistId = JournalistInBoard.findByJournalistId(id);
		List<EditorialBoardResponse> responseList = byJournalistId
				.stream()
				.map(EditorialBoardResponse::from)
				.toList();
		return Response.ok(responseList).status(200).build();
	}

}
