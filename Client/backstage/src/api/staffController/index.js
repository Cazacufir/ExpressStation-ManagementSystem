import { Get, Post, Delete, Put, PostForm } from "../server";

export function getStaff(staffId){
    return Get('/getStaff',{  staffId:staffId  })
}

export function updateStaffInfo(staff){
    return Put('/updateStaffInfo',staff)
}

export const staffApi = {
    getStaff,
    updateStaffInfo,
}