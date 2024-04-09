<template>
    <div class="w-full flex flex-col items-center">
        <el-table :data="list" stripe>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="sendName" label="邮寄人姓名" width="120" align="center" />
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
            <el-table-column prop="price" label="已付金额" width="100" align="center">
                <template #default="scope">
                    <span>{{ formatPrice(scope.row.price) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="state" label="当前状态" width="100" align="center" />
            <el-table-column prop="orderTime" label="下单时间" width="140" align="center">
                <template #default="scope">
                    <span>{{ formatDate(scope.row.orderTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="80" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="toSend(scope)">出库</el-button>
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

const store = adminStore();
const list = ref([])
let hub_id = null

let pageNum = 1

onMounted(() => {
    init()
})

const init = () => {
    hub_id = store.getAdminInfo().hub_id
    getList()
}

let totalPage = null

const getList = async () => {
    const [e, r] = await api.getSendListByHub(
        pageNum,
        1,
        hub_id
    )
    list.value = [...r.data.dataList]
    totalPage = r.data.totalPages
    console.log("🚀 ~ getList ~ list.value:", list.value)
}

const toSend = async () => {

}

function formatDate(dateString) {
    let date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0')
    const min = String(date.getMinutes()).padStart(2, '0')
    const sec = String(date.getSeconds()).padStart(2, '0')

    return `${year}-${month}-${day} ${hours}:${min}:${sec}`;
}

const formatAddress = (address) => {
    return address.replace(/_/g, '')
}

const formatPrice = (price) => {
    return '¥' + price
}

const changePage = (value) => {
    pageNum = value
    console.log("🚀 ~ changePage ~ page:", value)
    getList()
}
</script>