<template>
    <div class="container flex flex-col gap-20 items-center">
        <div class="formBar p-20 w-30%">
            <el-form ref="pswFormRef" :model="psw" :rules="rules_psw" size="large" label-width="auto" hide-required-asterisk="true">

                <el-form-item label="原密码" prop="rawPassword">
                    <el-input v-model="psw.rawPassword" placeholder="请输入原密码"></el-input>
                </el-form-item>

                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="psw.newPassword" placeholder="请输入新密码"></el-input>
                </el-form-item>


                <el-form-item label="确认密码" prop="checkPassword">
                    <el-input v-model="psw.checkPassword" placeholder="请输入确认密码"></el-input>
                </el-form-item>
            </el-form>
        </div>

        <div class="flex gap-5">
            <el-button size="large" @click="router.go(-1)">返回</el-button>
            <el-button size="large" type="primary" @click="toValidate">修改密码</el-button>
        </div>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { utils } from "@/utils/session.js";
import { useRouter } from "vue-router";
import { api } from "@/api"
import { ElMessage } from 'element-plus'

const router = useRouter();

onMounted(() => {
    init()
})

let pswFormRef = ref()

const psw = reactive({
    rawPassword: '',
    newPassword: '',
    checkPassword: '',
    adminId: ''
})

const init = () => {
    const res = utils.getSession('admin')
    psw.adminId = res.adminId
}

const checkConf = (role, value, callback) => {
    if (!value) {
        callback(new Error('确认密码不能为空!'))
    }
    if (value != psw.newPassword) {
        callback(new Error('两次输入的密码不一致!'))
    }
}

const rules_psw = reactive({
    rawPassword: [
        { required: true, message: '原密码不能为空！', trigger: 'blur' }
    ],
    newPassword: [
        { required: true, message: '新密码不能为空！', trigger: 'blur' }
    ],
    ccheckPasswordonf: [
        { validator: checkConf, trigger: 'blur' }
    ]
})

const toValidate = () => {
    pswFormRef.value.validate((vaild) => {
        if(vaild){
            toUpdate()
        }
        else {
            return false
        }
    })
}

const toUpdate = async () => {
    const [e,r] = await api.updatePassword(psw)
    if(r.code == 200){
        ElMessage({
            message: '修改成功！',
            type: 'success',
        })
        router.go(-1)
    }
    else {
        ElMessage.error(r.msg)
    }
}
</script>

<style lang="scss" scoped>

</style>