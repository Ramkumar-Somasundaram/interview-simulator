import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AppShell from '../../components/layout/AppShell';
import Loader from '../../components/common/Loader';
import AlertMessage from '../../components/common/AlertMessage';
import DomainCard from '../../components/mock/DomainCard';
import { getUserDomains } from '../../services/mockService';
import { domainGradients, slugify } from '../../utils/helpers';
 
const items = [
  { label: 'Mock', to: '/mock' },
  { label: 'Analyze', to: '/analyze' },
  { label: 'History', to: '/history' }
];
 
function MockPage() {
  const navigate = useNavigate();
  const [domains, setDomains] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
 
  useEffect(() => {
    const load = async () => {
      try {
        const response = await getUserDomains();
        setDomains(response.data || []);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load domains');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);
 
  return (
    <AppShell >
      <div className="section-header mb-4">
        <h1 className="page-title">Mock Practice</h1>
        <p className="text-muted mb-0">Choose a live domain and start your structured interview simulation.</p>
      </div>
      <AlertMessage message={error} />
      {loading ? <Loader text="Loading domains..." /> : (
        <div className="row g-4">
          {domains.map((domain, index) => (
            <DomainCard
              key={domain.id}
              domain={domain}
              gradient={domainGradients[index % domainGradients.length]}
              onClick={() => navigate(`/mock/${slugify(domain.name)}`, { state: { domain } })}
            />
          ))}
        </div>
      )}
    </AppShell>
  );
}
 
export default MockPage;
 