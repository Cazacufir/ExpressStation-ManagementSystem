import { Get, Post, Delete, Put, PostForm } from "../server";

export function getSendListByHub(pageNum,pageSize,hub_id){
    return Get('/getSendListByHub',{
        pageNum,
        pageSize,
        hub_id
    })
}

export const ParcelApi = {
    getSendListByHub
}