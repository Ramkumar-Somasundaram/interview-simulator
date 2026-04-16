import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
 
const AuthContext = createContext(null);
const STORAGE_KEY = 'ipes_auth';
 
export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  });
 
  useEffect(() => {
    if (auth) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }, [auth]);
 
  const login = (payload) => setAuth(payload);
  const logout = () => setAuth(null);
 
  const value = useMemo(
    () => ({
      auth,
      token: auth?.token || null,
      role: auth?.role || null,
      isAuthenticated: Boolean(auth?.token),
      user: auth || null,
      login,
      logout
    }),
    [auth]
  );
 
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
 
export function useAuth() {
  return useContext(AuthContext);
}
 