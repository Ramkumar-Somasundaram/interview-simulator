import React, { useEffect, useState } from 'react';
import AppShell from '../../components/layout/AppShell';
import Loader from '../../components/common/Loader';
import AlertMessage from '../../components/common/AlertMessage';
import { getAdminDomains, getTopicsByDomainAdmin } from '../../services/adminService';
 
const items = [
  { label: 'Domains', to: '/admin/domain' },
  { label: 'Questions', to: '/admin/question' },
  { label: 'Analysis', to: '/admin/analyze' },
  { label: 'User Dashboard', to: '/admin/user-view' },
];
 
function AdminUserViewPage() {
  const [domains, setDomains] = useState([]);
  const [expandedDomain, setExpandedDomain] = useState(null);
  const [topicsMap, setTopicsMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });
 
  useEffect(() => {
    const load = async () => {
      try {
        const res = await getAdminDomains();
        const all = res.data || [];
        // Show only domains that have questions (visible to users)
        setDomains(all.filter((d) => d.hasQuestions));
      } catch {
        setMessage({ type: 'danger', text: 'Failed to load user view data' });
        setTimeout(() => setMessage({ type: '', text: '' }), 4000);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);
 
  const handleExpand = async (domainId) => {
    if (expandedDomain === domainId) {
      setExpandedDomain(null);
      return;
    }
    setExpandedDomain(domainId);
    if (!topicsMap[domainId]) {
      try {
        const res = await getTopicsByDomainAdmin(domainId);
        setTopicsMap((prev) => ({ ...prev, [domainId]: res.data || [] }));
      } catch {
        setMessage({ type: 'danger', text: 'Failed to load topics' });
        setTimeout(() => setMessage({ type: '', text: '' }), 4000);
      }
    }
  };
 
  const getDifficultyColor = (difficulty) => {
    if (difficulty === 'EASY') return '#22c55e';
    if (difficulty === 'MEDIUM') return '#f59e0b';
    if (difficulty === 'HARD') return '#ef4444';
    return '#6b7280';
  };
 
  const gradients = [
    'linear-gradient(135deg, #667eea, #764ba2)',
    'linear-gradient(135deg, #f093fb, #f5576c)',
    'linear-gradient(135deg, #4facfe, #00f2fe)',
    'linear-gradient(135deg, #43e97b, #38f9d7)',
    'linear-gradient(135deg, #fa709a, #fee140)',
    'linear-gradient(135deg, #a18cd1, #fbc2eb)',
  ];
 
  return (
    <AppShell sidebarItems={items}>
      <div className="mb-4">
        <div className="d-flex align-items-center gap-2 mb-1">
          <span className="badge px-3 py-1 rounded-pill"
            style={{ background: 'var(--color-primary-highlight)', color: 'var(--color-primary)', fontSize: '0.75rem' }}>
            View Only
          </span>
        </div>
        <h1 className="fw-bold mb-1" style={{ fontSize: 'clamp(1.4rem, 3vw, 2rem)' }}>
          User Dashboard Preview
        </h1>
        <p className="text-muted mb-0">
          This is how the mock practice page appears to users. You cannot submit answers in this view.
        </p>
      </div>
 
      <AlertMessage variant={message.type || 'danger'} message={message.text} />
 
      {loading ? <Loader text="Loading user view..." /> : (
        <>
          {domains.length === 0 ? (
            <div className="text-center py-5">
              <p className="text-muted">No domains with questions are live for users yet.</p>
              <small className="text-muted">Add questions to a domain in the Questions section to make it visible to users.</small>
            </div>
          ) : (
            <>
              {/* Domain Cards Grid */}
              <div className="row g-4 mb-5">
                {domains.map((domain, index) => (
                  <div key={domain.id} className="col-12 col-md-6 col-lg-4">
                    <div
                      className="card border-0 h-100"
                      style={{
                        background: gradients[index % gradients.length],
                        borderRadius: '16px',
                        cursor: 'pointer',
                        transition: 'transform 0.2s, box-shadow 0.2s',
                        boxShadow: '0 4px 15px rgba(0,0,0,0.15)'
                      }}
                      onClick={() => handleExpand(domain.id)}
                      onMouseEnter={(e) => {
                        e.currentTarget.style.transform = 'scale(1.03)';
                        e.currentTarget.style.boxShadow = '0 8px 25px rgba(0,0,0,0.2)';
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.transform = 'scale(1)';
                        e.currentTarget.style.boxShadow = '0 4px 15px rgba(0,0,0,0.15)';
                      }}
                    >
                      <div className="card-body p-4 text-white">
                        <h3 className="fw-bold mb-1">{domain.name}</h3>
                        <p className="mb-3 opacity-75" style={{ fontSize: '0.875rem' }}>
                          {domain.description || 'Practice Interview Questions'}
                        </p>
                        <div className="d-flex align-items-center justify-content-between">
                          <span className="opacity-75" style={{ fontSize: '0.8rem' }}>
                            {domain.topics?.length || 0} topics
                          </span>
                          <button
                            className="btn btn-sm text-white fw-semibold"
                            style={{
                              background: 'rgba(255,255,255,0.25)',
                              border: '1px solid rgba(255,255,255,0.4)',
                              borderRadius: '20px',
                              padding: '4px 16px'
                            }}
                            onClick={(e) => { e.stopPropagation(); handleExpand(domain.id); }}
                          >
                            {expandedDomain === domain.id ? 'Hide Topics' : 'View Topics'}
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
 
              {/* Topics for expanded domain */}
              {expandedDomain && (
                <div className="card border-0 shadow-sm p-4"
                  style={{ borderRadius: '16px', background: 'var(--color-surface)' }}>
                  <h4 className="fw-bold mb-3">
                    Topics for: {domains.find((d) => d.id === expandedDomain)?.name}
                  </h4>
                  {!topicsMap[expandedDomain] ? (
                    <Loader text="Loading topics..." />
                  ) : topicsMap[expandedDomain].length === 0 ? (
                    <p className="text-muted">No topics found.</p>
                  ) : (
                    <div className="row g-3">
                      {topicsMap[expandedDomain].map((topic) => (
                        <div key={topic.id} className="col-12 col-md-6 col-lg-4">
                          <div className="card border p-3 h-100"
                            style={{ borderRadius: '12px', background: 'var(--color-surface-2)' }}>
                            <div className="d-flex justify-content-between align-items-start">
                              <div>
                                <h6 className="fw-semibold mb-1">{topic.name}</h6>
                                <span className="badge rounded-pill px-2 py-1"
                                  style={{
                                    background: getDifficultyColor(topic.difficulty) + '22',
                                    color: getDifficultyColor(topic.difficulty),
                                    fontSize: '0.75rem'
                                  }}>
                                  {topic.difficulty}
                                </span>
                              </div>
                              <span className="text-muted small">{topic.questionCount || 0} Qs</span>
                            </div>
                            <div className="mt-3">
                              <button
                                className="btn btn-sm w-100 disabled"
                                style={{
                                  background: 'var(--color-surface-offset)',
                                  color: 'var(--color-text-muted)',
                                  borderRadius: '8px',
                                  cursor: 'not-allowed'
                                }}
                                disabled
                                title="Admin view only — users can start practice from here"
                              >
                                Get Started (View Only)
                              </button>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </>
          )}
        </>
      )}
    </AppShell>
  );
}
 
export default AdminUserViewPage;