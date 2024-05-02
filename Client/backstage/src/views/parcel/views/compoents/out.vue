<template>
    <div class="w-full flex flex-col items-center gap-20">
        <div class="flex flex-row-reverse w-full">
            <div class="flex ml-auto">
                <el-input v-model="searchFor" placeholder="搜索快件"></el-input>
                <el-button type="primary" icon="Search" @click="searchParcel"></el-button>
            </div>
            <div class="flex gap-20" v-show="isShowAll">
                <el-button type="primary" @click="outedByList">出库所选快递</el-button>
                <el-button @click="toggleSelection">清除所选</el-button>
            </div>
        </div>

        <el-table :data="isShowSearch ? searchList : list" stripe @selection-change="handleSelectionChange" ref="multipleTableRef">
            <el-table-column fixed type="selection" width="55" />
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="sendName" label="寄件人姓名" width="120" align="center" />
            <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="地址" width="180" align="center" show-overflow-tooltip="true">
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
            <el-table-column prop="state" label="当前状态" width="100" align="center">
                <template #default="scope">
                    <span>{{ formatState(scope.row.state) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="orderType" sortable label="订单类型" width="120" align="center" />
            <el-table-column prop="staff" label="配送人员" width="120" align="center" show-overflow-tooltip="true"/>

            <el-table-column prop="orderTime" sortable label="下单时间" width="180" align="center" :filters="receiveFilter"
                :filter-method="filterReceiveDate">
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

        <!-- <el-pagination v-if="!isShowSearch" layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" /> -->
    </div>
</template>

<script setup>
import { onMounted, reactive, ref, watchEffect } from "vue";
import { adminStore } from "@/stores/admin.js";
import { api } from "@/api"
import { ElMessage } from 'element-plus'

const store = adminStore();
const list = ref([])
let hub_id = null

let pageNum = 1

const isShowAll = ref(false)

onMounted(() => {
    init()
})

const init = () => {
    hub_id = store.getAdminInfo().hub_id
    getList()
}

let totalPage = null

const multipleTableRef = ref()

const getList = async () => {
    const [e, r] = await api.getSendListByHub(
        pageNum,
        9999,
        hub_id
    )
    list.value = [...r.data.dataList]
    list.value.forEach(item => {
        let word = item.state.split('_')
        if (word.length == 2){
            item.staff = word[1]
        } 
        else{
            item.staff = "-"
        }
    })
    totalPage = r.data.totalPages
    console.log("🚀 ~ getList ~ list.value:", list.value)
}

const toSend = async (scope) => {
    const [e, r] = await api.sendParcelByHub(scope.row.parcelId,hub_id)
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
        const [e, r] = await api.searchSendListByHub(
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
        const [e, r] = await api.searchSendListByHub(
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

const formatState = (item) => {
    let word = item.split('_')
    if (word) return word[0]
    return item
}

const receiveFilter = [
    { text: '24小时内', value: 1 },
    { text: '一周内', value: 7 },
    { text: '两周内', value: 14 }
]

const filterReceiveDate = (value, row) => {
    const dateStr = new Date(row.orderTime)
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * value)
    return dateStr >= start
}

let outList = []

const handleSelectionChange = (val) => {
    outList = []
    outList = val
    console.log("🚀 ~ handleSelectionChange ~ outList:", outList)
    isShowAll.value = true
    if(val.length == 0) isShowAll.value = false
}

const toggleSelection = (rows) => {
  console.log("🚀 ~ toggleSelection ~ rows:", rows)
//   if (rows) {
//     rows.forEach((row) => {
//       multipleTableRef.value.toggleRowSelection(row, undefined)
//     })
//   } else {
    multipleTableRef.value.clearSelection()
//   }
}

const outedByList = async () => {
    console.log("🚀 ~ handleSelectionChange ~ outList:", outList)
    const [e,r] = await api.outParcelList(outList)
    if (r.code == 200) {
        outList.forEach((value,index) => {
            list.value.forEach((v,i) => {
                if(value.parcelId == v.parcelId){
                    list.value.splice(i, 1)
                }
            })
        })
        ElMessage({
            message: '出库成功，物流已更新',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
}
</script>