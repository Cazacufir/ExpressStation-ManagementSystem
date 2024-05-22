<template>
    <el-form ref="register_ruleFormRef" :model="user" :rules="rules_user" size="large" hide-required-asterisk="true">
        <el-form-item prop="staffContact">
            <el-input v-model="user.staffContact" placeholder="请输入您的手机号"></el-input>
        </el-form-item>

        <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" type="password" show-password></el-input>
        </el-form-item>

        <el-form-item prop="conf">
            <el-input v-model="user.conf" placeholder="请确认密码" type="password" show-password></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary w-full" @click="toSubmit">注 册</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue';

const user = reactive({
    staffContact: '',
    password: '',
    conf: ''
})

const emit = defineEmits(['getInfo'])

let register_ruleFormRef = ref()

const checkConf = (role,value,callback) => {
    if(!value) {
        callback(new Error('确认密码不能为空!'))
    }
    if(value != user.password){
        callback(new Error('两次输入的密码不一致!'))
    }
}

const checkContact = (role,value,callback) => {
    if(!value) {
        callback(new Error('手机号不能为空!'))
    }
    else if(value.length < 11){
        callback(new Error('请输入有效手机号'))
    }
}

const rules_user = reactive({
    staffContact: [
        { required: true, message: '手机号不能为空！', trigger: 'blur' }
    ],
    // staffContact:[
    //     { validator: checkContact, trigger: 'blur' }
    // ],
    password: [
        { required: true, message: '密码不能为空!', trigger: 'blur' }
    ],

    conf: [
        { validator: checkConf, trigger: 'blur' }
    ]
})

const toSubmit = () =>{
    emit('getInfo',user,true)
}

const toValidate = () => {
    register_ruleFormRef.value.validate((vaild) => {
        if(vaild){
            toSubmit()
        }
        else {
            console.log(111)
            return false
        }
    })
}
</script>