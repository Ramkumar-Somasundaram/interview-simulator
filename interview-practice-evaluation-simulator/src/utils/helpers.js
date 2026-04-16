export const formatDateTime = (value) => {
  if (!value) return '-';
  return new Date(value).toLocaleString();
};
 
export const formatDate = (value) => {
  if (!value) return '-';
  return new Date(value).toLocaleDateString();
};
 
export const slugify = (text) =>
  (text || '')
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)+/g, '');
 
export const domainGradients = [
  'linear-gradient(135deg, #0f766e, #14b8a6)',
  'linear-gradient(135deg, #1d4ed8, #60a5fa)',
  'linear-gradient(135deg, #7c3aed, #c084fc)',
  'linear-gradient(135deg, #ea580c, #fb923c)',
  'linear-gradient(135deg, #059669, #34d399)',
  'linear-gradient(135deg, #be185d, #f472b6)'
];
 