import { Get, Post, Delete, Put, PostForm } from "../server";

export function getAllDeliver(hub_id){
    return Get('/getAllDeliver',{hubId:hub_id})
}

export function getDeliver(deliverId = null,name = null){
    return Get('/getDeliver',{  deliverId:deliverId,name:name  })
}

export function updateDeliver(deliver){
    return Put('/updateDeliver',deliver)
}

export function addDeliver(deliver){
    return Post('/addDeliver',deliver)
}

export function deleteDeliver(mapId){
    return Delete('/deleteDeliver',{ mapId:mapId })
}

export const deliverApi = {
    getAllDeliver,
    getDeliver,
    updateDeliver,
    addDeliver,
    deleteDeliver
}