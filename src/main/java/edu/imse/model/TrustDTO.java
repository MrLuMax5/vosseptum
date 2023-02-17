package edu.imse.model;

import edu.imse.sql.entity.response.JournalistWithoutConnectionResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.bson.Document;

public record TrustDTO(float avgGrade, JournalistWithoutConnectionResponse journalist) {

	public static TrustDTO from(Object[] obj) {
		return new TrustDTO(((BigDecimal) obj[0]).floatValue(), new JournalistWithoutConnectionResponse(((BigInteger) obj[1]).longValue(), (String) obj[2], "", ""));
	}

	public static TrustDTO fromMongo(Document doc) {
		return new TrustDTO(((Double) doc.get("avg_grade")).floatValue(), new JournalistWithoutConnectionResponse(0L, (String) doc.get("_id"), "", ""));
	}
}
