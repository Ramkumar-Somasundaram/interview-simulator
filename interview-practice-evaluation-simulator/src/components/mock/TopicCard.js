import React from 'react';
 
function TopicCard({ topic, onClick }) {
  return (
    <div className="col-12 col-md-6">
      <div className="card border-0 shadow-sm h-100 topic-card" onClick={onClick} role="button" tabIndex={0} onKeyDown={(e) => e.key === 'Enter' && onClick()}>
        <div className="card-body p-4">
          <div className="d-flex justify-content-between align-items-start gap-3">
            <div>
              <h4 className="h5 mb-2">{topic.name}</h4>
              <p className="text-muted mb-0">Questions available: {topic.questionCount}</p>
            </div>
            <span className={`badge ${topic.difficulty === 'HARD' ? 'text-bg-danger' : topic.difficulty === 'MEDIUM' ? 'text-bg-warning' : 'text-bg-success'}`}>
              {topic.difficulty}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
 
export default TopicCard;
 