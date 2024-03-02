<template>
    <div class="container h-full w-full flex pt-40">
        <div class="w-150px flex flex-1 items-center">
            <el-icon v-show="activeNum > 0" class="cursor-pointer ml-20" color="#1e80ff" @click="forward"><ArrowLeft /></el-icon>
        </div>

        <Transition name="slide-fade" mode="out-in">
            <!-- <el-form v-show="activeNum == 0" ref="hub_ruleFormRef" :model="hub" :rules="rules_hub" size="large"></el-form>
            <el-form v-show="activeNum == 2" ref="register_ruleFormRef" :model="user" :rules="rules_user" size="large">
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
            </el-form> -->
            <component :is="forms[activeNum]"></component>
        </Transition>

        <div class="w-150px flex flex-1 items-center flex-row-reverse">
            <el-icon v-show="activeNum < 2" class="cursor-pointer mr-20" color="#1e80ff" @click="next"><ArrowRight /></el-icon>
        </div>
    </div>
</template>



<script setup>
import { reactive, ref } from 'vue';
import adminForm from './compoents/adminForm.vue'
import hubForm from './compoents/hubForm.vue'
import infoForm from './compoents/userInfoForm.vue'

const forms = [ hubForm,infoForm,adminForm ]

// const user = reactive({
//     concact: '',
//     password: '',
//     conf: ''
// })

// const register_ruleFormRef = ref()

// const rules_user = reactive({
//     concact: [
//         { required: true, message: '手机号不能为空！', trigger: 'blur' }
//     ],

//     password: [
//         { required: true, message: '密码不能为空!', trigger: 'blur' }
//     ],

//     conf: [
//         { required: true, message: '确认密码不能为空!', trigger: 'blur' },
//         { validator: checkConf, trigger: 'blur' }
//     ]
// })

const activeNum = ref(0)

// const hub = reactive({
//     role:0,
//     id:null,
//     name:'',
//     address:''
// })

// const hub_ruleFormRef = ref()

// const checkId = (role,value,callback) => {
//     if(hub.role == 0){
//         if(value == null){
//             callback(new Error('驿站ID不能为空!'))
//         }
//     }
//     else{
//         callback()
//     }
// }

// const checkName = (role,value,callback) => {
//     if(hub.role == 1){
//         if(value == ''){
//             callback(new Error('驿站名称不能为空!'))
//         }
//     }
//     else{
//         callback()
//     }
// }

// const checkAddress = (role,value,callback) => {
//     if(hub.role == 1){
//         if(value == ''){
//             callback(new Error('驿站地址不能为空!'))
//         }
//     }
//     else{
//         callback()
//     }
// }

// const checkConf = (role,value,callback) => {
//     if(value != user.password){
//         callback(new Error('两次输入的密码不一致!'))
//     }
// }

// const rules_hub = reactive({
//     id:[
//         { validator: checkId, trigger: 'blur' }
//     ],
//     name:[
//         { validator: checkName, trigger: 'blur' }
//     ],
//     address:[
//         { validator: checkAddress, trigger: 'blur' }
//     ]
// })

const next = () =>{
    if(activeNum.value++ > 2) activeNum.value = 0
}

const forward = () => {
    if(activeNum.value-- < 0) activeNum.value = 0
}
</script>

<style scoped lang="scss">
.slide-fade-enter-active,
.slide-fade-leave-active {
    transition: all 300ms cubic-bezier(0.4, 0.4, 0.25, 1.35);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
    transform: translate3d(10%, 0, 0);
    opacity: 0;
}
</style>