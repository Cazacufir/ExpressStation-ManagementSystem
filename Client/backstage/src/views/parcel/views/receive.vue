<template>
    <div class="w-full flex flex-col items-center gap-20" v-if="!isLoading">
        <div class="flex flex-row-reverse w-full">
            <div class="flex ml-auto">
                <el-input v-model="searchFor" placeholder="搜索快件"></el-input>
                <el-button type="primary" icon="Search" @click="searchParcel"></el-button>
            </div>
            <div class="flex ml-20 mt-auto" v-show="!isShowAll && !isShowSearch">
                <el-button type="primary" plain icon="Refresh" size="small" @click="getList">刷新</el-button>
            </div>
            <div class="flex gap-20" v-show="isShowAll">
                <el-button type="primary" @click="inParcelList">入库所选快递</el-button>
                <el-button @click="toggleSelection">清除所选</el-button>
            </div>
        </div>

        <el-table :data="isShowSearch ? searchList : list" stripe @selection-change="handleSelectionChange" ref="multipleTableRef">
            <el-table-column fixed type="selection" width="55" />
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
            <el-table-column prop="sendAddress" label="寄件人地址" width="180" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.sendAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="收件人地址" width="180" align="center"
                show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column sortable prop="type" label="类型" width="100" align="center" />
            <el-table-column sortable prop="weight" label="重量/kg" width="120" align="center" />
            <el-table-column sortable prop="company" label="运送公司" width="120" align="center" />
            <el-table-column prop="state" label="当前状态" width="100" align="center" />
            <el-table-column prop="route" label="运输记录" width="180" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatRoute(scope.row.route) }}</span>
                </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="80" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="toReceive(scope)">入库</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- <el-pagination v-if="!isShowSearch" layout="prev, pager, next" :page-count="totalPage"
            @current-change="changePage" /> -->
    </div>
    <div class="container w-full h-full" v-else>
        <el-skeleton :rows="5" animated/>
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

const isShowAll = ref(false)

let isLoading = ref(true)

const multipleTableRef = ref()

onMounted(() => {
    init()
})

const init = async () => {
    hub_id = store.getAdminInfo().hub_id
    getList()
    isLoading.value = false
}

let pageNum = 1
let totalPage = null

const getList = async () => {
    const [e, r] = await api.getSendingParcel(
        pageNum,
        9999,
        hub_id
    )
    list.value = [...r.data.records]
    totalPage = Math.ceil(r.data.total / r.data.size)
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
    const [e, r] = await api.receiveParcelByHub(scope.row)
    if (r.code == 200) {
        list.value.splice(scope.$index, 1)
        ElMessage({
            message: '入库成功',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
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
        const [e, r] = await api.searchSendingParcel(
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
        const [e, r] = await api.searchSendingParcel(
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

let inList = []

const handleSelectionChange = (val) => {
    inList = []
    inList = val
    isShowAll.value = true
    if(val.length == 0) isShowAll.value = false
}

const inParcelList = async () => {
    const [e,r] = await api.inParcelList(inList)
    if (r.code == 200) {
        inList.forEach((value,index) => {
            list.value.forEach((v,i) => {
                if(value.parcelId == v.parcelId){
                    list.value.splice(i, 1)
                }
            })
        })
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