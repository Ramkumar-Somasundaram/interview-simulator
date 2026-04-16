import React, { useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import AppShell from '../../components/layout/AppShell';
import Loader from '../../components/common/Loader';
import AlertMessage from '../../components/common/AlertMessage';
import { getEvaluationBySession } from '../../services/mockService';
import { formatDateTime } from '../../utils/helpers';

const items = [
  { label: 'Mock', to: '/mock' },
  { label: 'Analyze', to: '/analyze' },
  { label: 'History', to: '/history' }
];

function PerformancePage() {
  const { sessionId } = useParams();
  const location = useLocation();

  const [evaluation, setEvaluation] = useState(
    location.state?.evaluation || null
  );
  const [loading, setLoading] = useState(
    !location.state?.evaluation
  );
  const [error, setError] = useState('');

  useEffect(() => {
    if (evaluation) return;

    const loadEvaluation = async () => {
      try {
        const response = await getEvaluationBySession(sessionId);
        setEvaluation(response.data);
      } catch (err) {
        setError(
          err.response?.data?.message ||
          'Failed to load performance details'
        );
      } finally {
        setLoading(false);
      }
    };

    loadEvaluation();
  }, [evaluation, sessionId]);

  return (
    <AppShell>
      <AlertMessage message={error} />

      {loading ? (
        <Loader text="Loading performance..." />
      ) : (
        evaluation && (
          <div className="d-grid gap-4">

            {/* ========= SESSION SUMMARY ========= */}
            <div className="dashboard-hero-card shadow-sm">
              <span className="badge text-bg-light border mb-3">
                Performance Summary
              </span>

              <h1 className="page-title mb-2">
                {evaluation.domainName} • {evaluation.topicName}
              </h1>

              <p className="text-muted mb-3">
                Difficulty: {evaluation.difficulty} •
                Attended on {formatDateTime(evaluation.attendedAt)}
              </p>

              <div className="row g-3">
                <div className="col-6 col-md-3">
                  <div className="score-chip">
                    <span>Total Score</span>
                    <strong>{evaluation.totalScore}</strong>
                  </div>
                </div>
                <div className="col-6 col-md-3">
                  <div className="score-chip">
                    <span>Max Score</span>
                    <strong>{evaluation.maxPossibleScore}</strong>
                  </div>
                </div>
                <div className="col-6 col-md-3">
                  <div className="score-chip">
                    <span>Percentage</span>
                    <strong>{evaluation.percentageScore}%</strong>
                  </div>
                </div>
                <div className="col-6 col-md-3">
                  <div className="score-chip">
                    <span>Session ID</span>
                    <strong>{evaluation.sessionId}</strong>
                  </div>
                </div>
              </div>
            </div>

            {/* ========= QUESTION‑WISE FEEDBACK ========= */}
            {evaluation.answerEvaluations?.map((item, index) => (
              <div
                className="card border-0 shadow-sm"
                key={`${item.questionId}-${index}`}
              >
                <div className="card-body p-4">

                  <div className="d-flex justify-content-between align-items-start gap-3 mb-3">
                    <div>
                      <p className="text-muted small mb-1">
                        Question {index + 1}
                      </p>
                      <h2 className="h5 mb-0">
                        {item.questionText}
                      </h2>
                    </div>

                    <span className="badge text-bg-primary">
                      Score: {item.score}/{item.maxScore}
                    </span>
                  </div>

                  {/* Submitted Answer */}
                  <div className="mb-3">
                    <h3 className="h6">Submitted Answer</h3>
                    <p className="mb-0 text-muted">
                      {item.userAnswer || 'No answer submitted.'}
                    </p>
                  </div>

                  {/* Performance Feedback */}
                  <div className="mt-3">
                    <h4 className="h6 text-primary">
                      Performance Feedback
                    </h4>

                    <div className="feedback-box">
                      <p className="mb-0">
                        {item.suggestion || 'Feedback not available for this question.'}
                      </p>
                    </div>
                  </div>

                </div>
              </div>
            ))}

          </div>
        )
      )}
    </AppShell>
  );
}

export default PerformancePage;