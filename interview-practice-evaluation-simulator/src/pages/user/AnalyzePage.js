import React, { useEffect, useState } from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  LineElement,
  PointElement,
  LinearScale,
  TimeScale,
  Tooltip,
  Legend
} from 'chart.js';
import 'chartjs-adapter-date-fns';

import TopNavbar from '../../components/layout/TopNavbar';
import { analyticsService } from '../../services/analyticsService';

ChartJS.register(
  LineElement,
  PointElement,
  LinearScale,
  TimeScale,
  Tooltip,
  Legend
);

function AnalyzePage() {
  const [trendData, setTrendData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    analyticsService.getScoreTrend()
      .then(data => {
        setTrendData(data || []);
        setLoading(false);
      })
      .catch(() => {
        setError('Failed to load score trend.');
        setLoading(false);
      });
  }, []);

  const colors = [
    '#0d9488', // teal
    '#3b82f6', // blue
    '#6366f1', // indigo
    '#f59e0b', // amber
    '#ef4444'  // red
  ];

  const chartData = {
    datasets: trendData.map((domain, index) => ({
      label: domain.domainName,
      data: domain.points.map(point => ({
        x: new Date(point.date),       // ✅ REAL DATE OBJECT
        y: point.percentageScore
      })),
      borderColor: colors[index % colors.length],
      backgroundColor: colors[index % colors.length] + '33',
      tension: 0.35,
      fill: true,
      pointRadius: 4,
      pointHoverRadius: 6
    }))
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top'
      },
      tooltip: {
        callbacks: {
          label: ctx =>
            `${ctx.dataset.label}: ${ctx.parsed.y.toFixed(1)}%`
        }
      }
    },
    scales: {
  x: {
    type: 'time',
    time: {
      unit: 'day',
      tooltipFormat: 'dd MMM yyyy'
    },
    ticks: {
      autoSkip: false,
      maxRotation: 0,
      minRotation: 0,
      padding: 10
    },
    grid: {
      display: false
    }
  },
  y: {
    min: 0,
    max: 100,
    title: {
      display: true,
      text: 'Score (%)'
    },
    ticks: {
      callback: value => `${value}%`
    }
  }
}

  };

  return (
    <>
      <TopNavbar />

      <div className="container py-4">
        <h4 className="fw-bold mb-1">Score Trend</h4>
        <p className="text-muted mb-4">
          Your performance progress across all domains
        </p>

        {loading && (
          <div className="text-center py-5">
            <div className="spinner-border text-primary" />
          </div>
        )}

        {error && (
          <div className="alert alert-danger">{error}</div>
        )}

        {!loading && !error && trendData.length === 0 && (
          <div className="text-center py-5 text-muted">
            <p>No data yet. Complete a mock test to see your score trend.</p>
          </div>
        )}

        {!loading && trendData.length > 0 && (
          <div className="card shadow-sm p-4" style={{ height: 420 }}>
            <Line data={chartData} options={chartOptions} />
          </div>
        )}
      </div>
    </>
  );
}

export default AnalyzePage;