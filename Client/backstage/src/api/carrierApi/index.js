import { Get, Post, Delete, Put, PostForm } from "../server";

export function getCarrier(hub_id){
    return Get("/getCarrier",{ hub_id:hub_id })
}

export const carrierApi = {
    getCarrier
}