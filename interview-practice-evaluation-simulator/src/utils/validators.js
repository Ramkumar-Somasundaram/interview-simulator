export const isValidGmail = (email) => /.+@gmail\.com$/i.test(email || '');
export const isValidPassword = (password) => (password || '').length >= 8;
 
export const validateRegister = ({ username, email, password }) => {
  const errors = {};
  if (!username?.trim()) errors.username = 'Username must not be blank';
  if (!email?.trim()) errors.email = 'Email must not be blank';
  else if (!isValidGmail(email)) errors.email = 'Email is invalid. Must be a valid @gmail.com address';
  if (!password?.trim()) errors.password = 'Password must not be blank';
  else if (!isValidPassword(password)) errors.password = 'Password must have at least 8 characters';
  return errors;
};
 
export const validateLogin = ({ email, password }) => {
  const errors = {};
  if (!email?.trim()) errors.email = 'Email must not be blank';
  else if (!isValidGmail(email)) errors.email = 'Email is invalid. Must be a valid @gmail.com address';
  if (!password?.trim()) errors.password = 'Password must not be blank';
  else if (!isValidPassword(password)) errors.password = 'Password must have at least 8 characters';
  return errors;
};
 