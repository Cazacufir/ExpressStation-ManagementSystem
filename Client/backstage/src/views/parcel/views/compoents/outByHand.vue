<template>
    <div class="container flex justify-center items-center h-full">
        <article class="h-full w-full flex flex-col justify-center items-center">
            <el-form :model="parcel" label-width="120px" :rules="parcel_rules" ref="parcel_FormRef">
                <el-form-item prop="sendName" label="寄件人姓名：">
                    <el-input v-model="parcel.sendName"></el-input>
                </el-form-item>

                <el-form-item prop="sendContact" label="联系方式：">
                    <el-input v-model="parcel.sendContact"></el-input>
                </el-form-item>

                <el-form-item prop="receiveName" label="收件人姓名：">
                    <el-input v-model="parcel.receiveName"></el-input>
                </el-form-item>

                <el-form-item prop="receiveContact" label="联系方式：">
                    <el-input v-model="parcel.receiveContact"></el-input>
                </el-form-item>

                <el-form-item prop="sendCity" label="收件人地址">
                    <el-cascader size="large" :options="pcaTextArr" v-model="parcel.receiveCity">
                    </el-cascader>
                </el-form-item>

                <el-form-item prop="receiveDetail" label="详细地址：">
                    <el-input v-model="parcel.receiveDetail"></el-input>
                </el-form-item>

                <el-form-item prop="type" label="物品类型：">
                    <el-select v-model="parcel.type" placeholder="Select" style="width: 140px">
                        <el-option v-for="(item,index) in types" :key="index" :label="item" :value="item" />
                    </el-select>
                </el-form-item>

                <el-form-item prop="weight" label="重量：">
                    <el-input-number v-model="parcel.weight" :min="1"></el-input-number>
                </el-form-item>

                <el-form-item prop="price" label="运费：">
                    <el-text type="danger">{{ '¥' + price }}</el-text>
                </el-form-item>

            </el-form>
            <el-button type="primary"size="large" @click="toValidate">点击下单</el-button>
        </article>
    </div>
</template>

<script setup>
import { api } from "@/api"
import { onMounted, reactive, ref,computed } from "vue";
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'
import { pcaTextArr } from 'element-china-area-data'

const store = adminStore();

let parcel_FormRef = ref()

const parcel = reactive({
    sendName: '',
    sendContact: '',
    sendAddress:'',
    receiveName: '',
    receiveContact: '',
    receiveCity: '',
    receiveDetail: '',
    receiveAddress: '',
    type: '',
    weight: 1,
    hub_id: null,
    state:'等待揽收',
    getOrderType:'服务点自寄',
    price:null
})

onMounted(async () => {
    const hub = store.getAdminInfo()
    parcel.hub_id = hub.hub_id
    parcel.type = types[0]
    const [e,r] = await api.getMainInfo(parcel.hub_id)
    parcel.sendAddress = r.data.address
})

const types = ['日用品', '食品', '文件', '衣物', '数码产品', '药品', '生鲜', '易碎品', '其他']

const price = computed(() => {
		if (parcel.weight <= 8) {
			return 8
		} else {
			return 8 + (parcel.weight - 8) * 2
		}
})

const toSubmit = async () => {
    parcel.price = price
    parcel.receiveCity.forEach(item => parcel.receiveAddress += item)
    parcel.receiveAddress += '_' + parcel.receiveDetail
    const [e,r] = await api.addSendList(parcel)
    if (r.code == '200') {
        ElMessage({
            message: '下单成功！',
            type: 'success',
        })
        parcel.sendName = ''
        parcel.sendContact = ''
        parcel.receiveName = ''
        parcel.receiveContact = ''
        parcel.receiveCity = ''
        parcel.receiveDetail = ''
        parcel.type = types[0]
        parcel.weight = 1
    }
    else {
        ElMessage.error(r.msg)
    }
}

const parcel_rules = {
    sendName: [
        { required: true, message: '寄件人姓名不能为空！', trigger: 'blur' }
    ],
    sendContact: [
        { required: true, message: '寄件人联系方式不能为空!', trigger: 'blur' }
    ],
    receiveName: [
        { required: true, message: '收件人姓名不能为空！', trigger: 'blur' }
    ],
    receiveContact: [
        { required: true, message: '收件人联系方式不能为空!', trigger: 'blur' }
    ],
    receiveCity: [
        { required: true, message: '收件人地址不能为空！', trigger: 'blur' }
    ],
    receiveDetail: [
        { required: true, message: '收件人地址不能为空!', trigger: 'blur' }
    ],
}

const toValidate = () => {
    parcel_FormRef.value.validate((vaild) => {
        if(vaild){
            toSubmit()
        }
        else {
            return false
        }
    })
}
</script>