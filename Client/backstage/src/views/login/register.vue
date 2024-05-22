<template>
    <div class="container h-full w-full flex pt-40">

        <div class="w-150px flex flex-1 items-center">
            <el-icon v-show="activeNum > 0" :class="{ 'permit': showNext, 'forbidden': !showNext }" class="ml-20"
                color="#1e80ff" @click="forward">
                <ArrowLeft />
            </el-icon>
        </div>

        <Transition name="slide-fade" mode="out-in">
            <component :is="forms[activeNum]" @getHub="getHub" @getAdmin="getAdmin" @getInfo="getInfo"></component>
        </Transition>

        <div class="w-150px flex flex-1 items-center flex-row-reverse">
            <el-icon v-show="activeNum < 2" :class="{ 'permit': showNext, 'forbidden': !showNext }" class="mr-20"
                color="#1e80ff" @click="next">
                <ArrowRight />
            </el-icon>
        </div>

    </div>
</template>



<script setup>
import { reactive, ref } from 'vue';
import adminForm from './compoents/adminForm.vue'
import hubForm from './compoents/hubForm.vue'
import infoForm from './compoents/userInfoForm.vue'
import { api } from "@/api"
import { ElMessage } from 'element-plus'

const forms = [hubForm, infoForm, adminForm]

const activeNum = ref(0)

const showNext = ref(false)

const next = () => {
    if (activeNum.value++ > 2) activeNum.value = 0
}

const forward = () => {
    if (activeNum.value-- < 0) activeNum.value = 0
}

const hub = reactive({})
const admin = reactive({})
const info = reactive({})

const getHub = (data,show) => {
    Object.assign(hub,data)
    showNext.value = show
    console.log('data',data)
    console.log('show',show)
    if(show == true) next()
}

const getAdmin = (data,show) => {
    Object.assign(admin,data)
    showNext.value = show
    console.log('data',data)
    console.log('show',show)
    if(show == true) next()
}

const getInfo = (data,show) => {
    Object.assign(info,data)
    showNext.value = show
    console.log('data',data)
    console.log('show',show)
    toRegister()
}

const allInfo = reactive({})

const toRegister = async () => {
    Object.assign(allInfo,hub,admin,info)
    console.log('allInfo',allInfo)
    allInfo.contact = allInfo.staffContact
    const [e,r] = await api.register(allInfo)
    if(r.code == 200){
        ElMessage({
            message: '注册成功！',
            type: 'success',
        })
        routerKey.push('/login')
    }
    else{
        ElMessage.error(r.msg)
    }
    console.log(typeof allInfo.age)
    console.log(typeof allInfo.hub_id)
    console.log(typeof allInfo.role)
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

.permit {
    cursor: pointer;
}

.forbidden {
    cursor: not-allowed;
}
</style>