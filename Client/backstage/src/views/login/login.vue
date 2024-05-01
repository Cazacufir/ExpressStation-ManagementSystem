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
            <el-radio-group v-model="radio" class="m-auto">
                <el-radio label="1" size="large">记住密码</el-radio>
                <el-radio label="2" size="large">自动登录</el-radio>
            </el-radio-group>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" w-full @click="toValidate" :loading="isLoading">登 录</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive, ref,onMounted } from 'vue';
import { useRouter } from "vue-router";
import { utils } from "@/utils/session.js";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'

const store = adminStore();
const router = useRouter();
const isLoading = ref(false)
const user = reactive({
    contact: 'ad@',
    showContact: '',
    password: ''
})

onMounted(()=>{
    if(utils.getLocal('saved')){
        Object.assign(user,JSON.parse(utils.getLocal('saved')))
        radio.value = '1'
        if(utils.getLocal('AutoLogin')){
            radio.value = '2'
            toLogin()
        }
    }
})

let radio = ref('0')

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
        if (vaild) {
            toLogin()
        }
        else {
            return false
        }
    })
}

const toLogin = async () => {
    isLoading.value = true
    user.contact = 'ad@'
    user.contact += user.showContact
    const [e, r] = await api.login(user)
    console.log("🚀 ~ toLogin ~ e:", e)
    console.log('res', r)
    if (e) {
        isLoading.value = false
        utils.clearSession()
        toLogin()
    }
    if (r.msg != '认证或授权失败' && r.code != 505) {
        isLoading.value = false
        utils.setSession("token", r.data.token);
        if(radio.value === '1'){
            utils.setLocal('saved',JSON.stringify(user))
        }
        else if(radio.value === '2'){
            utils.setLocal('saved',JSON.stringify(user))
            utils.setLocal('AutoLogin',true)
        }
        // utils.setSession("admin",r.data.adminInfoVo)
        store.setAdminInfo(r.data.adminInfoVo)
        isLoading.value = false
        router.replace('/home')
        ElMessage({
            message: '登录成功！',
            type: 'success',
        })
    }
    else {
        ElMessage.error('用户名或密码错误')
    }
}
</script>