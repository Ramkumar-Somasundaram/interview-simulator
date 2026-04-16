import api from './api';
 
const unwrap = (response) => response.data;
 
// GET all domains that have questions (for user mock page)
export const getUserDomains = async () =>
  unwrap(await api.get('/api/user/mock/domains'));
 
// GET topics by domain ID
export const getTopicsByDomainUser = async (domainId) =>
  unwrap(await api.get(`/api/user/mock/domains/${domainId}/topics`));
 
// GET questions by domainId + topicId + difficulty
// Backend: GET /api/user/mock/questions?domainId=1&topicId=2&difficulty=EASY
export const getQuestionsByTopicUser = async (domainId, topicId, difficulty) =>
  unwrap(await api.get('/api/user/mock/questions', {
    params: { domainId, topicId, difficulty }
  }));
 
// POST submit answers for evaluation
// Backend: POST /api/user/mock/submit
export const submitAnswers = async (payload) =>
  unwrap(await api.post('/api/user/mock/submit', payload));
 
// GET performance/evaluation by sessionId
// Backend: GET /api/user/mock/session/{sessionId}/performance
export const getEvaluationBySession = async (sessionId) =>
  unwrap(await api.get(`/api/user/mock/session/${sessionId}/performance`));
 
// GET user history
// Backend: GET /api/user/history  (check your HistoryController mapping)
export const getHistory = async () =>
  unwrap(await api.get('/api/user/history'));
 