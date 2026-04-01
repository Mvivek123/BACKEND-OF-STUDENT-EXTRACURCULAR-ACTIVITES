const API_BASE = process.env.REACT_APP_API_BASE || 'http://localhost:54434';

const login = async (username, password) => {
  const res = await fetch(`${API_BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });
  if (!res.ok) {
    let text;
    try { text = await res.text(); } catch(e) { text = res.statusText; }
    throw new Error(text || `HTTP ${res.status}`);
  }
  const json = await res.json();
  // store token in localStorage (simple approach)
  if (json.token) {
    localStorage.setItem('auth_token', json.token);
    localStorage.setItem('auth_user', JSON.stringify(json.user || {}));
  }
  return json;
};

const logout = () => {
  localStorage.removeItem('auth_token');
  localStorage.removeItem('auth_user');
};

const getToken = () => localStorage.getItem('auth_token');
const getUser = () => {
  const u = localStorage.getItem('auth_user');
  return u ? JSON.parse(u) : null;
};

export default { login, logout, getToken, getUser };
