
export const setSession = (key, value) => {
  window.sessionStorage.setItem(key, value);
};

export const getSession = (key) => {
  return window.sessionStorage.getItem(key);
};

export const removeSession = (key) => {
  return window.sessionStorage.removeItem(key);
};

export const clearSession = () => {
  return window.sessionStorage.clear();
};

export const utils = {
  setSession,
  getSession,
  removeSession,
  clearSession
};
