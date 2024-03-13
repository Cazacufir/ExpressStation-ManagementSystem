import { Get, Post, Delete, Put, PostForm } from "../server";

export function getCompanyList (hub_id){
    return Get('/companyList',{ hub_id: hub_id})
}

export function getCompanyName (){
    return Get('/companyName')
}

export function addCompany (com_id,hub_id){
    return Post('/addCompany',{ com_id: com_id,hub_id: hub_id})
}

export const companyApi = {
    getCompanyList,
    getCompanyName,
    addCompany,
}