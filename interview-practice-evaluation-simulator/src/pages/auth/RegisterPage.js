import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthFormCard from '../../components/auth/AuthFormCard';
import AlertMessage from '../../components/common/AlertMessage';
import { registerUser } from '../../services/authService';
import { validateRegister } from '../../utils/validators';
 
function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [apiState, setApiState] = useState({ type: '', message: '' });
  const [loading, setLoading] = useState(false);
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    const validation = validateRegister(form);
    setErrors(validation);
    setApiState({ type: '', message: '' });
    if (Object.keys(validation).length) return;
 
    try {
      setLoading(true);
      const response = await registerUser(form);
      if (!response.success) {
        setApiState({ type: 'danger', message: response.message || 'Registration failed' });
        return;
      }
      setApiState({ type: 'success', message: response.message || 'Registered successfully' });
      setTimeout(() => navigate('/login'), 1000);
    } catch (error) {
      const backendData = error.response?.data;
      const message = backendData?.message || 'Registration failed';
      setApiState({ type: 'danger', message });
      if (backendData?.data) setErrors(backendData.data);
    } finally {
      setLoading(false);
    }
  };
 
  return (
    <AuthFormCard title="Create your account" subtitle="Register once and start structured mock interview practice.">
      <AlertMessage variant={apiState.type || 'danger'} message={apiState.message} />
      <form onSubmit={handleSubmit} noValidate>
        <div className="mb-3">
          <label className="form-label">Username</label>
          <input className={`form-control ${errors.username ? 'is-invalid' : ''}`} value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
          {errors.username && <div className="invalid-feedback">{errors.username}</div>}
        </div>
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input className={`form-control ${errors.email ? 'is-invalid' : ''}`} value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          {errors.email && <div className="invalid-feedback">{errors.email}</div>}
        </div>
        <div className="mb-4">
          <label className="form-label">Password</label>
          <input type="password" className={`form-control ${errors.password ? 'is-invalid' : ''}`} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          {errors.password && <div className="invalid-feedback">{errors.password}</div>}
        </div>
        <button className="btn btn-primary w-100 py-2" type="submit" disabled={loading}>{loading ? 'Creating account...' : 'Register'}</button>
      </form>
      <p className="text-center text-muted mt-4 mb-0">Already registered? <Link to="/login" className="fw-semibold">Login</Link></p>
    </AuthFormCard>
  );
}
 
export default RegisterPage;
 