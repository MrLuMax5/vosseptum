package edu.imse.sql.entity.resources;

import edu.imse.sql.entity.Journalist;
import edu.imse.sql.entity.Topic;
import edu.imse.sql.entity.bridges.JournalistInterestedTopic;
import edu.imse.sql.entity.bridges.JournalistWrittenArticle;
import edu.imse.sql.entity.keys.JournalistInterestedTopicKey;
import edu.imse.sql.entity.response.InterestedJournalist;
import edu.imse.sql.entity.services.TopicService;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("topic")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class TopicResource {

	@GET
	@Produces("application/json")
	public Response list() {
		List<Topic> topics = Topic.listAll();
		return Response.ok(topics).status(200).build();
	}

	@GET
	@Path("/count")
	public long count() {
		return Topic.count();
	}

	@GET
	@Transactional
	@Path("/fill")
	public Response fill() {
		int size = 20;
		TopicService service = new TopicService();
		service.fill(size);
		return Response.ok("Filled with " + size + " Entities").status(200).build();
	}

	@POST
	@Path("byJournalistId")
	public Response getInterestedTopicsByJournalist(long id) {
		List<Topic> byJournalistId = JournalistInterestedTopic.findByJournalistId(id);
		return Response.ok(byJournalistId).status(200).build();
	}

	@POST
	@Path("byJournalistIds")
	public Response getInterestedTopicsByJournalist(List<Journalist> journalists) {
		List<Topic> byJournalistIds = new ArrayList<>();
		for(Journalist journalist: journalists) {
			byJournalistIds.addAll(JournalistInterestedTopic.findByJournalistId(journalist.id));
		}
		return Response.ok(byJournalistIds).status(200).build();
	}

	@POST
	@Transactional
	@Path("addInterestedJournalist")
	public Response addInterestedJournalist(InterestedJournalist journalist) {
		Journalist connectingJournalist = Journalist.findById(journalist.id());
		Topic interestedTopic = Topic.findById(journalist.topic());
		if (connectingJournalist == null || interestedTopic == null) {
			return Response.ok(false).status(200).build();
		}
		JournalistInterestedTopic journalistInterestedTopic = new JournalistInterestedTopic();
		JournalistInterestedTopicKey key = new JournalistInterestedTopicKey();
		key.journalist = connectingJournalist;
		key.topic = interestedTopic;
		if (JournalistInterestedTopic.findById(key) == null) {
			journalistInterestedTopic.journalistInterestedTopicKey = key;
			journalistInterestedTopic.persist();
		}
		return Response.ok(true).status(200).build();
	}
}
