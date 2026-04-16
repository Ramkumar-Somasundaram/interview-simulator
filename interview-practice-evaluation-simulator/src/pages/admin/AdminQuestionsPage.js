import React, { useEffect, useState } from 'react';
import AppShell from '../../components/layout/AppShell';
import AlertMessage from '../../components/common/AlertMessage';
import Loader from '../../components/common/Loader';
import QuestionManager from '../../components/admin/QuestionManager';
import {
  createQuestion,
  deleteQuestion,
  getAdminDomains,
  getQuestionsByTopicAdmin,
  getTopicsByDomainAdmin
} from '../../services/adminService';
 
const items = [
  { label: 'Domains', to: '/admin/domain' },
  { label: 'Questions', to: '/admin/question' },
  { label: 'Analysis', to: '/admin/analyze' },
   { label: 'User Dashboard',to: '/admin/user-view'}
];
 
function AdminQuestionsPage() {
  const [domains, setDomains] = useState([]);
  const [topicsMap, setTopicsMap] = useState({});
  const [questions, setQuestions] = useState([]);
  const [selectedDomainId, setSelectedDomainId] = useState('');
  const [selectedTopicId, setSelectedTopicId] = useState('');
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });
 
  useEffect(() => {
    const load = async () => {
      try {
        const response = await getAdminDomains();
        setDomains(response.data || []);
      } catch (error) {
        setMessage({ type: 'danger', text: error.response?.data?.message || 'Failed to load question configuration data' });
        setTimeout(() => setMessage({ type: '', text: '' }), 4000);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);
 
  const loadTopics = async (domainId) => {
    setSelectedDomainId(domainId);
    setSelectedTopicId('');
    setQuestions([]);
    if (!domainId) return;
    try {
      const response = await getTopicsByDomainAdmin(domainId);
      setTopicsMap((prev) => ({ ...prev, [domainId]: response.data || [] }));
    } catch (error) {
      setMessage({ type: 'danger', text: error.response?.data?.message || 'Failed to load topics' });
      setTimeout(() => setMessage({ type: '', text: '' }), 4000);
    }
  };
 
  const loadQuestions = async (topicId) => {
    setSelectedTopicId(topicId);
    if (!topicId) return;
    try {
      const response = await getQuestionsByTopicAdmin(topicId);
      setQuestions(response.data || []);
    } catch (error) {
      setMessage({ type: 'danger', text: error.response?.data?.message || 'Failed to load questions' });
      setTimeout(() => setMessage({ type: '', text: '' }), 4000);
    }
  };
 
  const execute = async (callback, success) => {
    try {
      await callback();
      if (selectedTopicId) await loadQuestions(selectedTopicId);
      setMessage({ type: 'success', text: success });
      setTimeout(() => setMessage({ type: '', text: '' }), 4000);
    } catch (error) {
      setMessage({ type: 'danger', text: error.response?.data?.message || 'Action failed' });
      setTimeout(() => setMessage({ type: '', text: '' }), 4000);
    }
  };
 
  return (
    <AppShell >
      <div className="section-header mb-4">
        <h1 className="page-title">Admin Questions</h1>
        <p className="text-muted mb-0">Attach subjective questions, keywords, and rubric scores to each topic.</p>
      </div>
      <AlertMessage variant={message.type || 'danger'} message={message.text} />
      {loading ? <Loader text="Loading questions module..." /> : (
        <QuestionManager
          domains={domains}
          topics={topicsMap}
          questions={questions}
          selectedDomainId={selectedDomainId}
          selectedTopicId={selectedTopicId}
          onSelectDomain={loadTopics}
          onSelectTopic={loadQuestions}
          onCreateQuestion={(payload) => execute(() => createQuestion(payload), 'Question created successfully')}
          onDeleteQuestion={(id) => execute(() => deleteQuestion(id), 'Question deleted successfully')}
        />
      )}
    </AppShell>
  );
}
 
export default AdminQuestionsPage;