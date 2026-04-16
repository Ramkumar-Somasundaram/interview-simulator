import React, { useState } from 'react';
 
const initial = { name: '', difficulty: 'EASY' };
 
function TopicManager({ domain, topics, onCreate, onUpdate, onDelete }) {
  const [form, setForm] = useState(initial);
  const [editingId, setEditingId] = useState(null);
 
  const submit = (e) => {
    e.preventDefault();
    const payload = { ...form, domainId: domain.id };
    if (editingId) onUpdate(editingId, payload);
    else onCreate(payload);
    setForm(initial);
    setEditingId(null);
  };
 
  return (
    <div className="card border-0 shadow-sm mt-4">
      <div className="card-body p-4">
        <h3 className="h5 mb-3">Topics for {domain.name}</h3>
        <form className="row g-3 align-items-end" onSubmit={submit}>
          <div className="col-md-5">
            <label className="form-label">Topic Name</label>
            <input className="form-control" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
          </div>
          <div className="col-md-4">
            <label className="form-label">Difficulty</label>
            <select className="form-select" value={form.difficulty} onChange={(e) => setForm({ ...form, difficulty: e.target.value })}>
              <option value="EASY">EASY</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HARD">HARD</option>
            </select>
          </div>
          <div className="col-md-3 d-grid">
            <button className="btn btn-primary" type="submit">{editingId ? 'Update Topic' : 'Add Topic'}</button>
          </div>
        </form>
        <div className="row g-3 mt-3">
          {topics.map((topic) => (
            <div className="col-12 col-md-6" key={topic.id}>
              <div className="border rounded-4 p-3 h-100 bg-light-subtle">
                <div className="d-flex justify-content-between align-items-start gap-2">
                  <div>
                    <h4 className="h6 mb-1">{topic.name}</h4>
                    <p className="small text-muted mb-1">Difficulty: {topic.difficulty}</p>
                    <p className="small text-muted mb-0">Questions: {topic.questionCount}</p>
                  </div>
                  <span className="badge text-bg-dark">{topic.difficulty}</span>
                </div>
                <div className="d-flex gap-2 mt-3">
                  <button type="button" className="btn btn-sm btn-outline-secondary" onClick={() => { setEditingId(topic.id); setForm({ name: topic.name, difficulty: topic.difficulty }); }}>Edit</button>
                  <button type="button" className="btn btn-sm btn-outline-danger" onClick={() => onDelete(topic.id)}>Delete</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
 
export default TopicManager;