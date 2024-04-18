import { Get, Post, Delete, Put, PostForm } from "../server";

export function getCarrier(pageNum,pageSize,hub_id){
    return Get("/getCarrier",{
        pageNum,
        pageSize,
        hub_id
    })
}

export function getCarrierParcel(pageNum,pageSize,carrierId){
    return Get("/getCarrierParcel",{
        pageNum,
        pageSize,
        carrierId
    })
}

export const carrierApi = {
    getCarrier,
    getCarrierParcel
}