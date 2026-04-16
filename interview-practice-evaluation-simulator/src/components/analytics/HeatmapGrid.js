import React from 'react';
 
function getClassName(count) {
  if (!count) return 'heat-cell level-0';
  if (count === 1) return 'heat-cell level-1';
  if (count <= 3) return 'heat-cell level-2';
  return 'heat-cell level-3';
}
 
function HeatmapGrid({ activities = [] }) {
  return (
    <div className="heatmap-wrapper">
      <div className="heatmap-grid">
        {activities.map((item) => (
          <div key={item.date} className={getClassName(item.count)} title={`${item.date}: ${item.count} submissions`} />
        ))}
      </div>
      <div className="d-flex gap-2 align-items-center mt-3 text-muted small">
        <span>Less</span>
        <span className="heat-legend level-0" />
        <span className="heat-legend level-1" />
        <span className="heat-legend level-2" />
        <span className="heat-legend level-3" />
        <span>More</span>
      </div>
    </div>
  );
}
 
export default HeatmapGrid;
 