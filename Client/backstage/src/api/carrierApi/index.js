import { Get, Post, Delete, Put, PostForm,DeleteObj } from "../server";

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

export function addCarrier(carrier){
    return Post("/addCarrier",carrier)
}

export function updateCarrier(carrier){
    return Put("/updateCarrier",carrier)
}

export function deleteCarrier(carrierId){
    return Delete("/deleteCarrier",{ carrierId:carrierId })
}

export const carrierApi = {
    getCarrier,
    getCarrierParcel,
    addCarrier,
    updateCarrier,
    deleteCarrier
}