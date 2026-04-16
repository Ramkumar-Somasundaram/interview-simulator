import api from './api';
 
const unwrap = (response) => response.data;
 
export const getAdminDomains = async () => unwrap(await api.get('/api/admin/domains'));
export const createDomain = async (payload) => unwrap(await api.post('/api/admin/domains', payload));
export const updateDomain = async (id, payload) => unwrap(await api.put(`/api/admin/domains/${id}`, payload));
export const deleteDomain = async (id) => unwrap(await api.delete(`/api/admin/domains/${id}`));
 
export const getTopicsByDomainAdmin = async (domainId) => unwrap(await api.get(`/api/admin/topics/domain/${domainId}`));
export const createTopic = async (payload) => unwrap(await api.post('/api/admin/topics', payload));
export const updateTopic = async (id, payload) => unwrap(await api.put(`/api/admin/topics/${id}`, payload));
export const deleteTopic = async (id) => unwrap(await api.delete(`/api/admin/topics/${id}`));
 
export const createQuestion = async (payload) => unwrap(await api.post('/api/admin/questions', payload));
export const updateQuestion = async (id, payload) => unwrap(await api.put(`/api/admin/questions/${id}`, payload));
export const deleteQuestion = async (id) => unwrap(await api.delete(`/api/admin/questions/${id}`));
export const getQuestionsByTopicAdmin = async (topicId) => unwrap(await api.get(`/api/admin/questions/topic/${topicId}`));
export const getAdminAnalysis = async () => unwrap(await api.get('/api/admin/analysis/most-attempted-domains'));
 