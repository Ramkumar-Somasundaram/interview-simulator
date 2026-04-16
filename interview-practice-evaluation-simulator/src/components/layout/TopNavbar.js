import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

function TopNavbar() {
  const { role } = useAuth();
  const base = role === 'ADMIN' ? '/admin' : '';

  return (
    <nav className="navbar navbar-expand-lg app-navbar sticky-top shadow-sm bg-secondary ">
      <div className="container-fluid px-3 px-lg-4">
        <NavLink className="text-light navbar-brand brand-mark" to={role === 'ADMIN' ? '/admin/dashboard' : '/dashboard'}>
          InterviewSimulator
        </NavLink>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#mainNavbar"
          aria-controls="mainNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="mainNavbar">
          <div className="navbar-nav ms-auto gap-lg-2 align-items-center">
            {/* <NavLink className="nav-link custom-nav-link" to={`${base}/domain`}>
              {role === 'ADMIN' ? 'Domains' : 'Mock'}
            </NavLink>
            <NavLink className="nav-link custom-nav-link" to={`${base}/analyze`}>
              Analyze
            </NavLink>
            <NavLink className="nav-link custom-nav-link" to={`${base}/question`}>
              {role === 'ADMIN' ? 'Questions' : 'History'}
            </NavLink> */}

            {/* ---------------------------------admin correct ----------------------------------------------- */}

            {role === 'ADMIN' && (
              <NavLink className="nav-link text-light custom-nav-link" to={`${base}/domain`}>
                Domain
              </NavLink>
            )}

            {role === 'ADMIN' && (
              <NavLink className="nav-link text-light custom-nav-link" to={`${base}/question`}>
                Questions
              </NavLink>
            )}

            {role === 'ADMIN' && (
              <NavLink className="nav-link text-light custom-nav-link" to={`${base}/analyze`}>
                Analysis
              </NavLink>
            )}



            {/* -------------------------------user correct--------------------------------------- */}

            {role !== 'ADMIN' && (
              <NavLink className="nav-link text-light custom-nav-link" to={`${base}/mock`}>
                Mock
              </NavLink>
            )}

            {role !== 'ADMIN' && (
              <NavLink className="nav-link text-light custom-nav-link" to={`${base}/analyze`}>
                Analyze
              </NavLink>
            )}

            {role !== 'ADMIN' && (
              <NavLink className="nav-link text-light custom-nav-link" to={`${base}/history`}>
                History
              </NavLink>
            )}



            {/* Show Dashboard link only for USER */}
            {role !== 'ADMIN' && (
              <NavLink className="text-light nav-link custom-nav-link" to="/dashboard">
                Dashboard
              </NavLink>
            )}

            {/* Show User Dashboard link only for ADMIN */}
            {role === 'ADMIN' && (
              <NavLink className="text-light nav-link custom-nav-link" to="/dashboard">
                Admin Dashboard
              </NavLink>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

export default TopNavbar;