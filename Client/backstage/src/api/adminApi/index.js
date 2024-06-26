import { Get, Post, Delete, Put, PostForm } from "../server";

export function login(admin) {
    return Post("/login", admin);
}

export function register(admin) {
    return Post("/register", admin);
}

export function vertifyInfo(hub){
    return Post("/vertifyInfo",hub)
}

export function updatePassword(admin){
    return Post("/updatePassword",admin);
}

export const adminApi = {
    login,
    register,
    vertifyInfo,
    updatePassword
}
