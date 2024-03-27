import axios from "axios";
import { getSession, clearSession } from "../utils/session";

const inst = axios.create({
  baseURL: "/api",
  timeout: 10000,
});

inst.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    config.headers.type = "admin"
    const token = getSession("token");
    if (token) {
      config.headers.token = token;
    }
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

inst.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    const reg = /\d\d/;
    if (response.data.code.toString().match(reg)[0] === "40") {
      clearSession();
    }
    return response;
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  }
);

export const Get = (url, params, clearFn = undefined) =>
  new Promise((resolve) => {
    inst
      .get(url, { params })
      .then((result) => {
        let res;
        if (clearFn !== undefined) {
          res = clearFn(result.data);
        } else {
          res = result.data;
        }
        resolve([null, res]);
      })
      .catch((err) => {
        resolve([err, undefined]);
      });
  });

export const Post = (url, data, params) => {
  return new Promise((resolve) => {
    inst
      .post(url, data, { params })
      .then((result) => {
        resolve([null, result.data]);
      })
      .catch((err) => {
        resolve([err, undefined]);
      });
  });
};

export const PostForm = (url, data, params) => {
  return new Promise((resolve) => {
    inst
      .postForm(url, data, { params })
      .then((result) => {
        resolve([null, result.data]);
      })
      .catch((err) => {
        resolve([err, undefined]);
      });
  });
};

// export const Delete = (url, data, params) => {
//   return new Promise((resolve) => {
//     inst
//       .delete(url, {
//         data: data,
//         params: params,
//       })
//       .then((result) => {
//         resolve([null, result.data]);
//       })
//       .catch((err) => {
//         resolve([err, undefined]);
//       });
//   });
// };
export const Delete = (url, params, clearFn = undefined) =>
  new Promise((resolve) => {
    inst
      .delete(url, { params })
      .then((result) => {
        let res;
        if (clearFn !== undefined) {
          res = clearFn(result.data);
        } else {
          res = result.data;
        }
        resolve([null, res]);
      })
      .catch((err) => {
        resolve([err, undefined]);
      });
  });

export const Put = (url, data, params) => {
  return new Promise((resolve) => {
    inst
      .put(url, data, { params })
      .then((result) => {
        resolve([null, result.data]);
      })
      .catch((err) => {
        resolve([err, undefined]);
      });
  });
};
