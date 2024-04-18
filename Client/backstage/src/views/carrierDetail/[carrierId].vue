<template>
    <div class="container w-full flex flex-col gap-10">
        <div>
            <el-button size="large" type="info" @click="backTo">< 返回</el-button>
        </div>
    </div>
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
            <el-table-column prop="arrivalTime" label="到站时间" width="150" align="center" />
            <el-table-column prop="code" label="取件码" width="100" align="center" />

            <el-table-column fixed="right" label="操作" width="80" align="center">
                <template #default="scope">
                    <el-button link type="primary">出库</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />
    </div>
</template>

<script setup>
import { ref, onMounted,onBeforeMount,reactive } from 'vue'
import { api } from "@/api";
import { useRouter } from 'vue-router';

const router = useRouter();
const carrierId = parseInt(router.currentRoute.value.params.carrierId)

let pageNum = 1
let totalPage
const list = ref([])

onBeforeMount(async ()=> {
    console.log("🚀 ~ carrierId:", carrierId)
    getList()
})

const getList = async () => {
    const [e, r] = await api.getCarrierParcel(
        pageNum,
        6,
        carrierId
    )
    if(r.code == 200){
        list.value = [...r.data.dataList]
        totalPage = r.data.totalPages
    }
    else{
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
</script>