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

export function receiveParcel(parcel){
    return Post("/receiveParcel",parcel)
}

export function getAllParcelByHub(pageNum,pageSize,hub_id){
    return Get("/getAllParcelByHub",{
        pageNum,
        pageSize,
        hub_id
    })
}

export const ParcelApi = {
    getSendListByHub,
    sendParcelByHub,
    getSendingParcel,
    receiveParcel,
    getAllParcelByHub
}