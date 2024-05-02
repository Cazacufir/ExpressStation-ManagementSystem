<template>
    <div class="container w-full flex flex-col gap-10">
        <div>
            <el-button size="large" type="info" @click="backTo">
                < 返回</el-button>
        </div>
    </div>
    <div class="w-full flex flex-col items-center">
        <el-table :data="list" stripe>
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
            <el-table-column prop="sendAddress" label="地址" width="180" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.sendAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
            <el-table-column sortable prop="receiveName" label="收件人姓名" width="150" align="center" />
            <el-table-column sortable prop="receiveAddress" label="地址" width="180" align="center"
                show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column sortable prop="type" label="类型" width="100" align="center" />
            <el-table-column sortable prop="weight" label="重量/kg" width="120" align="center" />
            <el-table-column prop="state" label="当前状态" width="100" align="center" />
            <el-table-column sortable prop="arrivalTime" label="到站时间" width="180" align="center"
                :filters="receiveFilter" :filter-method="filterReceiveDate">
                <template #default="scope">
                    <span>{{ formatArrivalTime(scope.row.arrivalTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="code" label="取件码" width="100" align="center" />

            <el-table-column fixed="right" label="操作" width="100" align="center">
                <template #default="scope">
                    <el-popconfirm title="确定要回退此包裹?请确保已事先与用户沟通。" @confirm="toSendBack(scope)">
                        <template #reference>
                            <el-button link type="danger">回退</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeMount, reactive } from 'vue'
import { api } from "@/api";
import { useRouter } from 'vue-router';

const router = useRouter();
const carrierId = parseInt(router.currentRoute.value.params.carrierId)

let pageNum = 1
let totalPage
const list = ref([])

onBeforeMount(async () => {
    console.log("🚀 ~ carrierId:", carrierId)
    getList()
})

const getList = async () => {
    const [e, r] = await api.getCarrierParcel(
        pageNum,
        6,
        carrierId
    )
    if (r.code == 200) {
        list.value = [...r.data.dataList]
        totalPage = r.data.totalPages
    }
    else {
        ElMessage.error(r.msg)
    }
}

const changePage = (value) => {
    pageNum = value
    console.log("🚀 ~ changePage ~ page:", value)
    getList()
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

const backTo = () => {
    router.go(-1)
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

const receiveFilter = [
    { text: '24小时内', value: 1 },
    { text: '一周内', value: 7 },
    { text: '两周内', value: 14 },
    { text: '滞留件', value: 15 }
]

const filterReceiveDate = (value, row) => {
    const dateStr = new Date(row.arrivalTime)
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * value)
    if (value == 15) return dateStr < start
    return dateStr >= start
}

const formatArrivalTime = (time) => {
  return time ? time : '未到站'
}
</script>