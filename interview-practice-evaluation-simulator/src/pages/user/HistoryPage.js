import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AppShell from '../../components/layout/AppShell';
import Loader from '../../components/common/Loader';
import AlertMessage from '../../components/common/AlertMessage';
import HistoryTable from '../../components/history/HistoryTable';
import { getHistory } from '../../services/mockService';
 
const items = [
  { label: 'Mock', to: '/mock' },
  { label: 'Analyze', to: '/analyze' },
  { label: 'History', to: '/history' }
];
 
function HistoryPage() {
  const navigate = useNavigate();
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
 
  useEffect(() => {
    const load = async () => {
      try {
        const response = await getHistory();
        setHistory(response.data || []);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load history');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);
 
  return (
    <AppShell >
      <div className="section-header mb-4">
        <h1 className="page-title">History</h1>
        <p className="text-muted mb-0">Open any previous mock session to inspect the full performance breakdown.</p>
      </div>
      <AlertMessage message={error} />
      {loading ? <Loader text="Loading history..." /> : <HistoryTable items={history} onSelect={(item) => navigate(`/performance/${item.sessionId}`)} />}
    </AppShell>
  );
}
 
export default HistoryPage;