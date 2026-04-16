import React from 'react';
 
function DomainCard({ domain, gradient, onClick }) {
  return (
    <div className="col-12 col-sm-6 col-lg-4">
      <div className="domain-card h-100" style={{ backgroundImage: gradient }} onClick={onClick} role="button" tabIndex={0} onKeyDown={(e) => e.key === 'Enter' && onClick()}>
        <div className="domain-card-content">
          <div>
            <p className="domain-subtitle">Practice Interview Questions</p>
            <h3 className="domain-title">{domain.name}</h3>
            <p className="domain-description mb-0">{domain.description || 'Sharpen your interview answers with structured evaluation.'}</p>
          </div>
          <button type="button" className="btn btn-light fw-semibold mt-4">
            Get Started
          </button>
        </div>
      </div>
    </div>
  );
}
 
export default DomainCard;
 