<template>
    <div class="container flex flex-col justify-center items-center">
        <div class="header h-50 w-full flex flex-row-reverse">
            <el-text tag="b" size="large">驿站主体信息</el-text>
        </div>

        <div class="MainInfo w-40% h-50vh mr-100 flex flex-col justify-center items-center">
            <el-form :model="test"  lable-width="120px">

                <el-form-item label="驿站ID：" v-show="!isModify">
                    <el-text>{{ test.id }}</el-text>
                </el-form-item>

                <el-form-item label="驿站名称：">
                    <el-input v-model="test.name" v-show="isModify"></el-input>
                    <el-text v-show="!isModify">{{ test.name }}</el-text>
                </el-form-item>


                <el-form-item label="驿站地址：">
                    <el-input v-show="isModify" v-model="test.address"></el-input>
                    <el-text v-show="!isModify">{{ test.address }}</el-text>
                </el-form-item>

                <el-form-item label="营业时间：" class="time">
                    <el-input v-show="isModify" v-model="test.open_time"></el-input><el-text v-show="!isModify">{{ test.open_time }}</el-text> ~
                    <el-input v-show="isModify" v-model="test.close_time"></el-input><el-text v-show="!isModify">{{ test.close_time }}</el-text>
                </el-form-item>

                <el-form-item label="联系方式：">
                    <el-input v-show="isModify" v-model="test.contact"></el-input>
                    <el-text v-show="!isModify">{{ test.contact }}</el-text>
                </el-form-item>

                <el-form-item label="成立时间：" v-show="!isModify">
                    <el-text>{{ test.setupTime }}</el-text>
                </el-form-item>
            </el-form>

            <div class="btnBar">
                <el-button v-show="!isModify" plain size="large" @click="isModify = true">修改信息</el-button>
                <el-button type="danger" v-show="isModify" @click="isModify = false">取消</el-button>
                <el-button type="primary" v-show="isModify">确定</el-button>
            </div>

        </div>
    </div>
</template>

<script setup>
import { api } from "@/api"
import { onMounted, reactive, ref } from "vue";
import { adminStore } from "@/stores/admin.js";

const store = adminStore();

const Info = reactive({})

let isModify = ref(false)

onMounted(() => {
    // init()
})

const test = reactive({
    name: '驿站',
    id:"40000",
    address: '翻斗大街翻斗花园',
    contact: '123',
    open_time: '9:00',
    close_time: '19:00',
    setupTime: '2024-02-23'
})

const init = async () => {
    const hub = store.getAdminInfo()
    console.log(hub)
    const [e, r] = await api.getMainInfo(hub.hub_id)
    console.log(111)
    Object.assign(Info, r.data)
}
</script>

<style scope lang="scss">
.time .el-input {
    --el-input-width: 100px;
}
</style>