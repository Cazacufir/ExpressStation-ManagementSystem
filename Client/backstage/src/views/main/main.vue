<template>
    <div class="container flex flex-col">
        <!-- <div class="header h-50 w-full flex flex-row-reverse">
            <el-text tag="b" size="large">驿站主体信息</el-text>
        </div>

        <div class="MainInfo w-40% h-50vh mr-100 flex flex-col justify-center items-center">
            <el-form ref="ruleFormRef" :model="Info" lable-width="120px" :rules="hub_rules" hide-required-asterisk>

                <el-form-item prop="id" label="驿站ID：" v-show="!isModify">
                    <el-text>{{ Info.id }}</el-text>
                </el-form-item>

                <el-form-item prop="name" label="驿站名称：">
                    <el-input v-model="Info.name" v-show="isModify"></el-input>
                    <el-text v-show="!isModify">{{ Info.name }}</el-text>
                </el-form-item>


                <el-form-item prop="address" label="驿站地址：">
                    <el-input v-show="isModify" v-model="Info.address"></el-input>
                    <el-text v-show="!isModify">{{ Info.address }}</el-text>
                </el-form-item>

                <el-form-item prop="open_time" label="营业时间：" class="time">
                    <el-input v-show="isModify" v-model="Info.open_time"></el-input><el-text v-show="!isModify">{{
                Info.open_time }}</el-text> ~
                    <el-input v-show="isModify" v-model="Info.close_time"></el-input><el-text v-show="!isModify">{{
                Info.close_time }}</el-text>
                </el-form-item>

                <el-form-item prop="contact" label="联系方式：">
                    <el-input v-show="isModify" v-model="Info.contact"></el-input>
                    <el-text v-show="!isModify">{{ Info.contact }}</el-text>
                </el-form-item>

                <el-form-item prop="setupDate" label="成立时间：" v-show="!isModify">
                    <el-text>{{ formatDate(Info.setupDate) }}</el-text>
                </el-form-item>
            </el-form>

            <div class="btnBar">
                <el-button type="primary" v-show="!isModify" size="large" @click="isModify = true">修改信息</el-button>
                <el-button type="danger" v-show="isModify" @click="isModify = false">取消</el-button>
                <el-button type="primary" v-show="isModify" @click="toValidate">确定</el-button>
            </div>

        </div> -->
        <el-descriptions title="驿站主体信息" column="6" :size="size" direction="vertical" :style="blockMargin" border>
            <el-descriptions-item label="驿站ID" span="2">{{ Info.id }}</el-descriptions-item>
            <el-descriptions-item label="驿站名称" span="2">{{ Info.name }}</el-descriptions-item>
            <el-descriptions-item label="营业时间" span="2">{{ Info.open_time + '~' + Info.close_time
                }}</el-descriptions-item>
            <el-descriptions-item label="驿站地址" span="6">{{ Info.address }}</el-descriptions-item>
            <el-descriptions-item label="联系方式" span="2">{{ Info.contact }}</el-descriptions-item>
            <el-descriptions-item label="成立时间" span="2">{{ formatDate(Info.setupDate) }}</el-descriptions-item>
        </el-descriptions>

        <div class="m-auto mt-50">
            <el-button type="primary" size="large" @click="openForm">修改驿站信息</el-button>
        </div>

        <el-drawer v-model="isShow" direction="rtl">
            <template #header>
                <h4>修改驿站信息</h4>
            </template>
            <template #default>
                <div>
                    <el-form ref="ruleFormRef" :model="modify" lable-width="120px" :rules="hub_rules"
                        hide-required-asterisk @close="cancelClick">

                        <el-form-item prop="name" label="驿站名称">
                            <el-input v-model="modify.name"></el-input>
                        </el-form-item>


                        <el-form-item prop="city" label="驿站地址">
                            <!-- <el-input v-model="modify.address"></el-input> -->
                            <el-cascader size="large" :options="pcaTextArr" v-model="modify.city"
                                :placeholder="modify.city" style="width: 100%;">
                            </el-cascader>
                        </el-form-item>

                        <el-form-item prop="detail" label="详细地址">
                            <el-input v-model="modify.detail"></el-input>
                        </el-form-item>


                        <el-form-item prop="open_time" label="营业时间" class="time">
                            <!-- <el-input v-model="modify.open_time"></el-input> -->
                            <el-time-picker format='HH:mm' value-format='HH:mm' v-model="modify.open_time" arrow-control
                                placeholder="选择营业开始时间" />
                            <div class="mt-10 mb-10">
                                <el-text>至</el-text>
                            </div>
                            <el-time-picker format='HH:mm' value-format='HH:mm' v-model="modify.close_time"
                                arrow-control placeholder="选择营业结束时间" />
                            <!-- <el-input v-model="modify.close_time"></el-input> -->
                        </el-form-item>

                        <el-form-item prop="contact" label="联系方式">
                            <el-input v-model="modify.contact"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
            </template>
            <template #footer>
                <div style="flex: auto">
                    <el-button @click="cancelClick">取消</el-button>
                    <el-button type="primary" @click="toValidate">提交</el-button>
                </div>
            </template>
        </el-drawer>
    </div>
