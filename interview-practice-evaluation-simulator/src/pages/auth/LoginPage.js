import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthFormCard from '../../components/auth/AuthFormCard';
import AlertMessage from '../../components/common/AlertMessage';
import { loginUser } from '../../services/authService';
import { useAuth } from '../../context/AuthContext';
import { validateLogin } from '../../utils/validators';
 
function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState('');
  const [loading, setLoading] = useState(false);
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    const validation = validateLogin(form);
    setErrors(validation);
    setApiError('');
    if (Object.keys(validation).length) return;
 
    try {
      setLoading(true);
      const response = await loginUser(form);
      if (!response.success) {
        setApiError(response.message || 'Invalid email or password');
        return;
      }
      login(response.data);
      navigate(response.data.role === 'ADMIN' ? '/admin/dashboard' : '/dashboard');
    } catch (error) {
      setApiError(error.response?.data?.message || 'Invalid email or password');
    } finally {
      setLoading(false);
    }
  };
 
  return (
    <AuthFormCard title="Welcome back" subtitle="Practice smarter. Track progress. Improve with structured evaluation.">
      <AlertMessage message={apiError} />
      <form onSubmit={handleSubmit} noValidate>
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
        <button className="btn btn-primary w-100 py-2" type="submit" disabled={loading}>{loading ? 'Signing in...' : 'Login'}</button>
      </form>
      <p className="text-center text-muted mt-4 mb-0">New here? <Link to="/register" className="fw-semibold">Create an account</Link></p>
    </AuthFormCard>
  );
}
 
export default LoginPage;