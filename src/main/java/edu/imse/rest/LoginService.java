package edu.imse.rest;

import edu.imse.sql.entity.Journalist;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("login")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class LoginService {

	@POST
	@Produces("application/json")
	public Response fillAll(Journalist journalist) {
		PanacheQuery<PanacheEntityBase> query = Journalist.find("select id from Journalist where email = ?1", journalist.email);
		if (query.list().isEmpty()) {
			return Response.noContent().status(403).build();
		}

		return Response.ok(query.list().get(0)).status(200).build();
	}

}