</template>

<script setup>
import { api } from "@/api"
import { onMounted, reactive, ref, computed, onBeforeMount } from "vue";
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'
import { pcaTextArr } from 'element-china-area-data'
import { ElMessageBox } from 'element-plus'

const store = adminStore();

const Info = reactive({})

const modify = reactive({})

const ruleFormRef = ref()

let isShow = ref(false)

onBeforeMount(() => {
    init()
})

let showAddress

const init = async () => {
    const hub = store.getAdminInfo()
    console.log("🚀 ~ init ~ hub:", hub)
    const [e, r] = await api.getMainInfo(hub.hub_id)
    Object.assign(Info, r.data)
    console.log("🚀 ~ init ~ Info:", Info)
    showAddress = r.data.address
    const word = r.data.address.split("_")
    modify.city = word[0]
    modify.detail = word[1]
    Info.address = Info.address.replace(/_/g, '')
}


const formatDate = (dateString) => {
    let dateObject = new Date(dateString);
    let year = dateObject.getFullYear();
    let month = dateObject.getMonth() + 1;
    let day = dateObject.getDate();
    let formattedDate = `${year}年${month}月${day}日`;
    return formattedDate
}

const checkAddress = async (role, value, callback) => {
    if (!value) {
        callback(new Error('地址不能为空!'))
    }
    if (isShow.value == true) {
        const [e, r] = await api.vertifyHub(modify)
        if (r.code != 200) {
            callback(new Error(r.msg))
        }
    }
    callback()
}

const checkName = async (role, value, callback) => {
    if (!value) {
        callback(new Error('名称不能为空!'))
    }
    if (isShow.value == true) {
        const [e, r] = await api.vertifyHub(modify)
        if (r.code != 200) {
            callback(new Error(r.msg))
        }
    }
    callback()
}

const hub_rules = reactive({
    name: [
        { validator: checkName, trigger: 'blur' }
    ],
    contact: [
        { required: true, message: '联系方式不能为空！', trigger: 'blur' }
    ],
    city: [
        { required: true, message: '驿站地址不能为空！', trigger: 'blur' }
    ],
    detail: [
        { required: true, message: '驿站地址不能为空！', trigger: 'blur' }
    ]
})

const submitForm = async () => {
    modify.address = ''
    if (Array.isArray(modify.city)) {
        console.log("🚀 ~ submitForm ~ modify.city:", modify.city)
        modify.city.forEach(item => modify.address += item)
        modify.address += '_' + modify.detail
    }
    else {
        modify.address = modify.city + '_' + modify.detail
    }

    console.log("🚀 ~ submitForm ~ modify.address:", modify)
    const [e, r] = await api.updateHubInfo(modify)
    if (r.code == 200) {
        ElMessage({
            message: '修改成功！',
            type: 'success',
        })
        showAddress = modify.address
        console.log("🚀 ~ submitForm ~ showAddress:", showAddress)
        isShow.value = false
        Object.assign(Info, modify)
        Info.address = Info.address.replace(/_/g, '')
    }
    else {
        ElMessage.error('修改失败，请检查网络连接')
    }
}

const toValidate = () => {
    ElMessageBox.confirm('确认修改吗?')
        .then(() => {
            ruleFormRef.value.validate((vaild) => {
                if (vaild) {
                    submitForm()
                }
                else {
                    return false
                }
            })
        })
        .catch(() => {
            ElMessage({
            message: '未知错误',
            type: 'error',
        })
        })

}

const size = ref('large')
const blockMargin = computed(() => {
    const marginMap = {
        large: '32px',
        default: '28px',
        small: '24px',
    }
    return {
        marginTop: marginMap[size.value] || marginMap.default,
    }
})

function cancelClick() {
    const word = showAddress.split("_")
    modify.city = word[0]
    modify.detail = word[1]
    isShow.value = false
}

const openForm = () => {
    isShow.value = true
    Object.assign(modify, Info)
    console.log("🚀 ~ openForm ~ modify:", modify)
}

</script>

<style scope lang="scss">
.time .el-input {
    --el-input-width: 100px;
}
</style>