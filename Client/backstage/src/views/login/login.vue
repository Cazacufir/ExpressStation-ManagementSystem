<template>
    <el-form ref="login_ruleFormRef" :model="user" :rules="rules" size="large"
        class="h-full w-full flex flex-1 flex-col pt-40">
        <el-form-item prop="showContact">
            <el-input v-model="user.showContact" placeholder="请输入您的手机号" style="width:250px"></el-input>
        </el-form-item>

        <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" type="password" show-password
                style="width:250px"></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" w-full @click="toValidate">登 录</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from "vue-router";
import { utils } from "@/utils/session.js";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'

const store = adminStore();
const router = useRouter();
const user = reactive({
    contact: 'ad@',
    showContact:'',
    password: ''
})

const login_ruleFormRef = ref()

const rules = reactive({
    showContact: [
        { required: true, message: '手机号不能为空！', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '密码不能为空!', trigger: 'blur' }
    ]
})

const toValidate = () => {
    login_ruleFormRef.value.validate((vaild) => {
        if(vaild){
            toLogin()
        }
        else {
            return false
        }
    })
}

const toLogin = async () => {
    user.contact = 'ad@'
    user.contact += user.showContact    
    const [e, r] = await api.login(user)
    console.log("🚀 ~ toLogin ~ e:", e)
    console.log('res', r)
    if(!r.msg){
        toLogin()
    }
    if (r.msg != '认证或授权失败') {
        utils.setSession("token", r.data.token);
        // utils.setSession("admin",r.data.adminInfoVo)
        store.setAdminInfo(r.data.adminInfoVo)
        router.replace('/home')
        ElMessage({
            message: '登录成功！',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
}
</script>