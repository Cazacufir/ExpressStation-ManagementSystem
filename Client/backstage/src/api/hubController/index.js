import { Get, Post, Delete, Put, PostForm } from "../server";

export function getMainInfo(Id){
    return Get("/mainInfo",Id)
}

export const hubApi = {
    getMainInfo
}