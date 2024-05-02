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

export function getKindsParcel(hub_id){
    return Get("/getKindsParcel",{ hub_id:hub_id })
}

export function countUser(hub_id){
    return Get("/countUser",{ hub_id:hub_id })
}

export function countSRParcel(hub_id){
    return Get("/countSRParcel",{ hub_id:hub_id })
}

export function searchSendListByHub(hub_id,parcelId,word){
    return Get("/searchSendListByHub",{
        hub_id:hub_id,
        parcelId:parcelId,
        word:word,
    })
}

export function searchSendingParcel(hub_id,parcelId,word){
    return Get("/searchSendingParcel",{
        hub_id:hub_id,
        parcelId:parcelId,
        word:word,
    })
}

export function searchAllParcelByHub(hub_id,parcelId,word){
    return Get("/searchAllParcelByHub",{
        hub_id:hub_id,
        parcelId:parcelId,
        word:word,
    })
}

export function deleteParcelRecord(parcel){
    return Post("/deleteParcelRecord",parcel)
}

export function selectAllinHub(start,end,hub_id){
    return Get("/selectAllinHub",{
        start:start,
        end:end,
        hub_id:hub_id
    })
}

export function selectAllGot(start,end,hub_id){
    return Get("/selectAllGot",{
        start:start,
        end:end,
        hub_id:hub_id
    })
}

export function getOutedParcel(pageNum,pageSize,hub_id){
    return Get("/getOutedParcel",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function searchOutedParcel(hub_id,parcelId,word){
    return Get("/searchOutedParcel",{
        hub_id:hub_id,
        parcelId:parcelId,
        word:word,
    })
}

export function outParcelList(parcel){
    return Post("/outParcelList",parcel)
}

export function inParcelList(parcel){
    return Post("/inParcelList",parcel)
}

export function getGotParcelByHub(pageNum,pageSize,hub_id){
    return Get("/getGotParcelByHub",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function searchGotParcelByHub(hub_id,parcelId,word){
    return Get("/searchGotParcelByHub",{
        hub_id:hub_id,
        parcelId:parcelId,
        word:word,
    })
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
    countParcel,
    getKindsParcel,
    countUser,
    countSRParcel,
    searchSendListByHub,
    searchSendingParcel,
    searchAllParcelByHub,
    deleteParcelRecord,
    selectAllinHub,
    searchOutedParcel,
    getOutedParcel,
    outParcelList,
    inParcelList,
    selectAllGot,
    searchGotParcelByHub,
    getGotParcelByHub
}