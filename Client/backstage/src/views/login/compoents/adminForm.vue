<template>
    <el-form ref="register_ruleFormRef" :model="user" :rules="rules_user" size="large">
        <el-form-item prop="concact">
            <el-input v-model="user.concact" placeholder="请输入您的手机号"></el-input>
        </el-form-item>

        <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" type="password" show-password></el-input>
        </el-form-item>

        <el-form-item prop="conf">
            <el-input v-model="user.conf" placeholder="请确认密码" type="password" show-password></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary w-full">注 册</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue';

const user = reactive({
    concact: '',
    password: '',
    conf: ''
})

const register_ruleFormRef = ref()

const checkConf = (role,value,callback) => {
    if(!value) {
        callback(new Error('确认密码不能为空!'))
    }
    if(value != user.password){
        callback(new Error('两次输入的密码不一致!'))
    }
}

const rules_user = reactive({
    concact: [
        { required: true, message: '手机号不能为空！', trigger: 'blur' }
    ],

    password: [
        { required: true, message: '密码不能为空!', trigger: 'blur' }
    ],

    conf: [
        { validator: checkConf, trigger: 'blur' }
    ]
})
</script>