<template>
    <el-form
    ref="login_ruleFormRef"
    :model="user"
    :rules="rules"
    size="large"
    class="h-full w-full flex flex-1 flex-col pt-40">
        <el-form-item prop="contact">
            <el-input v-model="user.contact" placeholder="请输入您的手机号" style="width:250px"></el-input>
        </el-form-item>

        <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" type="password"
            show-password style="width:250px"></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" w-full @click="toValidate">登 录</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive,ref } from 'vue';
import { useRouter } from "vue-router";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js";

const store = adminStore();
const router = useRouter();
const user = reactive({
    contact:'',
    password:''
})

const login_ruleFormRef = ref()

const rules = reactive({
    contact:[
        { required:true,message:'手机号不能为空！',trigger:'blur' }
    ],
    password:[
        { required:true,message:'密码不能为空!',trigger:'blur' }
    ]
})

const toValidate = async () => {
    // const [e,r] = await api.login(user)
    // console.log('res',r)
    // if(r.code == '200'){
    //     store.setAdminInfo(r.data)
    //     router.replace('/main')
    // }
    router.replace('/home')
}
</script>