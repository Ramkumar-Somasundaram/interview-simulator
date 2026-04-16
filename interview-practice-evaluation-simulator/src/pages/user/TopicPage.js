import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import AppShell from '../../components/layout/AppShell';
import Loader from '../../components/common/Loader';
import AlertMessage from '../../components/common/AlertMessage';
import TopicCard from '../../components/mock/TopicCard';
import { getUserDomains, getTopicsByDomainUser } from '../../services/mockService';
import { slugify } from '../../utils/helpers';
 
const items = [
  { label: 'Mock', to: '/mock' },
  { label: 'Analyze', to: '/analyze' },
  { label: 'History', to: '/history' }
];
 
function TopicPage() {
  const { domainSlug } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const [domain, setDomain] = useState(location.state?.domain || null);
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
 
  useEffect(() => {
    const load = async () => {
      try {
        let selectedDomain = domain;
        if (!selectedDomain) {
          const domainResponse = await getUserDomains();
          selectedDomain = (domainResponse.data || []).find((item) => slugify(item.name) === domainSlug);
          setDomain(selectedDomain || null);
        }
        if (!selectedDomain) {
          setError('Domain not found');
          return;
        }
        const response = await getTopicsByDomainUser(selectedDomain.id);
        setTopics(response.data || []);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load topics');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [domain, domainSlug]);
 
  return (
    <AppShell>
      <div className="section-header mb-4">
        <h1 className="page-title">{domain?.name || 'Domain Topics'}</h1>
        <p className="text-muted mb-0">Choose a topic and difficulty combination to start the mock test.</p>
      </div>
      <AlertMessage message={error} />
    {loading ? (
  <Loader text="Loading topics..." />
) : (
  <div className="row g-4">
    {topics.map((topic) => (
      <div className="col-12" key={topic.id}>
        <TopicCard
          topic={topic}
          onClick={() =>
            navigate(`/mock/${domainSlug}/${topic.id}`, {
              state: { topic, domain }
            })
          }
        />
      </div>
    ))}
  </div>
)}
    </AppShell>
  );
}
 
export default TopicPage;