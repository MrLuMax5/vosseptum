package de.vosseptum.sql.entity.response;

import java.util.List;

public record JournalistResponse(long id,
								 String email,
								 String password,
								 String location,
								 List<JournalistWithoutConnectionResponse> connections) { }
