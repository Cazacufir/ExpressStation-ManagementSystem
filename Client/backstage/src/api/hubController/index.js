import { Get, Post, Delete, Put, PostForm } from "../server";

export function getMainInfo(Id){
    const params = {
        Id: Id,
    }
    return Get(`/mainInfo/${params.Id}`)
}

export const hubApi = {
    getMainInfo
}