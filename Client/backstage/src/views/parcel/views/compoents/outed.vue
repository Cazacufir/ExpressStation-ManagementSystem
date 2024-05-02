<template>
    <div class="w-full flex flex-col items-center gap-20">
        <div class="flex flex-row-reverse w-full">
            <div class="flex ml-auto">
                <el-input v-model="searchFor" placeholder="搜索已出库快件"></el-input>
                <el-button type="primary" icon="Search" @click="searchParcel"></el-button>
            </div>
        </div>

        <el-table :data="isShowSearch ? searchList : list" stripe>
            <el-table-column fixed type="expand">
                <template #default="scope">
                    <div class="ml-15 flex gap-15">
                        <div class="w-200px">
                            <p m="t-0 b-4">收件人地址：</p>
                            <p m="t-0 b-4">{{ formatAddress(scope.row.receiveAddress) }}</p><br>
                            <p m="t-0 b-4">寄件人地址：</p>
                            <p m="t-0 b-4">{{ formatAddress(scope.row.sendAddress) }}</p>
                        </div>

                        <div style="white-space: pre-line;">
                            <p m="t-0 b-4">运输历史：</p>
                            <p m="t-0 b-4">{{ formatRoute(scope.row.route) }}</p>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="sendName" label="寄件人姓名" width="120" align="center" />
            <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="收件人地址" width="180" align="center" show-overflow-tooltip="true">
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
            <el-table-column sortable prop="company" label="运送公司" width="120" align="center" />
            <el-table-column prop="state" label="当前状态" width="100" align="center">
                <template #default="scope">
                    <span>{{ formatState(scope.row.state) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="route" label="运输记录" width="180" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatRoute(scope.row.route) }}</span>
                </template>
            </el-table-column>
            <el-table-column sortable prop="arrivalTime" label="到站时间" width="180" align="center"
                :filters="receiveFilter" :filter-method="filterReceiveDate">
                <template #default="scope">
                    <span>{{ formatArrivalTime(scope.row.arrivalTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="orderType" sortable label="订单类型" width="120" align="center" />

            <el-table-column prop="orderTime" sortable label="下单时间" width="180" align="center" :filters="receiveFilter"
                :filter-method="filterReceiveDate">
                <template #default="scope">
                    <span>{{ formatDate(scope.row.orderTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="sendTime" sortable label="运输开始时间" width="180" align="center" :filters="receiveFilter"
                :filter-method="filterReceiveDate">
                <template #default="scope">
                    <span>{{ formatDate(scope.row.sendTime) }}</span>
                </template>
            </el-table-column>
        </el-table>

         <el-pagination v-if="!isShowSearch" layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />
    </div>
</template>

<script setup>
import { onMounted, reactive, ref, watchEffect } from "vue";
import { adminStore } from "@/stores/admin.js";
import { api } from "@/api"

const store = adminStore();
const list = ref([])
let hub_id = null


onMounted(() => {
    init()
})

const init = () => {
    hub_id = store.getAdminInfo().hub_id
    getList()
}

let pageNum = 1
let totalPage = null

const getList = async () => {
    const [e, r] = await api.getOutedParcel(
        pageNum,
        9999,
        hub_id
    )
    list.value = [...r.data.dataList]
    list.value.forEach(item => {
        let word = item.state.split('_')
        if (word.length == 2) {
            item.staff = word[1]
        }
        else {
            item.staff = "-"
        }
    })
    totalPage = r.data.totalPages
    console.log("🚀 ~ getList ~ list.value:", list.value)
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

let isShowSearch = ref(false)
const searchList = ref([])
const searchFor = ref()

watchEffect(() => {
    if (searchFor.value == '') {
        isShowSearch.value = false;
    }
})

const searchParcel = async () => {
    if (searchFor.value == null) {
        return
    }
    searchList.value = []
    const word = parseInt(searchFor.value)
    if (word) {
        const [e, r] = await api.searchOutedParcel(
            hub_id,
            searchFor.value,
            0
        )
        if (r.code == 200) {
            searchList.value = [...r.data]
            isShowSearch.value = true
        }
        else {
            ElMessage.error(r.msg)
        }
    }
    else {
        const [e, r] = await api.searchOutedParcel(
            hub_id,
            0,
            searchFor.value
        )
        if (r.code == 200) {
            searchList.value = [...r.data]
            isShowSearch.value = true
        }
        else {
            ElMessage.error(r.msg)
        }
    }
}

const receiveFilter = [
    { text: '24小时内', value: 1 },
    { text: '一周内', value: 7 },
    { text: '一月内', value: 30 },
    { text: '三月内', value: 90 }
]

const filterReceiveDate = (value, row) => {
    const dateStr = new Date(row.orderTime)
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * value)
    return dateStr >= start
}

const formatState = (item) => {
    let word = item.split('_')
    if (word) return word[0]
    return item
}

const formatRoute = (route) => {
    // let routeStr = ''
    if (!route) return '-'
    let newStr = ''
    route.split(',').forEach(item => {
        let Str = item.split('_')
        if (Str[0] == '已揽收') {
            newStr += '[' + Str[1] + ']' + '快件从' + Str[2] + '寄出\n'
        }
        else if (Str[0] == '运输中') {
            newStr += '[' + Str[1] + ']' + '快件已到达' + Str[2] + '\n'
        }
        else if (Str[0] == '派送中') {
            newStr += '[' + Str[1] + ']' + Str[3] + '\n'
        }
        else if (Str[0] == '待取件') {
            newStr += '[' + Str[1] + ']' + '快件到达此驿站\n'
        }
    })
    console.log("🚀 ~ formatRoute ~ newStr:", newStr)
    return newStr;
}

const formatArrivalTime = (time) => {
    return time ? time : '未到站'
}

const formatReceiveTime = (time) => {
    return time ? time : '暂未领取'
}

const changePage = (value) => {
  pageNum = value
  console.log("🚀 ~ changePage ~ page:", value)
  getList()
}
</script>