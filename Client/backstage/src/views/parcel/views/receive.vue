<template>
    <!-- <div class="container flex justify-center items-center h-50vh">
        <article class="h-100% w-500 flex flex-col justify-center items-center gap-20 h-50vh">
            <el-input v-model="parcelId" placeholder="请输入快递ID码">
                <template #append>
                    <el-button :icon="Search" />
                </template>
            </el-input>

            <el-button round>点击此处上传快递条形码</el-button>

            <el-radio-group v-model="receive">
                <el-radio label="1" size="large">自动入库</el-radio>
                <el-radio label="2" size="large">手动入库</el-radio>
            </el-radio-group>

            <section class="flex" v-show="receive == '2'">
                <label for="carrier" class="w-90 leading-32px">货架号：</label>
                <el-input id="carrier"></el-input>
            </section>

            <el-button type="primary">点击入库</el-button>

        </article>
    </div> -->
    <div class="w-full flex flex-col items-center">
        <el-table :data="list" stripe>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="sendName" label="邮寄人姓名" width="120" align="center" />
            <el-table-column prop="sendAddress" label="地址" width="180" align="center">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.sendAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="地址" width="180" align="center">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column prop="type" label="类型" width="100" align="center" />
            <el-table-column prop="weight" label="重量/kg" width="100" align="center" />
            <el-table-column prop="state" label="当前状态" width="100" align="center" />

            <el-table-column fixed="right" label="操作" width="80" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="toReceive(scope)">入库</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { Search } from '@element-plus/icons-vue'
import { adminStore } from "@/stores/admin.js";
import { api } from "@/api"
import { ElMessage } from 'element-plus'

const store = adminStore();
const list = ref([])
let hub_id = null

onMounted(()=> {
    init()
})

const init = async () => {
    hub_id = store.getAdminInfo().hub_id
    getList()
}

let pageNum = 1
let totalPage = null

const getList = async () => {
    const [e, r] = await api.getSendingParcel(
        pageNum,
        6,
        hub_id
    )
    list.value = [...r.data.records]
    totalPage = Math.ceil(r.data.total/r.data.size) 
    // console.log("🚀 ~ getList ~ list.value:", list.value)
    console.log("🚀 ~ getList ~ r:", r)
}

const formatAddress = (address) => {
    return address.replace(/_/g, '')
}

const changePage = (value) => {
    pageNum = value
    console.log("🚀 ~ changePage ~ page:", value)
    getList()
}

const toReceive = async (scope) => {
    const [e,r] = await api.receiveParcel(scope.row)
    if(r.code == 200){
        list.value.splice(scope.$index,1)
        ElMessage({
            message: '入库成功',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
}
</script>