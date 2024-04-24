<template>
    <el-form ref="hub_ruleFormRef" :model="hub" :rules="rules_hub" size="large" label-width="auto"
        hide-required-asterisk="true">
        <el-form-item prop="role" label="您的身份：">
            <el-radio-group v-model="hub.role">
                <el-radio :label="0" size="large">我是管理员</el-radio>
                <el-radio :label="1" size="large">我是站长</el-radio>
            </el-radio-group>
        </el-form-item>

        <el-form-item v-show="hub.role == 0" prop="hub_id" label="驿站ID">
            <el-input v-model.number="hub.hub_id" placeholder="请输入待加入的驿站ID"></el-input>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="name" label="驿站名称">
            <el-input v-model="hub.name"></el-input>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="hubContact" label="驿站联系方式">
            <el-input v-model="hub.hubContact"></el-input>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="city" label="驿站地址">
            <el-cascader size="large" :options="pcaTextArr" v-model="hub.city">
            </el-cascader>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="detail" label="详细地址">
            <el-input v-model="hub.detail"></el-input>
        </el-form-item>

        <el-form-item v-show="hub.role == 1" prop="close" label="营业时间">
            <el-time-picker format = 'HH:mm' value-format = 'HH:mm' v-model="hub.open_time" arrow-control placeholder="选择营业开始时间" />
            <div class="mt-10 mb-10">
                <el-text>至</el-text>
            </div>
            <el-time-picker format = 'HH:mm' value-format = 'HH:mm' v-model="hub.close_time" arrow-control placeholder="选择营业结束时间" />
        </el-form-item>

        <el-form-item>
            <el-button type="primary m-auto" @click="toValidate">已完成，下一步</el-button>
        </el-form-item>

    </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { api } from "@/api"
import { ElMessage } from 'element-plus'
import { pcaTextArr } from 'element-china-area-data'

const hub = reactive({
    role: 0,
    hub_id: null,
    name: '',
    city: '',
    detail: '',
    address: '',
    close_time: '',
    open_time: ''
})

const emit = defineEmits(['getHub'])

const hub_ruleFormRef = ref()

const checkId = (role, value, callback) => {
    if (hub.role == 0) {
        if (value == null) {
            callback(new Error('驿站ID不能为空!'))
        }
    }
    else {
        callback()
    }
}

const checkName = (role, value, callback) => {
    if (hub.role == 1) {
        if (value == '') {
            callback(new Error('驿站名称不能为空!'))
        }
    }
    else {
        callback()
    }
}

// const checkAddress = (role, value, callback) => {
//     if (hub.role == 1) {
//         if (value == '') {
//             callback(new Error('驿站地址不能为空!'))
//         }
//     }
//     else {
//         callback()
//     }
// }

const checkContact = (role, value, callback) => {
    if (hub.role == 1) {
        if (value == '') {
            callback(new Error('联系方式不能为空!'))
        }
    }
    else {
        callback()
    }
}

const checkTime = (role, value, callback) => {
    if (hub.role == 1) {
        if (value == '' || hub.open == '') {
            callback(new Error('营业时间不能为空!'))
        }
    }
    else {
        callback()
    }
}

const rules_hub = reactive({
    hub_id: [
        { validator: checkId, trigger: 'blur' }
    ],
    name: [
        { validator: checkName, trigger: 'blur' }
    ],
    detail: [
        { required: true, message: '详细地址不能为空', trigger: 'blur' }
    ],
    city: [
        { required: true, message: '地址不能为空', trigger: 'blur' }
    ],
    hubContact: [
        { validator: checkContact, trigger: 'blur' }
    ],
    close: [
        { validator: checkTime, trigger: 'blur' }
    ]
})

const toSubmit = async () => {
    hub.city.forEach(item => hub.address += item)
    hub.address += '_' + hub.detail
    console.log("🚀 ~ toSubmit ~ hub:", hub)
    const [e, r] = await api.vertifyInfo(hub)
    if (r.code == 200) emit('getHub', hub, true)
    else {
        ElMessage.error(r.msg)
        emit('getHub', null, false)
    }
}

const toValidate = () => {
    console.log('hub',hub)
    hub_ruleFormRef.value.validate((vaild) => {
        if (vaild) {
            toSubmit()
        }
        else {
            return false
        }
    })
}
</script>