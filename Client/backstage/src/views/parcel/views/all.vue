<template>
  <div class="w-full flex flex-col items-center">
    <el-table :data="list" stripe>
            <el-table-column prop="parcelId" label="快递单号" width="100" align="center" />
            <el-table-column prop="receiveName" label="收件人姓名" width="150" align="center" />
            <el-table-column prop="receiveAddress" label="地址" width="180" align="center">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
            <el-table-column prop="sendName" label="邮寄人姓名" width="120" align="center" />
            <el-table-column prop="sendAddress" label="地址" width="180" align="center">
                <template #default="scope">
                    <span>{{ formatAddress(scope.row.sendAddress) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
            <el-table-column prop="type" label="类型" width="100" align="center" />
            <el-table-column prop="weight" label="重量/kg" width="100" align="center" />
            <el-table-column prop="company" label="运送公司" width="100" align="center" />
            <el-table-column prop="state" label="当前状态" width="100" align="center" />

            <el-table-column prop="route" label="运输记录" width="180" align="center" show-overflow-tooltip="true">
              <template #default="scope">
                    <span>{{ formatRoute(scope.row.route) }}</span>
                </template>                       
            </el-table-column>
            <el-table-column prop="arrivalTime" label="到站时间" width="180" align="center">
              <template #default="scope">
                    <span>{{ formatArrivalTime(scope.row.arrivalTime) }}</span>
                </template>              
            </el-table-column>
            <el-table-column prop="code" label="取件码" width="100" align="center">
              <template #default="scope">
                    <span>{{ formatCode(scope.row.code) }}</span>
                </template>              
            </el-table-column>
            <el-table-column prop="receiveTime" label="取件时间" width="100" align="center">
              <template #default="scope">
                    <span>{{ formatReceiveTime(scope.row.receiveTime) }}</span>
                </template>              
            </el-table-column>
            <!-- <el-table-column fixed="right" label="操作" width="80" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="toReceive(scope)">入库</el-button>
                </template>
            </el-table-column> -->
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
let pageNum = 1
let totalPage = null

onMounted(()=> {
  init()
})

const getList = async () => {
    const [e, r] = await api.getAllParcelByHub(
        pageNum,
        6,
        hub_id
    )
    list.value = [...r.data.records]
    totalPage = Math.ceil(r.data.total/r.data.size) 
    console.log("🚀 ~ getList ~ list.value:", list.value)
}

const init = async () => {
  hub_id = store.getAdminInfo().hub_id
  getList()
}

const formatAddress = (address) => {
    return address.replace(/_/g, '')
}

const changePage = (value) => {
    pageNum = value
    console.log("🚀 ~ changePage ~ page:", value)
    getList()
}

const formatArrivalTime = (time) => {
  return time? time : '未到站'
}

const formatCode = (code) => {
  return code? code : '——'
}

const formatReceiveTime = (time) => {
  return time? time : '暂未领取'
}

const formatRoute = (route) => {
  let routeStr = ''
  let newStr = ''
  route.split(',').forEach(item => {
    let Str = item.split('_')
    if(Str[0] == '已揽收'){
      newStr += '[' + Str[1] + ']' + '快件从' + Str[2] + '寄出'
    }
    else if(Str[0] == '运输中'){
      newStr += '[' + Str[1] + ']' + '快件已到达' + Str[2]
    }
    else if(Str[0] == '派送中'){
      newStr += '[' + Str[1] + ']' + Str[3]
    }
    else if(Str[0] == '已入库'){
      newStr += '[' + Str[1] + ']' + '快件到达此驿站'
    }
    routeStr += newStr
  })
  return routeStr;
}
</script>

