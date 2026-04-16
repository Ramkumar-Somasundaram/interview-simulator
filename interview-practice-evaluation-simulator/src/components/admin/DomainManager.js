import React, { useState } from 'react';
 
const initialForm = { name: '', description: '' };
 
function DomainManager({ domains, onCreate, onUpdate, onDelete, onManageTopics }) {
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
 
  const submit = (e) => {
    e.preventDefault();
    if (!form.name.trim()) return;
    if (editingId) onUpdate(editingId, form);
    else onCreate(form);
    setForm(initialForm);
    setEditingId(null);
  };
 
  const startEdit = (domain) => {
    setEditingId(domain.id);
    setForm({ name: domain.name, description: domain.description || '' });
  };
 
  return (
    <div className="row g-4">
      <div className="col-12 col-lg-4">
        <div className="card border-0 shadow-sm h-100">
          <div className="card-body p-4">
            <h3 className="h5 mb-3">{editingId ? 'Edit Domain' : 'Create Domain'}</h3>
            <form onSubmit={submit} className="d-grid gap-3">
              <input className="form-control" placeholder="Domain name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
              <textarea className="form-control" placeholder="Description" rows="4" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
              <div className="d-flex gap-2">
                <button className="btn btn-primary" type="submit">{editingId ? 'Update' : 'Create'}</button>
                {editingId && <button type="button" className="btn btn-outline-secondary" onClick={() => { setEditingId(null); setForm(initialForm); }}>Cancel</button>}
              </div>
            </form>
          </div>
        </div>
      </div>
      <div className="col-12 col-lg-8">
        <div className="row g-3">
          {domains.map((domain) => (
            <div className="col-12 col-md-6" key={domain.id}>
              <div className="card border-0 shadow-sm h-100">
                <div className="card-body p-4 d-flex flex-column">
                  <div className="d-flex justify-content-between gap-3">
                    <div>
                      <h4 className="h5 mb-2">{domain.name}</h4>
                      <p className="text-muted mb-2">{domain.description || 'No description provided.'}</p>
                    </div>
                  <span
                  className={`badge status-badge ${
                   domain.hasQuestions ? 'text-bg-success' : 'text-bg-warning'
                   }`}
                  >
                  {domain.hasQuestions ? 'Live for users' : 'Draft'}
                  </span>
                  
                  </div>

                  <p className="small text-muted mt-auto mb-3">Topics: {domain.topics?.length || 0}</p>
                  <div className="d-flex flex-wrap gap-2">
                    <button type="button" className="btn btn-outline-primary btn-sm" onClick={() => onManageTopics(domain)}>Manage Topics</button>
                    <button type="button" className="btn btn-outline-secondary btn-sm" onClick={() => startEdit(domain)}>Edit</button>
                    <button type="button" className="btn btn-outline-danger btn-sm" onClick={() => onDelete(domain.id)}>Delete</button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
 
export default DomainManager;