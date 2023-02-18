package de.vosseptum.helper;

public class SophoraIdHelper {

	private SophoraIdHelper() {

	}

	public static String getDescriptionFromMongoArticle(String descriptionWithNumber) {
		try {
			return descriptionWithNumber.substring(0, descriptionWithNumber.indexOf("-"));
		} catch (StringIndexOutOfBoundsException e) {
			return descriptionWithNumber;
		}
	}

	public static Integer getNumberFromMongoArticle(String descriptionWithNumber) {
		return Integer.valueOf(descriptionWithNumber.substring(descriptionWithNumber.indexOf("-") + 1));
	}

}
