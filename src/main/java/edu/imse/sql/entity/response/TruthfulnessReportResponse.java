package edu.imse.sql.entity.response;

import java.sql.Date;
import java.util.List;

public record TruthfulnessReportResponse(Integer grade,
										 Date date,
										 String content,
										 Integer articleNumber,
										 String articleShortDescription,
										 List<JournalistWithoutConnectionResponse> authors) {

}
