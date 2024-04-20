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

export const ParcelApi = {
    getSendListByHub,
    sendParcelByHub,
    getSendingParcel,
    receiveParcelByHub,
    getAllParcelByHub,
    addSendList
}