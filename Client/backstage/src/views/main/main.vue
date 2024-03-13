<template>
    <div class="container flex flex-col justify-center items-center">
        <div class="header h-50 w-full flex flex-row-reverse">
            <el-text tag="b" size="large">驿站主体信息</el-text>
        </div>

        <div class="MainInfo w-40% h-50vh mr-100 flex flex-col justify-center items-center">
            <el-form ref="ruleFormRef" :model="Info" lable-width="120px" :rules="hub_rules">

                <el-form-item prop="id" label="驿站ID：" v-show="!isModify">
                    <el-text>{{ Info.id }}</el-text>
                </el-form-item>

                <el-form-item prop="name" label="驿站名称：">
                    <el-input v-model="Info.name" v-show="isModify"></el-input>
                    <el-text v-show="!isModify">{{ Info.name }}</el-text>
                </el-form-item>


                <el-form-item prop="address" label="驿站地址：">
                    <el-input v-show="isModify" v-model="Info.address"></el-input>
                    <el-text v-show="!isModify">{{ Info.address }}</el-text>
                </el-form-item>

                <el-form-item prop="open_time" label="营业时间：" class="time">
                    <el-input v-show="isModify" v-model="Info.open_time"></el-input><el-text v-show="!isModify">{{
                Info.open_time }}</el-text> ~
                    <el-input v-show="isModify" v-model="Info.close_time"></el-input><el-text v-show="!isModify">{{
                Info.close_time }}</el-text>
                </el-form-item>

                <el-form-item prop="contact" label="联系方式：">
                    <el-input v-show="isModify" v-model="Info.contact"></el-input>
                    <el-text v-show="!isModify">{{ Info.contact }}</el-text>
                </el-form-item>

                <el-form-item prop="setupDate" label="成立时间：" v-show="!isModify">
                    <el-text>{{ formatDate(Info.setupDate) }}</el-text>
                </el-form-item>
            </el-form>

            <div class="btnBar">
                <el-button type="primary" v-show="!isModify" size="large" @click="isModify = true">修改信息</el-button>
                <el-button type="danger" v-show="isModify" @click="isModify = false">取消</el-button>
                <el-button type="primary" v-show="isModify" @click="submitForm">确定</el-button>
            </div>

        </div>
    </div>
</template>

<script setup>
import { api } from "@/api"
import { onMounted, reactive, ref } from "vue";
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'

const store = adminStore();

const Info = reactive({})

const ruleFormRef = ref()

let isModify = ref(false)

onMounted(() => {
    init()
})

const init = async () => {
    const hub = store.getAdminInfo()
    console.log(hub)
    const [e, r] = await api.getMainInfo(hub.hub_id)
    console.log(r.data)
    Object.assign(Info, r.data)
}

const formatDate = (dateString) => {
    let dateObject = new Date(dateString);
    let year = dateObject.getFullYear();
    let month = dateObject.getMonth() + 1;
    let day = dateObject.getDate();
    let formattedDate = `${year}年${month}月${day}日`;
    return formattedDate
}

const checkAddress = async (role, value, callback) => {
    if (!value) {
        callback(new Error('地址不能为空!'))
    }
    if (isModify.value == true) {
        const [e, r] = await api.vertifyHub(Info)
        if (r.code != 200) {
            callback(new Error(r.msg))
        }
    }
    callback()
}

const checkName = async (role, value, callback) => {
    if (!value) {
        callback(new Error('名称不能为空!'))
    }
    if (isModify.value == true) {
        const [e, r] = await api.vertifyHub(Info)
        if (r.code != 200) {
            callback(new Error(r.msg))
        }
    }
    callback()
}

const hub_rules = reactive({
    name: [
        { validator: checkAddress,trigger: 'blur' }
    ],
    address: [
        { validator: checkName,trigger: 'blur' }
    ],
    contact: [
        { required: true, message: '联系方式不能为空！', trigger: 'blur' }
    ]
})

const submitForm = async () => {
    const [e, r] = await api.updateHubInfo(Info)
    if (r.code == 200) {
        ElMessage({
            message: '修改成功！',
            type: 'success',
        })
        isModify.value = false
    }
    else {
        ElMessage.error('修改失败，请检查网络连接')
    }
}
</script>

<style scope lang="scss">
.time .el-input {
    --el-input-width: 100px;
}
</style>