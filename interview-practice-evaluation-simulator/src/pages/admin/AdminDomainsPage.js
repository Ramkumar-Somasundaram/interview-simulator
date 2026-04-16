import React, { useEffect, useState } from 'react';
import AppShell from '../../components/layout/AppShell';
import AlertMessage from '../../components/common/AlertMessage';
import Loader from '../../components/common/Loader';
import DomainManager from '../../components/admin/DomainManager';
import TopicManager from '../../components/admin/TopicManager';
import {
  createDomain,
  createTopic,
  deleteDomain,
  deleteTopic,
  getAdminDomains,
  getTopicsByDomainAdmin,
  updateDomain,
  updateTopic
} from '../../services/adminService';
 
const items = [
  { label: 'Domains', to: '/admin/domain' },
  { label: 'Questions', to: '/admin/question' },
  { label: 'Analysis', to: '/admin/analyze' },
   { label: 'User Dashboard',to: '/admin/user-view'}
];
 
function AdminDomainsPage() {
  const [domains, setDomains] = useState([]);
  const [selectedDomain, setSelectedDomain] = useState(null);
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });
 
  const loadDomains = async () => {
    const response = await getAdminDomains();
    setDomains(response.data || []);
  };
 
  useEffect(() => {
    const init = async () => {
      try {
        await loadDomains();
      } catch (error) {
        setMessage({ type: 'danger', text: error.response?.data?.message || 'Failed to load domains' });
        setTimeout(() => setMessage({ type: '', text: '' }), 4000);
      } finally {
        setLoading(false);
      }
    };
    init();
  }, []);
 
  const handleManageTopics = async (domain) => {
    setSelectedDomain(domain);
    try {
      const response = await getTopicsByDomainAdmin(domain.id);
      setTopics(response.data || []);
    } catch (error) {
      setMessage({ type: 'danger', text: error.response?.data?.message || 'Failed to load topics' });
      setTimeout(() => setMessage({ type: '', text: '' }), 4000);
    }
  };
 
  const action = async (callback, successText) => {
    try {
      await callback();
      await loadDomains();
      if (selectedDomain) await handleManageTopics(selectedDomain);
      setMessage({ type: 'success', text: successText });
      setTimeout(() => setMessage({ type: '', text: '' }), 4000);
    } catch (error) {
      setMessage({ type: 'danger', text: error.response?.data?.message || 'Action failed' });
    }
  };
 
  return (
    <AppShell>
      <div className="section-header mb-4">
        <h1 className="page-title">Admin Domains</h1>
        <p className="text-muted mb-0">Create, edit, publish, and remove domains and topics.</p>
      </div>
      <AlertMessage variant={message.type || 'danger'} message={message.text} />
      {loading ? <Loader text="Loading admin domains..." /> : (
        <>
          <DomainManager
            domains={domains}
            onCreate={(payload) => action(() => createDomain(payload), 'Domain created successfully')}
            onUpdate={(id, payload) => action(() => updateDomain(id, payload), 'Domain updated successfully')}
            onDelete={(id) => action(() => deleteDomain(id), 'Domain deleted successfully')}
            onManageTopics={handleManageTopics}
          />
          {selectedDomain && (
            <TopicManager
              domain={selectedDomain}
              topics={topics}
              onCreate={(payload) => action(async () => { await createTopic(payload); await handleManageTopics(selectedDomain); }, 'Topic created successfully')}
              onUpdate={(id, payload) => action(async () => { await updateTopic(id, payload); await handleManageTopics(selectedDomain); }, 'Topic updated successfully')}
              onDelete={(id) => action(async () => { await deleteTopic(id); await handleManageTopics(selectedDomain); }, 'Topic deleted successfully')}
            />
          )}
        </>
      )}
    </AppShell>
  );
}
 
export default AdminDomainsPage;