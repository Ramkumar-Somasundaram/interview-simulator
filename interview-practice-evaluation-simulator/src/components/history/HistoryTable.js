import React from 'react';
import { formatDateTime } from '../../utils/helpers';
 
function HistoryTable({ items, onSelect }) {
  return (
    <div className="table-responsive shadow-sm rounded-4 overflow-hidden bg-white">
      <table className="table table-hover align-middle mb-0">
        <thead className="table-light">
          <tr>
            <th>Domain</th>
            <th>Topic</th>
            <th>Difficulty</th>
            <th>Date</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          {items.length ? (
            items.map((item) => (
              <tr key={item.sessionId} className="history-row" role="button" tabIndex={0} onClick={() => onSelect(item)} onKeyDown={(e) => e.key === 'Enter' && onSelect(item)}>
                <td>{item.domainName}</td>
                <td>{item.topicName}</td>
                <td><span className="badge text-bg-secondary">{item.difficulty}</span></td>
                <td>{formatDateTime(item.attendedAt)}</td>
                <td>{item.totalScore}/{item.maxPossibleScore} ({item.percentageScore}%)</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" className="text-center py-5 text-muted">No history available yet.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
 
export default HistoryTable;
 