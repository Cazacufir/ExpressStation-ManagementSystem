
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

export const setLocal = (key,value) => {
  return localStorage.setItem(key,value)
}

export const getLocal = (key) => {
  return localStorage.getItem(key)
}

export const removeLocal = (key) => {
  return localStorage.removeItem(key);
};

export const utils = {
  setSession,
  getSession,
  removeSession,
  clearSession,
  setLocal,
  getLocal,
  removeLocal
};
