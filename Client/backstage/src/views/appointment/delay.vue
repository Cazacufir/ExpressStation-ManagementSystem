<template>
        <div class="w-full flex flex-col justify-center items-center">
        <el-table :data="list" stripe>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="100" align="center" />
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="地址" width="250" align="center">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="arrivalTime" label="到站时间" width="150" align="center" />
            <el-table-column prop="days" label="延迟天数" width="100" align="center" />
            <el-table-column prop="reason" label="备注" width="150" align="center" />
            <el-table-column prop="code" label="取件码" width="100" align="center" />

            <el-table-column fixed="right" label="操作" width="80" align="center">
                <template #default="scope">
                    <el-popconfirm title="确定要回退此包裹?请确保已与用户事先沟通。" @confirm="toSendBack(scope)">
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
import { api } from "@/api"
import { onMounted, reactive, ref} from "vue";
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
    const [e,r] = await api.returnParcel(scope.row.parcelId)
    if(r.code == 200){
        list.value.splice(scope.$index,1)
        ElMessage({
            message: '回退成功',
            type: 'success',
        })
    }
    else {
        ElMessage.error(r.msg)
    }
}
</script>