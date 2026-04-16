import React, { useEffect, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { analyticsService } from '../../services/analyticsService';
import TopNavbar from '../../components/layout/TopNavbar';
 
function UserDashboard() {
  const { username, logout } = useAuth();
  const navigate = useNavigate();
  const [heatmap, setHeatmap] = useState(null);
  const [selectedYear, setSelectedYear] = useState('current');
  const [showYearDropdown, setShowYearDropdown] = useState(false);
 
  const currentYear = new Date().getFullYear();
  const yearOptions = ['current', String(currentYear), String(currentYear - 1)];
 
  useEffect(() => {
    analyticsService.getHeatmap()
      .then(data => setHeatmap(data))
      .catch(() => {});
  }, []);
 
  // Close dropdown on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (!e.target.closest('[data-year-dropdown]')) {
        setShowYearDropdown(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);
 
  const handleLogout = () => {
    logout();
    navigate('/login');
  };
 
  const initial = username ? username.charAt(0).toUpperCase() : 'U';
 
  const toLocalDateStr = (date) => {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  };
 
  const getColor = (count, isToday) => {
    if (isToday && (!count || count === 0)) return '#b2dfdb';
    if (!count || count === 0) return '#ebedf0';
    if (count === 1) return '#9be9a8';
    if (count <= 3) return '#40c463';
    if (count <= 6) return '#30a14e';
    return '#216e39';
  };
 
  const getActivityMap = () => {
    if (heatmap && heatmap.activities) {
      const map = {};
      heatmap.activities.forEach(a => { map[a.date] = a.count; });
      return map;
    }
    return {};
  };
 
  const generateMonthColumns = () => {
    const today = new Date();
    const todayStr = toLocalDateStr(today);
    const activityMap = getActivityMap();
    const monthNames = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    const months = [];
 
    if (selectedYear === 'current') {
      // Always generate exactly 12 month columns ending with current month
      // e.g. if today is Apr 2026 → Apr 2025 ... Apr 2026
      for (let i = 11; i >= 0; i--) {
        // Calculate target month: (currentMonth - i) months ago
        let targetYear = today.getFullYear();
        let targetMonth = today.getMonth() - i;
 
        // Handle year underflow
        while (targetMonth < 0) {
          targetMonth += 12;
          targetYear -= 1;
        }
 
        const isCurrentMonth = (targetYear === today.getFullYear() && targetMonth === today.getMonth());
        const lastDayOfMonth = new Date(targetYear, targetMonth + 1, 0).getDate();
        const endDay = isCurrentMonth ? today.getDate() : lastDayOfMonth;
 
        const days = [];
        for (let d = 1; d <= endDay; d++) {
          const dateStr = `${targetYear}-${String(targetMonth + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
          days.push({
            date: dateStr,
            count: activityMap[dateStr] || 0,
            isToday: dateStr === todayStr
          });
        }
 
        months.push({
          label: monthNames[targetMonth],
          year: targetYear,
          days,
          startDow: new Date(targetYear, targetMonth, 1).getDay()
        });
      }
    } else {
      const yr = parseInt(selectedYear);
      for (let month = 0; month < 12; month++) {
        const lastDayOfMonth = new Date(yr, month + 1, 0).getDate();
        const days = [];
        for (let d = 1; d <= lastDayOfMonth; d++) {
          const dateStr = `${yr}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
          days.push({
            date: dateStr,
            count: activityMap[dateStr] || 0,
            isToday: dateStr === todayStr
          });
        }
        months.push({
          label: monthNames[month],
          year: yr,
          days,
          startDow: new Date(yr, month, 1).getDay()
        });
      }
    }
 
    return months;
  };
 
  const renderHeatmap = () => {
    const monthColumns = generateMonthColumns();
 
    const CELL = 13;
    const GAP = 2;
    const MONTH_GAP = 8;
    const DAY_LABEL_W = 28;
    const dayLabels = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
 
    return (
      <div style={{ overflowX: 'auto', paddingBottom: '8px' }}>
        <div style={{ display: 'flex', alignItems: 'flex-start' }}>
 
          {/* Day labels column */}
          <div style={{
            display: 'flex',
            flexDirection: 'column',
            marginRight: GAP,
            marginTop: CELL + GAP + 6
          }}>
            {dayLabels.map((d, i) => (
              <div key={d} style={{
                height: CELL,
                marginBottom: GAP,
                fontSize: '10px',
                color: '#888',
                lineHeight: `${CELL}px`,
                width: DAY_LABEL_W,
                textAlign: 'right',
                paddingRight: '4px',
                visibility: i % 2 !== 0 ? 'visible' : 'hidden'
              }}>
                {d}
              </div>
            ))}
          </div>
 
          {/* Month columns */}
          <div style={{ display: 'flex', gap: `${MONTH_GAP}px` }}>
            {monthColumns.map((monthData, mi) => {
              // Pad start based on day-of-week of 1st
              const startPad = monthData.startDow;
              const cells = [
                ...Array(startPad).fill(null),
                ...monthData.days
              ];
 
              // Group into week columns of 7 rows
              const weekCols = [];
              for (let i = 0; i < cells.length; i += 7) {
                const col = cells.slice(i, i + 7);
                while (col.length < 7) col.push(null);
                weekCols.push(col);
              }
 
              return (
                <div key={mi} style={{ display: 'flex', flexDirection: 'column' }}>
                  {/*  Month label always shown — positioned above this month's columns */}
                  <div style={{
                    fontSize: '11px',
                    color: '#444',
                    fontWeight: '600',
                    marginBottom: '4px',
                    whiteSpace: 'nowrap',
                    height: CELL,
                    lineHeight: `${CELL}px`
                  }}>
                    {monthData.label}
                  </div>
 
                  {/* Week columns side by side */}
                  <div style={{ display: 'flex', gap: `${GAP}px` }}>
                    {weekCols.map((col, wi) => (
                      <div key={wi} style={{
                        display: 'flex',
                        flexDirection: 'column',
                        gap: `${GAP}px`
                      }}>
                        {col.map((day, di) => (
                          <div
                            key={di}
                            title={day ? `${day.date}: ${day.count} submission${day.count !== 1 ? 's' : ''}` : ''}
                            style={{
                              width: CELL,
                              height: CELL,
                              borderRadius: '2px',
                              backgroundColor: day ? getColor(day.count, day.isToday) : 'transparent',
                              outline: day?.isToday ? '2px solid #0d9488' : 'none',
                              outlineOffset: '1px',
                              cursor: 'default'
                            }}
                          />
                        ))}
                      </div>
                    ))}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
 
        {/* Legend */}
        <div style={{
          display: 'flex', alignItems: 'center', gap: '4px',
          marginTop: '10px', fontSize: '12px', color: '#666',
          marginLeft: DAY_LABEL_W + GAP
        }}>
          <span>Less</span>
          {['#ebedf0','#9be9a8','#40c463','#30a14e','#216e39'].map(c => (
            <div key={c} style={{
              width: CELL, height: CELL,
              borderRadius: '2px', backgroundColor: c
            }} />
          ))}
          <span>More</span>
          <div style={{ marginLeft: '12px', display: 'flex', alignItems: 'center', gap: '4px' }}>
            <div style={{
              width: CELL, height: CELL, borderRadius: '2px',
              backgroundColor: '#b2dfdb',
              outline: '2px solid #0d9488', outlineOffset: '1px'
            }} />
            <span>Today</span>
          </div>
        </div>
      </div>
    );
  };
 
  return (
    <>
      <TopNavbar />
      <div className="d-flex" style={{ minHeight: 'calc(100vh - 56px)' }}>
 
        {/* Sidebar */}
        <div className="d-flex flex-column p-3" style={{
          width: '220px', minWidth: '220px',
          background: '#f8f9fa', borderRight: '1px solid #e9ecef'
        }}>
          <div className="text-center mb-4 mt-2">
            <div className="mx-auto mb-2 d-flex align-items-center justify-content-center"
              style={{
                width: '48px', height: '48px', borderRadius: '50%',
                background: '#0d9488', color: 'white',
                fontSize: '1.3rem', fontWeight: 'bold'
              }}>
              {initial}
            </div>
            <small className="text-muted d-block">Profile</small>
            <strong className="d-block">{username}</strong>
          </div>
          <hr />
          <nav className="flex-grow-1">
            {[
              { label: 'Dashboard', path: '/dashboard' },
              { label: 'Mock', path: '/mock' },
              { label: 'Analyze', path: '/analyze' },
              { label: 'History', path: '/history' }
            ].map(link => (
              <NavLink
                key={link.path}
                to={link.path}
                className="d-block py-2 px-3 rounded mb-1 text-decoration-none"
                style={({ isActive }) => ({
                  background: isActive ? '#e6f4f3' : 'transparent',
                  color: isActive ? '#0d9488' : '#333',
                  fontWeight: isActive ? '600' : '400'
                })}
              >
                {link.label}
              </NavLink>
            ))}
          </nav>
          <button className="btn btn-outline-danger w-100 mt-3" onClick={handleLogout}>
            Logout
          </button>
        </div>
 
        {/* Main Content */}
        <div className="flex-grow-1 p-4">
          <div className="mb-4">
            <h4 className="fw-bold">Welcome back, {username} </h4>
            <p className="text-muted">Practice with structure. Improve with evidence.</p>
          </div>
 
          {/* Stats Cards */}
          <div className="row g-3 mb-4">
            {[
              { label: 'Total active days', value: heatmap?.totalActiveDays ?? 0 },
              { label: 'Current streak', value: heatmap?.currentStreak ?? 0 },
              { label: 'Maximum streak', value: heatmap?.maxStreak ?? 0 },
              { label: 'Total submissions', value: heatmap?.totalSubmissions ?? 0 }
            ].map((s, i) => (
              <div className="col-6 col-md-3" key={i}>
                <div className="card text-center p-3 shadow-sm border-0">
                  <div style={{ fontSize: '2rem', fontWeight: 700, color: '#0d9488' }}>{s.value}</div>
                  <div className="text-muted" style={{ fontSize: '0.85rem' }}>{s.label}</div>
                </div>
              </div>
            ))}
          </div>
 
          {/* Heatmap Card */}
          <div className="card p-4 shadow-sm border-0 mb-4">
            <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
              <h6 className="fw-bold mb-0">
                {heatmap?.totalSubmissions ?? 0} submissions in the past one year
              </h6>
              <div className="d-flex align-items-center gap-3">
                <span className="text-muted" style={{ fontSize: '0.8rem' }}>
                  Total active days: <strong>{heatmap?.totalActiveDays ?? 0}</strong>
                  &nbsp;&nbsp;
                  Max streak: <strong>{heatmap?.maxStreak ?? 0}</strong>
                </span>
 
                {/* Year selector dropdown */}
                <div style={{ position: 'relative' }} data-year-dropdown>
                  <button
                    className="btn btn-sm btn-outline-secondary d-flex align-items-center gap-1"
                    onClick={() => setShowYearDropdown(v => !v)}
                    style={{ fontSize: '0.8rem' }}
                  >
                    {selectedYear === 'current' ? 'Current' : selectedYear}
                    <span>▾</span>
                  </button>
                  {showYearDropdown && (
                    <div style={{
                      position: 'absolute', right: 0, top: '110%',
                      background: 'white', border: '1px solid #e9ecef',
                      borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                      zIndex: 100, minWidth: '120px', padding: '4px 0'
                    }}>
                      {yearOptions.map(y => (
                        <div
                          key={y}
                          onClick={() => { setSelectedYear(y); setShowYearDropdown(false); }}
                          style={{
                            padding: '8px 16px', cursor: 'pointer',
                            fontSize: '0.85rem',
                            background: selectedYear === y ? '#e6f4f3' : 'white',
                            color: selectedYear === y ? '#0d9488' : '#333',
                            fontWeight: selectedYear === y ? '600' : '400'
                          }}
                          onMouseEnter={e => e.currentTarget.style.background = '#f5f5f5'}
                          onMouseLeave={e => e.currentTarget.style.background = selectedYear === y ? '#e6f4f3' : 'white'}
                        >
                          {y === 'current' ? 'Current' : y}
                          {selectedYear === y && ' ✓'}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            </div>
            {renderHeatmap()}
          </div>
 
          {/* Quick Links */}
          <div className="row g-3">
            {[
              { title: 'Mock Practice', desc: 'Answer domain-specific questions and get evaluated.', path: '/mock' },
              { title: 'Analytics', desc: 'See your score trends by domain.', path: '/analyze' },
              { title: 'History', desc: 'Review past attempts and keyword gaps.', path: '/history' }
            ].map((card, i) => (
              <div className="col-md-4" key={i}>
                <div
                  className="card p-3 shadow-sm border-0 h-100"
                  style={{ cursor: 'pointer' }}
                  onClick={() => navigate(card.path)}
                >
                  <h6 className="fw-bold">{card.title}</h6>
                  <p className="text-muted mb-0" style={{ fontSize: '0.875rem' }}>{card.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
}
 
export default UserDashboard;
