package de.vosseptum.sql.entity.services;

import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.EditorialBoard;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.Topic;
import de.vosseptum.sql.entity.TruthfulnessReport;
import de.vosseptum.sql.entity.bridges.ArticleCoversTopic;
import de.vosseptum.sql.entity.bridges.JournalistInBoard;
import de.vosseptum.sql.entity.bridges.JournalistInterestedTopic;
import de.vosseptum.sql.entity.bridges.JournalistWrittenArticle;
import de.vosseptum.sql.entity.bridges.JournalistWrittenReport;
import de.vosseptum.sql.entity.keys.ArticleCoversTopicKey;
import de.vosseptum.sql.entity.keys.JournalistInBoardKey;
import de.vosseptum.sql.entity.keys.JournalistInterestedTopicKey;
import de.vosseptum.sql.entity.keys.JournalistWrittenArticleKey;
import de.vosseptum.sql.entity.keys.JournalistWrittenReportKey;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BridgeService {

	public void fill(int size) {
		int articleCount = (int) Article.count();
		List<Article> articles = Article.listAll();
		int topicCount = (int) Topic.count();
		List<Topic> topics = Topic.listAll();
		int journalistCount = (int) Journalist.count();
		List<Journalist> journalists = Journalist.listAll();
		int boardCount = (int) EditorialBoard.count();
		List<EditorialBoard> boards = EditorialBoard.listAll();
		int reportCount = (int) TruthfulnessReport.count();
		List<TruthfulnessReport> reports = TruthfulnessReport.listAll();
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			int number = random.nextInt(1000);
			createArticleCoversTopic(number, articleCount, articles, topicCount, topics);
			createJournalistInBoard(number, journalistCount, journalists, boardCount, boards);
			createJournalistInterestedTopic(number, journalistCount, journalists, topicCount, topics);
			createJournalistWrittenReport(number, journalistCount, journalists, reportCount, reports);
		}
		addAuthorsToArticles(journalistCount, journalists, articleCount, articles);
		addAuthorsToReports(journalistCount, journalists, reportCount, reports);
		addConnections(journalistCount, journalists);
	}

	private void createArticleCoversTopic(int randomNumber, int articleCount, List<Article> articles, int topicCount, List<Topic> topics) {
		ArticleCoversTopic articleCoversTopic = new ArticleCoversTopic();
		ArticleCoversTopicKey key = new ArticleCoversTopicKey();
		key.article = articles.get(randomNumber % articleCount);
		key.topic = topics.get(randomNumber % topicCount);
		articleCoversTopic.articleCoversTopicKey = key;
		if (ArticleCoversTopic.findById(key) == null) {
			articleCoversTopic.persist();
		}
	}

	private void createJournalistInBoard(int randomNumber, int journalistCount, List<Journalist> journalists, int boardCount, List<EditorialBoard> boards) {
		JournalistInBoard journalistInBoard = new JournalistInBoard();
		JournalistInBoardKey key = new JournalistInBoardKey();
		key.journalist = journalists.get(randomNumber % journalistCount);
		key.editorialBoard = boards.get(randomNumber % boardCount);
		journalistInBoard.journalistInBoardKey = key;
		journalistInBoard.date = new Date(randomNumber * 1000000L);
		if (JournalistInBoard.findById(key) == null) {
			journalistInBoard.persist();
		}
	}

	private void createJournalistInterestedTopic(int randomNumber, int journalistCount, List<Journalist> journalists, int topicCount, List<Topic> topics) {
		JournalistInterestedTopic journalistInterestedTopic = new JournalistInterestedTopic();
		JournalistInterestedTopicKey key = new JournalistInterestedTopicKey();
		key.journalist = journalists.get(randomNumber % journalistCount);
		key.topic = topics.get(randomNumber % topicCount);
		journalistInterestedTopic.journalistInterestedTopicKey = key;
		if (JournalistInterestedTopic.findById(key) == null) {
			journalistInterestedTopic.persist();
		}
	}

	private void createJournalistWrittenReport(int randomNumber, int journalistCount, List<Journalist> journalists, int reportCount, List<TruthfulnessReport> reports) {
		JournalistWrittenReport journalistWrittenReport = new JournalistWrittenReport();
		JournalistWrittenReportKey key = new JournalistWrittenReportKey();
		key.journalist = journalists.get(randomNumber % journalistCount);
		key.report = reports.get(randomNumber % reportCount);
		journalistWrittenReport.journalistWrittenReportKey = key;
		if (JournalistWrittenReport.findById(key) == null){
			journalistWrittenReport.persist();
		}
	}

	private void addAuthorsToArticles(int journalistCount, List<Journalist> journalists, int articleCount, List<Article> articles) {
		for(int i = 0; i < articleCount; i++) {
			JournalistWrittenArticleKey key1 = new JournalistWrittenArticleKey();
			JournalistWrittenArticleKey key2 = new JournalistWrittenArticleKey();
			key1.article = articles.get(i % articleCount);
			key2.article = articles.get((i + 1) % articleCount);
			key1.journalist = journalists.get((i + (journalistCount / 2)) % journalistCount);
			key2.journalist = journalists.get((i + (journalistCount / 2) + 1) % journalistCount);
			persistIfUniqueJWKKey(key1);
			persistIfUniqueJWKKey(key2);
		}
	}

	private void addAuthorsToReports(int journalistCount, List<Journalist> journalists, int reportCount, List<TruthfulnessReport> reports) {
		for (int i = 0; i < reportCount; i++) {
			JournalistWrittenReportKey key1 = new JournalistWrittenReportKey();
			JournalistWrittenReportKey key2 = new JournalistWrittenReportKey();
			key1.report = reports.get((i + 5) % reportCount);
			key2.report = reports.get((i + 6) % journalistCount);
			key1.journalist = journalists.get((i + (journalistCount / 2)) % journalistCount);
			key2.journalist = journalists.get((i + (journalistCount / 2) + 1) % journalistCount);
			persistIfUniqueJWRKey(key1);
			persistIfUniqueJWRKey(key2);
		}
	}

	private void addConnections(int journalistCount, List<Journalist> journalists) {
		for (int i = 0; i < journalistCount; i++) {
			Journalist journalist = journalists.get(i);
			Journalist connectionOf = journalists.get((i + 1) % journalistCount);
			journalist.connections.add(connectionOf);
			journalist.persist();
		}
	}

	private void persistIfUniqueJWRKey(JournalistWrittenReportKey key) {
		if (JournalistWrittenReport.findById(key) == null) {
			JournalistWrittenReport report = new JournalistWrittenReport();
			report.journalistWrittenReportKey = key;
			report.persist();
		}
	}

	private void persistIfUniqueJWKKey(JournalistWrittenArticleKey key) {
		if (JournalistWrittenArticle.findById(key) == null) {
			JournalistWrittenArticle article = new JournalistWrittenArticle();
			article.journalistWrittenArticleKey = key;
			article.persist();
		}
	}
}
