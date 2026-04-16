import axios from 'axios';
 
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});
 
api.interceptors.request.use((config) => {
  const raw = localStorage.getItem('ipes_auth');
  if (raw) {
    const auth = JSON.parse(raw);
    if (auth?.token) {
      config.headers.Authorization = `Bearer ${auth.token}`;
    }
  }
  return config;
});
 
api.interceptors.response.use(
  (response) => response,
  (error) => Promise.reject(error)
);
 
export default api;
 