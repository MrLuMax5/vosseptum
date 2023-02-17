package edu.imse.model;

import edu.imse.sql.entity.response.ArticleResponse;

public record ArticleWithMetaInformationDTO(ArticleResponse article, Boolean useMongo) {

}
