import React from 'react';
 
function AuthFormCard({ title, subtitle, children }) {
  return (
    <div className="auth-shell">
      <div className="auth-card shadow-lg">
        <div className="mb-4 text-center">
          <span className="badge rounded-pill text-bg-light border px-3 py-2 mb-3">Interview Practice Evaluation Simulator</span>
          <h1 className="auth-title">{title}</h1>
          <p className="text-muted mb-0">{subtitle}</p>
        </div>
        {children}
      </div>
    </div>
  );
}
 
export default AuthFormCard;