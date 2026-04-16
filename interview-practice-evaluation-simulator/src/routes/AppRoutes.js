import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import LoginPage from '../pages/auth/LoginPage';
import RegisterPage from '../pages/auth/RegisterPage';
import UserDashboardPage from '../pages/user/UserDashboardPage';
import MockPage from '../pages/user/MockPage';
import TopicPage from '../pages/user/TopicPage';
import QuestionPage from '../pages/user/QuestionPage';
import PerformancePage from '../pages/user/PerformancePage';
import AnalyzePage from '../pages/user/AnalyzePage';
import HistoryPage from '../pages/user/HistoryPage';
import AdminDashboardPage from '../pages/admin/AdminDashboardPage';
import AdminDomainsPage from '../pages/admin/AdminDomainsPage';
import AdminQuestionsPage from '../pages/admin/AdminQuestionsPage';
import AdminAnalysisPage from '../pages/admin/AdminAnalysisPage';
import NotFoundPage from '../pages/common/NotFoundPage';
import AdminUserViewPage from '../pages/admin/AdminUserViewPage';
 
function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
 
      <Route element={<ProtectedRoute allowedRoles={['USER']} />}>
        <Route path="/dashboard" element={<UserDashboardPage />} />
        <Route path="/mock" element={<MockPage />} />
        <Route path="/mock/:domainSlug" element={<TopicPage />} />
        <Route path="/mock/:domainSlug/:topicId" element={<QuestionPage />} />
        <Route path="/performance/:sessionId" element={<PerformancePage />} />
        <Route path="/analyze" element={<AnalyzePage />} />
        <Route path="/history" element={<HistoryPage />} />
      </Route>
 
      <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
        <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
        <Route path="/admin/domain" element={<AdminDomainsPage />} />
        <Route path="/admin/question" element={<AdminQuestionsPage />} />
        <Route path="/admin/analyze" element={<AdminAnalysisPage />} />
        <Route path="/admin/user-view" element={<AdminUserViewPage/>}/>
      </Route>
 
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
 
export default AppRoutes;