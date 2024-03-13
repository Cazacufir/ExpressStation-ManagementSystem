import { Get, Post, Delete, Put, PostForm } from "../server";

export function getMainInfo(Id){
    return Get(`/mainInfo`,{ Id:Id });
}

export function vertifyHub(hub){
    return Post('/vertifyHub',hub)
}

export function updateHubInfo(hub){
    return Put('/updateHubInfo',hub)
}

export const hubApi = {
    getMainInfo,
    vertifyHub,
    updateHubInfo,
}