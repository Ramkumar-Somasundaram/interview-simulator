import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import TopNavbar from './TopNavbar';
import Sidebar from './Sidebar';
 
function AppShell({ children, sidebarItems, footerLink }) {
  const { logout, user } = useAuth();
  const navigate = useNavigate();
 
  const handleLogout = () => {
    logout();
    navigate('/login');
  };
 
  return (
    <div className="app-layout">
      <TopNavbar />
      <div className="container-fluid px-3 px-lg-4 py-4">
        <div className="row g-4">
 
          {/* Sidebar — only render when sidebarItems is passed */}
          {sidebarItems && (
            <div className="col-12 col-xl-3">
              <Sidebar
                profileName={user?.username}
                items={sidebarItems}
                onLogout={handleLogout}
                footerLink={footerLink}
              />
            </div>
          )}
 
          {/* Main content — full width when no sidebar, partial when sidebar exists */}
          <div className={sidebarItems ? 'col-12 col-xl-9' : 'col-12'}>
            <main>{children}</main>
          </div>
 
        </div>
      </div>
    </div>
  );
}
 
export default AppShell;