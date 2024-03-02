<template>
    <el-form ref="hub_ruleFormRef" :model="hub" :rules="rules_hub" size="large" label-width="auto">
        <el-form-item prop="role" label="您的身份：">
            <el-radio-group v-model="hub.role">
                <el-radio :label="ZERO" size="large">我是管理员</el-radio>
                <el-radio :label="ONE" size="large">我是站长</el-radio>
            </el-radio-group>
        </el-form-item>

        <el-form-item v-show="hub.role == 0" prop="id" label="驿站ID">
            <el-input v-model="hub.id" placeholder="请输入待加入的驿站ID"></el-input>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="name" label="驿站名称">
            <el-input v-model="hub.name"></el-input>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="address" label="驿站地址">
            <el-input v-model="hub.address"></el-input>
        </el-form-item>

    </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue';

const hub = reactive({
    role:0,
    id:null,
    name:'',
    address:''
})

const hub_ruleFormRef = ref()

const ZERO = 0
const ONE = 1

const checkId = (role,value,callback) => {
    if(hub.role == 0){
        if(value == null){
            callback(new Error('驿站ID不能为空!'))
        }
    }
    else{
        callback()
    }
}

const checkName = (role,value,callback) => {
    if(hub.role == 1){
        if(value == ''){
            callback(new Error('驿站名称不能为空!'))
        }
    }
    else{
        callback()
    }
}

const checkAddress = (role,value,callback) => {
    if(hub.role == 1){
        if(value == ''){
            callback(new Error('驿站地址不能为空!'))
        }
    }
    else{
        callback()
    }
}

const rules_hub = reactive({
    id:[
        { validator: checkId, trigger: 'blur' }
    ],
    name:[
        { validator: checkName, trigger: 'blur' }
    ],
    address:[
        { validator: checkAddress, trigger: 'blur' }
    ]
})
</script>