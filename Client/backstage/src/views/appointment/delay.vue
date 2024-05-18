<template>
    <div class="w-full flex flex-col justify-center items-center" v-if="!isLoading">
        <div class="w-full">
            <el-button type="primary" plain icon="Refresh" size="small" @click="getList">刷新</el-button>
        </div>
        <el-table :data="list" stripe>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="100" align="center" />
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="地址" width="250" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column sortable prop="arrivalTime" label="到站时间" width="180" align="center"
                :filters="receiveFilter" :filter-method="filterReceiveDate" />
            <el-table-column sortable prop="days" label="延迟天数" width="120" align="center" />
            <el-table-column prop="reason" label="备注" width="150" align="center" />
            <el-table-column prop="code" label="取件码" width="100" align="center" />

            <el-table-column fixed="right" label="操作" width="120" align="center">
                <template #default="scope">
                    <el-popconfirm title="确定要回退此包裹?请确保已与用户事先沟通。" @confirm="toSendBack(scope)">
                        <template #reference>
                            <el-button link type="danger">回退</el-button>
                        </template>
                    </el-popconfirm>
                    <el-popover placement="left" :width="250" trigger="click">
                        <template #reference>
                            <el-button link type="info">延迟</el-button>
                        </template>
                        <div class="flex flex-col justify-center items-center gap-15">
                            <el-text>更改延迟天数</el-text>
                            <el-input-number v-model="days" :min="1" :max="14"/>
                            <el-button type="primary" @click="toDelay(scope)">确定</el-button>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />
    </div>
    <div class="container w-full h-full" v-else>
        <el-skeleton :rows="5" animated/>
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

let isLoading = ref(true)

let days = ref(1)

onMounted(() => {
    hub_id = store.getAdminInfo().hub_id
    getList()
    isLoading.value = false
})

const getList = async () => {
    const [e, r] = await api.getDelayParcel(
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

const toSendBack = async (scope) => {
    const [e, r] = await api.returnParcel(scope.row.parcelId)
    if (r.code == 200) {
        list.value.splice(scope.$index, 1)
        ElMessage({
            message: '回退成功',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
}

const receiveFilter = [
    { text: '24小时内', value: 1 },
    { text: '两天内', value: 2 },
    { text: '三天内', value: 3 }
]

const filterReceiveDate = (value, row) => {
    const dateStr = new Date(row.arrivalTime)
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * value)
    return dateStr >= start
}

const toDelay = async (scope) => {
    const [e,r] = await api.addDelay(scope.row.parcelId,days.value)
    if (r.code == 200) {
        ElMessage({
            message: '延迟成功',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
    days.value = 1
}
</script>