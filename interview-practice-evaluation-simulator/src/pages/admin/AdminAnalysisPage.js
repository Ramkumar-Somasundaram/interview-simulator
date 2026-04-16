import React, { useEffect, useMemo, useState } from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend
} from 'chart.js';

import AppShell from '../../components/layout/AppShell';
import AlertMessage from '../../components/common/AlertMessage';
import Loader from '../../components/common/Loader';
import { getAdminAnalysis } from '../../services/adminService';

// Register chart elements
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend
);

const items = [
  { label: 'Domains', to: '/admin/domain' },
  { label: 'Questions', to: '/admin/question' },
  { label: 'Analysis', to: '/admin/analyze' },
  { label: 'User Dashboard', to: '/admin/user-view' }
];

function AdminAnalysisPage() {
  const [dataRows, setDataRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const load = async () => {
      try {
        const response = await getAdminAnalysis();
        setDataRows(response.data || []);
      } catch (err) {
        setError(
          err.response?.data?.message || 'Failed to load admin analysis'
        );
        setTimeout(() => setError(''), 4000);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  // Line chart data
  const chartData = useMemo(() => ({
    labels: dataRows.map(
      item => item.domainName || item.name || 'Domain'
    ),
    datasets: [
      {
        label: 'Attempts',
        data: dataRows.map(
          item => item.attemptCount || item.count || item.totalAttempts || 0
        ),
        borderColor: '#1d4ed8',
        backgroundColor: 'rgba(29, 78, 216, 0.12)',
        tension: 0.4,              // Smooth curve
        fill: true,                // Area under line
        pointRadius: 5,
        pointHoverRadius: 7,
        pointBackgroundColor: '#1d4ed8',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2
      }
    ]
  }), [dataRows]);

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        mode: 'index',
        intersect: false
      }
    },
    scales: {
      x: {
        grid: { display: false },
        ticks: {
          color: '#64748b'
        }
      },
      y: {
        beginAtZero: true,
        ticks: {
          stepSize: 1,
          color: '#64748b'
        },
        grid: {
          color: 'rgba(226, 232, 240, 0.6)'
        }
      }
    }
  };

  return (
    <AppShell>
      <div className="section-header mb-4">
        <div>
          <h1 className="page-title">Admin Analysis</h1>
          <p className="text-muted mb-0">
            Track the most attempted domains and overall usage trends.
          </p>
        </div>
      </div>

      <AlertMessage message={error} />

      {loading ? (
        <Loader text="Loading admin analytics..." />
      ) : (
        <div className="card border-0 shadow-sm">
          <div className="card-body p-4">
            <h2 className="h5 mb-4">Domain Attempt Trends</h2>

            {/* Chart container for better height */}
            <div style={{ height: '380px' }}>
              <Line data={chartData} options={chartOptions} />
            </div>
          </div>
        </div>
      )}
    </AppShell>
  );
}

export default AdminAnalysisPage;