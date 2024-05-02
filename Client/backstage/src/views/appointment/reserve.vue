<template>
    <div class="w-full flex flex-col justify-center items-center">
        <el-table :data="list" stripe>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column sortable prop="receiveName" label="收件人姓名" width="120" align="center" />
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="地址" width="180" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="100" align="center" />
            <el-table-column prop="weight" label="重量/kg" width="100" align="center" />
            <el-table-column sortable prop="arrivalTime" label="到站时间" width="180" align="center"
                :filters="receiveFilter" :filter-method="filterReceiveDate">
                <template #default="scope">
                    <span>{{ formatArrivalTime(scope.row.arrivalTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="code" label="取件码" width="100" align="center" />
            <el-table-column sortable prop="dateTime" label="预约时间" width="200" align="center"
                :filters="receiveFilter2" :filter-method="filterReceiveDate" />
            <el-table-column sortable prop="name" label="配送员" width="100" align="center" />

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
import { api } from "@/api"
import { onMounted, reactive, ref } from "vue";
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'

const store = adminStore();
const list = ref([])
let hub_id = null
let pageNum = 1
let totalPage = null

onMounted(() => {
    hub_id = store.getAdminInfo().hub_id
    getList()
})

const getList = async () => {
    const [e, r] = await api.getAllReserveParcel(
        pageNum,
        6,
        hub_id
    )
    list.value = [...r.data.dataList]
    totalPage = r.data.totalPages
    console.log("🚀 ~ getList ~ list.value:", list.value)
}

const formatAddress = (address) => {
    return address.replace(/_/g, '')
}

const changePage = (value) => {
    pageNum = value
    console.log("🚀 ~ changePage ~ page:", value)
    getList()
}

const toSend = async (scope) => {
    const [e, r] = await api.receiveSingleParcel(scope.row.parcelId)
    if (r.code == 200) {
        list.value.splice(scope.$index, 1)
        ElMessage({
            message: '出库成功，物流已更新',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
}

const receiveFilter = [
  { text: '24小时内', value: 1 },
  { text: '一周内', value: 7 },
  { text: '两周内', value: 14 }
]

const filterReceiveDate = (value, row) => {
  const dateStr = new Date(row.arrivalTime)
  const start = new Date()
  start.setTime(start.getTime() - 3600 * 1000 * 24 * value)
  return dateStr >= start
}

const receiveFilter2 = [
  { text: '24小时内', value: 1 },
  { text: '两天内', value: 2 },
  { text: '三天内', value: 3 }
]

const formatArrivalTime = (time) => {
  return time ? time : '未到站'
}
</script>