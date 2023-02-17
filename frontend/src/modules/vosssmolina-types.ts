export interface Journalist {
  id?: number;
  email?: string;
  password?: string;
  location?: string;
}

export interface FillResponse {
  entityCount: number;
  bridgeCount: number;
}

export interface Article {
  authors: Journalist[];
  topics: Topic[];
  anonymous: boolean;
  shortDescription: string
  number: number;
  content: string;
}

export interface Topic {
  subject: string;
  popularity?: number;
  category?: string;
}

export interface InterestedJournalist {
  id: number;
  topic: string;
}

export interface EditorialBoard {
  institution: string;
  topic: string;
  budget: number;
  reputation: number;
}

export interface ArticleEditData {
  article: Article;
  useMongo: boolean;
  isOwnArticle: boolean;
  connections: Journalist[];
}

export interface TruthfulnessReport {
  authors: Journalist[];
  grade: number;
  content: number;
  articleNumber: number;
  articleShortDescription: string;
}

export interface TruthfulnessReportData {
  report: TruthfulnessReport;
  connections: Journalist[];
}

export interface ArticleKey {
  shortDescription: string
  number: number;
}

export interface TrustResponse {
  avgGrade: number;
  journalist: Journalist;
}

export interface PopularityResponse {
  email: string;
  avg_popularity: number;
  avg_budget: number;
}
