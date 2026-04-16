import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AppShell from '../../components/layout/AppShell';
import Loader from '../../components/common/Loader';
import AlertMessage from '../../components/common/AlertMessage';
import { getQuestionsByTopicUser, submitAnswers } from '../../services/mockService';

function QuestionPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const domain = location.state?.domain;
  const topic = location.state?.topic;

  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadQuestions = async () => {
      if (!domain?.id || !topic?.id || !topic?.difficulty) {
        setError('Invalid navigation. Please start again from mock page.');
        setLoading(false);
        return;
      }

      try {
        const response = await getQuestionsByTopicUser(
          domain.id,
          topic.id,
          topic.difficulty
        );
        setQuestions(response.data || []);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load questions');
      } finally {
        setLoading(false);
      }
    };

    loadQuestions();
  }, [domain, topic]);

  // ✅ Check if question is unanswered
  const isUnanswered = (questionId) =>
    !answers[questionId] || answers[questionId].trim() === '';

  // ✅ Submit handler with validation
  const handleSubmit = async () => {
    const unansweredQuestions = questions.filter((q) =>
      isUnanswered(q.id)
    );

    if (unansweredQuestions.length > 0) {
      setError(
        `Please answer all questions before submitting. 
        ${unansweredQuestions.length} question(s) are still unanswered.`
      );
      return;
    }

    try {
      setSubmitting(true);
      setError('');

      const payload = {
        domainId: domain.id,
        topicId: topic.id,
        answers: questions.map((q) => ({
          questionId: q.id,
          answerText: answers[q.id]
        }))
      };

      const response = await submitAnswers(payload);

      navigate(`/performance/${response.data.sessionId}`, {
        state: { evaluation: response.data }
      });
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit answers');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AppShell>
      <div className="section-header mb-4">
        <h1 className="page-title">{topic?.name || 'Question Set'}</h1>
        <p className="text-muted mb-0">
          Answer all subjective questions and submit for structured evaluation.
        </p>
      </div>

      <AlertMessage message={error} />

      {loading ? (
        <Loader text="Loading questions..." />
      ) : (
        <div className="d-grid gap-4">
          {questions.map((question, index) => {
            const unanswered = isUnanswered(question.id);

            return (
              <div
                key={question.id}
                className={`card shadow-sm ${
                  unanswered ? 'border border-danger' : 'border-0'
                }`}
              >
                <div className="card-body p-4">
                  <p className="text-muted small mb-2">
                    Question {index + 1}
                    {unanswered && (
                      <small className="text-danger mt-2 d-block">
                      * Must answer this question
                      </small>
                    )}
                  </p>

                  <h2 className="h5 mb-3">
                    {question.questionText}
                  </h2>

                  <textarea
                    rows="5"
                    className="form-control"
                    placeholder="Type your answer here..."
                    value={answers[question.id] || ''}
                    onChange={(e) =>
                      setAnswers({
                        ...answers,
                        [question.id]: e.target.value
                      })
                    }
                  />

                </div>
              </div>
            );
          })}

          <div className="d-flex justify-content-end">
            <button
              className="btn btn-primary px-4"
              onClick={handleSubmit}
              disabled={submitting || !questions.length}
            >
              {submitting ? 'Submitting...' : 'Submit Answers'}
            </button>
          </div>
        </div>
      )}
    </AppShell>
  );
}

export default QuestionPage;