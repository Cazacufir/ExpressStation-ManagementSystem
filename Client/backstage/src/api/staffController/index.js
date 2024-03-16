import { Get, Post, Delete, Put, PostForm } from "../server";

export function getStaffList(hub_id){
    return Get('/getStaffList',{hub_id:hub_id})
}

export function getStaff(staffId){
    return Get('/getStaff',{  staffId:staffId  })
}

export function updateStaffInfo(staff){
    return Put('/updateStaffInfo',staff)
}

export function addStaff(staff){
    return Post('/addStaff',staff)
}

export function deleteStaff(staffId){
    return Delete('/deleteStaff',{staffId:staffId})
}

export const staffApi = {
    getStaffList,
    getStaff,
    updateStaffInfo,
    addStaff,
    deleteStaff
}