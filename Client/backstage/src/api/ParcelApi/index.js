import { Get, Post, Delete, Put, PostForm } from "../server";

export function getSendListByHub(pageNum,pageSize,hub_id){
    return Get('/getSendListByHub',{
        pageNum,
        pageSize,
        hub_id
    })
}

export function sendParcelByHub(parcelId,hub_id){
    return Post('/sendParcelByHub',{
        parcelId:parcelId,
        hub_id:hub_id
    })
}

export function  getSendingParcel(pageNum,pageSize,hub_id){
    return Get("/getSendingParcel",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function receiveParcelByHub(parcel){
    return Post("/receiveParcelByHub",parcel)
}

export function getAllParcelByHub(pageNum,pageSize,hub_id){
    return Get("/getAllParcelByHub",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function addSendList(parcel){
    return Post("/addSendList",parcel)
}

export function getAllReserveParcel(pageNum,pageSize,hub_id){
    return Get("/getAllReserveParcel",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function receiveSingleParcel(parcelId){
    return Get("/receiveSingleParcel",{ parcelId:parcelId })
}

export function getDelayParcel(pageNum,pageSize,hub_id){
    return Get("/getDelayParcel",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function returnParcel(parcelId){
    return Get("/returnParcel",{ parcelId:parcelId })
}

export function countParcel(hub_id){
    return Get("/countParcel",{ hub_id:hub_id })
}

export const ParcelApi = {
    getSendListByHub,
    sendParcelByHub,
    getSendingParcel,
    receiveParcelByHub,
    getAllParcelByHub,
    addSendList,
    getAllReserveParcel,
    receiveSingleParcel,
    getDelayParcel,
    returnParcel,
    countParcel
}