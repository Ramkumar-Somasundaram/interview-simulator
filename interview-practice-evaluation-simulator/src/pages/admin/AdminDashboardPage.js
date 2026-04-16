import React from 'react';
import AppShell from '../../components/layout/AppShell';
import './AdminDashboard.css'
 
const items = [
  { label: 'Domains', to: '/admin/domain' },
  { label: 'Questions', to: '/admin/question' },
  { label: 'Analysis', to: '/admin/analyze' },
  { label: 'User Dashboard',to: '/admin/user-view'}
];
 
function AdminDashboardPage() {
  return (
    <AppShell sidebarItems={items} footerLink={{ label: 'Admin Dashboard', to: '/dashboard' }}>
      <div className="dashboard-hero-card shadow-sm">
        <span className="badge text-bg-light border mb-3">Admin Dashboard</span>
        <h1 className="page-title">Control domains, topics, questions, and analytics.</h1>
        <p className="text-muted mb-0">Manage the assessment content lifecycle and monitor which domains users practice the most.</p>
      </div>
      <div className="row g-4 mt-1">
        <div className="col-12 col-md-4"><div className="metric-card shadow-sm"><h2>Domains</h2><p>Create and publish mock interview domains with topics and visibility rules.</p></div></div>
        <div className="col-12 col-md-4"><div className="metric-card shadow-sm"><h2>Questions</h2><p>Configure subjective questions, keywords, and rubric scores for evaluation.</p></div></div>
        <div className="col-12 col-md-4"><div className="metric-card shadow-sm"><h2>Analysis</h2><p>Inspect most attempted domains and usage trends across the platform.</p></div></div>
      </div>
    </AppShell>
  );
}
 
export default AdminDashboardPage;