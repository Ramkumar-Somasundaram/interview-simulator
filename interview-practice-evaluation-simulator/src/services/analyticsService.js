import api from './api';
 
export const analyticsService = {
  getHeatmap: () => api.get('/api/user/analytics/heatmap').then(r => r.data.data),
  getScoreTrend: () => api.get('/api/user/analytics/trends').then(r => r.data.data)
};