import React, { useMemo, useState } from 'react';
 
function QuestionManager({ domains, topics, questions, onSelectDomain, onSelectTopic, selectedDomainId, selectedTopicId, onCreateQuestion, onDeleteQuestion }) {
  const [questionText, setQuestionText] = useState('');
  const [keywords, setKeywords] = useState([{ word: '', rubricScore: 10 }]);
 
  const selectedDomainTopics = useMemo(() => topics[selectedDomainId] || [], [topics, selectedDomainId]);
 
  const updateKeyword = (index, key, value) => {
    const next = [...keywords];
    next[index][key] = key === 'rubricScore' ? Number(value) : value;
    setKeywords(next);
  };
 
  const addKeyword = () => setKeywords([...keywords, { word: '', rubricScore: 10 }]);
  const removeKeyword = (index) => setKeywords(keywords.filter((_, i) => i !== index));
 
  const submit = (e) => {
    e.preventDefault();
    const payload = {
      questionText,
      topicId: Number(selectedTopicId),
      keywords: keywords.filter((item) => item.word.trim())
    };
    onCreateQuestion(payload);
    setQuestionText('');
    setKeywords([{ word: '', rubricScore: 10 }]);
  };
 
  return (
    <div className="row g-4">
      <div className="col-12 col-lg-5">
        <div className="card border-0 shadow-sm h-100">
          <div className="card-body p-4">
            <h3 className="h5 mb-3">Create Question</h3>
            <form className="d-grid gap-3" onSubmit={submit}>
              <select className="form-select" value={selectedDomainId || ''} onChange={(e) => onSelectDomain(e.target.value)}>
                <option value="">Select Domain</option>
                {domains.map((domain) => <option key={domain.id} value={domain.id}>{domain.name}</option>)}
              </select>
              <select className="form-select" value={selectedTopicId || ''} onChange={(e) => onSelectTopic(e.target.value)} disabled={!selectedDomainId}>
                <option value="">Select Topic</option>
                {selectedDomainTopics.map((topic) => <option key={topic.id} value={topic.id}>{topic.name} - {topic.difficulty}</option>)}
              </select>
              <textarea className="form-control" rows="4" placeholder="Enter subjective question" value={questionText} onChange={(e) => setQuestionText(e.target.value)} />
              <div className="d-grid gap-2">
                {keywords.map((keyword, index) => (
                  <div key={index} className="row g-2 align-items-center">
                    <div className="col-7">
                      <input className="form-control" placeholder="Keyword" value={keyword.word} onChange={(e) => updateKeyword(index, 'word', e.target.value)} />
                    </div>
                    <div className="col-3">
                      <input className="form-control" type="number" min="1" placeholder="Score" value={keyword.rubricScore} onChange={(e) => updateKeyword(index, 'rubricScore', e.target.value)} />
                    </div>
                    <div className="col-2 d-grid">
                      <button type="button" className="btn btn-outline-danger" onClick={() => removeKeyword(index)} disabled={keywords.length === 1}>Remove</button>
                    </div>
                  </div>
                ))}
              </div>
              <button type="button" className="btn btn-outline-secondary" onClick={addKeyword}>Add Keyword</button>
              <button className="btn btn-primary" type="submit" disabled={!selectedTopicId}>Save Question</button>
            </form>
          </div>
        </div>
      </div>
      <div className="col-12 col-lg-7">
        <div className="card border-0 shadow-sm h-100">
          <div className="card-body p-4">
            <h3 className="h5 mb-3">Questions</h3>
            <div className="d-grid gap-3">
              {questions.map((question) => (
                <div key={question.id} className="border rounded-4 p-3 bg-light-subtle">
                  <div className="d-flex justify-content-between align-items-start gap-3">
                    <div>
                      <h4 className="h6 mb-1">{question.questionText}</h4>
                      <p className="small text-muted mb-2">{question.domainName} - {question.topicName} - {question.difficulty}</p>
                      <div className="d-flex flex-wrap gap-2">
                        {question.keywords?.map((k) => <span key={k.id || k.word} className="badge text-bg-light border text-dark">{k.word} ({k.rubricScore})</span>)}
                      </div>
                    </div>
                    <button type="button" className="btn btn-sm btn-outline-danger" onClick={() => onDeleteQuestion(question.id)}>Delete</button>
                  </div>
                </div>
              ))}
              {!questions.length && <p className="text-muted mb-0">Select a topic to view questions.</p>}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
 
export default QuestionManager;