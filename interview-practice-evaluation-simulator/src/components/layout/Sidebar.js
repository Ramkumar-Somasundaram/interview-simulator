import React from 'react';
import { NavLink } from 'react-router-dom';
 
function Sidebar({ profileName, items, onLogout, footerLink }) {
  return (
    <aside className="app-sidebar shadow-sm">
      <div>
        <div className="sidebar-profile-card">
          <div className="avatar-circle">{(profileName || 'U').charAt(0).toUpperCase()}</div>
          <div>
            <p className="sidebar-label mb-1">Profile</p>
            <h5 className="sidebar-name mb-0">{profileName}</h5>
          </div>
        </div>
        <nav className="nav flex-column gap-2 mt-4">
          {(items ||[]).map((item) => (
            <NavLink key={item.label} to={item.to} className="sidebar-link">
              {item.label}
            </NavLink>
          ))}
          {footerLink && (
            <NavLink to={footerLink.to} className="sidebar-link sidebar-secondary-link">
              {footerLink.label}
            </NavLink>
          )}
        </nav>
      </div>
      <button type="button" className="btn btn-outline-danger w-100 mt-4" onClick={onLogout}>
        Logout
      </button>
    </aside>
  );
}
 
export default Sidebar;
 