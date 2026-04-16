import api from './api';
 
const unwrap = (response) => response.data;
 
export const registerUser = async (payload) => unwrap(await api.post('/api/auth/register', payload));
export const loginUser = async (payload) => unwrap(await api.post('/api/auth/login', payload));