import React from 'react';
 
function AlertMessage({ variant = 'danger', message }) {
  if (!message) return null;
  return <div className={`alert alert-${variant} shadow-sm`}>{message}</div>;
}
 
export default AlertMessage;