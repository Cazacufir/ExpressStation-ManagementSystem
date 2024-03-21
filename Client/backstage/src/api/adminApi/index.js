import { Get, Post, Delete, Put, PostForm } from "../server";

export function login(admin) {
    return Post("/login", admin);
}

export const adminApi = {
    login
}
