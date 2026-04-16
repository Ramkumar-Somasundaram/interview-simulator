import React from 'react';
import { Link } from 'react-router-dom';
 
function NotFoundPage() {
  return (
    <div className="auth-shell">
      <div className="auth-card shadow-lg text-center">
        <h1 className="auth-title">404</h1>
        <p className="text-muted mb-4">The page you are looking for does not exist.</p>
        <Link to="/login" className="btn btn-primary">Go to Login</Link>
      </div>
    </div>
  );
}
 
export default NotFoundPage;