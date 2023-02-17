package edu.imse.sql.entity.resources;

import edu.imse.sql.entity.Journalist;
import edu.imse.sql.entity.response.JournalistResponse;
import edu.imse.sql.entity.response.JournalistWithoutConnectionResponse;
import edu.imse.sql.entity.services.JournalistService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("journalist")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class JournalistResource {

	@GET
	@Produces("application/json")
	public Response list() {
		List<Journalist> journalists = Journalist.listAll();
		return Response.ok(journalists).status(200).build();
	}

	@POST
	@Produces("application/json")
	public Response byId(long id) {
		Journalist journalist = Journalist.findById(id);
		return Response.ok(JournalistWithoutConnectionResponse.from(journalist)).status(200).build();
	}

	@GET
	@Path("/count")
	public long count() {
		return Journalist.count();
	}

	@GET
	@Transactional
	@Path("/fill")
	public Response fill() {
		int size = 20;
		JournalistService service = new JournalistService();
		service.fill(size);
		return Response.ok(Journalist.count()).status(200).build();
	}

	@POST
	@Path("/add")
	@Transactional
	public Response add(Journalist journalist) {
		journalist.persist();
		return Response.ok(journalist).status(200).build();
	}

	@POST
	@Path("/connections")
	public Response getConnections(long id) {
		Journalist journalist = Journalist.findById(id);
		Set<JournalistWithoutConnectionResponse> connections = journalist.connections
				.stream()
				.map(JournalistWithoutConnectionResponse::from)
				.collect(Collectors.toSet());
		connections.addAll(journalist.connectionOf
				.stream()
				.map(JournalistWithoutConnectionResponse::from)
				.toList());
		return Response.ok(connections).status(200).build();
	}

	@POST
	@Transactional
	@Path("/connections/add")
	public Response addConnection(Journalist journalist) {
		Journalist connectingJournalist = Journalist.findById(journalist.id);
		List<Journalist> existingJournalists = Journalist.findByEmail(journalist.email);
		if (existingJournalists == null || existingJournalists.isEmpty()) {
			return Response.ok(false).status(200).build();
		}
		connectingJournalist.connections.add(existingJournalists.get(0));
		connectingJournalist.persist();
		return Response.ok(true).status(200).build();
	}

}
