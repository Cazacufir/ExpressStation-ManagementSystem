import { defineStore } from "pinia";

export const adminStore = defineStore("admin",{
    state:()=>{
        return {
            adminInfo: {}
        }
    },
    actions: {
        getAdminInfo(){
            return this.adminInfo
        },
        setAdminInfo(admin){
            Object.assign(this.adminInfo,admin);
        },
        deleteAdminInfo(){
            this.adminInfo = null
        }
    },
    persist: true,
})