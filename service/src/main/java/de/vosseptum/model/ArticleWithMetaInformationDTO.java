package de.vosseptum.model;

import de.vosseptum.sql.entity.response.ArticleResponse;

public record ArticleWithMetaInformationDTO(ArticleResponse article, Boolean useMongo) {

}
