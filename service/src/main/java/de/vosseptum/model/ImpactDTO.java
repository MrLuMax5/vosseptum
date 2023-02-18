package de.vosseptum.model;

import java.math.BigDecimal;
import org.bson.Document;

public record ImpactDTO(String email, Double avg_popularity, Double avg_budget) {

	public static ImpactDTO from(Object[] objects) {
		return new ImpactDTO((String) objects[0], ((BigDecimal) objects[1]).doubleValue(), ((BigDecimal) objects[2]).doubleValue());
	}

	public static ImpactDTO fromMongo(Document doc) {
		return new ImpactDTO(doc.getString("_id"), (doc.getDouble("avg_popularity")), (doc.getDouble("avg_budget")));
	}
}
