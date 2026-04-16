import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
 
function ProtectedRoute({ allowedRoles }) {
  const { isAuthenticated, role } = useAuth();
 
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
 
  if (allowedRoles && !allowedRoles.includes(role)) {
    return <Navigate to={role === 'ADMIN' ? '/admin/dashboard' : '/dashboard'} replace />;
  }
 
  return <Outlet />;
}
 
export default ProtectedRoute;
 