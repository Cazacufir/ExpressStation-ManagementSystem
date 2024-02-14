<template>
    <el-form
    ref="register_ruleFormRef"
    :model="user"
    :rules="rules"
    size="large">
        <el-form-item prop="concact">
            <el-input v-model="user.concact" placeholder="请输入您的手机号" ></el-input>
        </el-form-item>

        <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" type="password"
            show-password></el-input>
        </el-form-item>

        <el-form-item prop="conf">
            <el-input v-model="user.conf" placeholder="请确认密码" type="password"
            show-password></el-input>
        </el-form-item>


        <el-form-item>
            <el-button type="primary w-full">注 册</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive,ref } from 'vue';

const user = reactive({
    concact:'',
    password:'',
    conf:''
})

const register_ruleFormRef = ref()

const rules = reactive({
    concact:[
        { required:true,message:'手机号不能为空！',trigger:'blur' }
    ],
    password:[
        { required:true,message:'密码不能为空!',trigger:'blur' }
    ],
    conf:[
        { required:true,message:'确认密码不能为空!',trigger:'blur' }
    ]
})
</script>