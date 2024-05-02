<template>
  <div class="w-full flex flex-col items-center gap-20">
    <div class="flex flex-row-reverse w-full justify-between">
      <div class="flex ml-auto">
        <el-input v-model="searchFor" placeholder="搜索快件"></el-input>
        <el-button type="primary" icon="Search" @click="searchParcel"></el-button>
      </div>
      <div class="flex gap-10">
        <el-text type="info">以取件时间筛选:</el-text>
        <el-date-picker
        v-model="receiveDateTime"
        type="daterange"
        unlink-panels
        range-separator="To"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        value-format="YYYY-MM-DD HH:mm:ss"
        :shortcuts="shortcuts"
        @change="handleChange"
      />
      </div>
    </div>

    <el-table :data="isShowSearch ? searchList : list " stripe>
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
      <el-table-column prop="receiveName" sortable label="收件人姓名" width="150" align="center" />
      <el-table-column prop="receiveAddress" sortable label="地址" width="180" align="center"
        show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ formatAddress(scope.row.receiveAddress) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="receiveContact" label="联系方式" width="150" align="center" />
      <el-table-column prop="sendName" label="寄件人姓名" width="120" align="center" />
      <el-table-column prop="sendAddress" label="地址" width="180" align="center" show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ formatAddress(scope.row.sendAddress) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sendContact" label="联系方式" width="100" align="center" />
      <el-table-column sortable prop="type" label="类型" width="100" align="center" />
      <el-table-column sortable prop="weight" label="重量/kg" width="120" align="center" />
      <el-table-column sortable prop="company" label="运送公司" width="120" align="center" />
      <el-table-column sortable prop="state" label="当前状态" width="120" align="center">
        <template #default="scope">
          <span>{{ formatState(scope.row.state) }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="route" label="运输记录" width="180" align="center" show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ formatRoute(scope.row.route) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable prop="arrivalTime" label="到站时间" width="180" align="center" :filters="receiveFilter" :filter-method="filterReceiveDate">
        <template #default="scope">
          <span>{{ formatArrivalTime(scope.row.arrivalTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="code" label="取件码" width="100" align="center">
        <template #default="scope">
          <span>{{ formatCode(scope.row.code) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable prop="receiveTime" label="取件时间" width="120" align="center">
        <template #default="scope">
          <span>{{ formatReceiveTime(scope.row.receiveTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="100" align="center">
        <template #default="scope">
          <el-button v-if="scope.row.state == '已取件'" link type="danger" @click.prevent="toDelete(scope)">删除记录</el-button>
          <el-text v-else>-</el-text>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-if="!isShowSearch" layout="prev, pager, next" :page-count="totalPage"
      @current-change="changePage" />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watchEffect } from "vue";
import { adminStore } from "@/stores/admin.js";
import { api } from "@/api"
import { ElMessage, ElMessageBox } from 'element-plus'

const store = adminStore();
const list = ref([])
let hub_id = null
let pageNum = 1
let totalPage = null
let show = ref(false)

onMounted(() => {
  init()
})

const getList = async () => {
  const [e, r] = await api.getAllParcelByHub(
    pageNum,
    10,
    hub_id
  )
  list.value = [...r.data.records]
  totalPage = Math.ceil(r.data.total / r.data.size)
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
  return time ? time : '未到站'
}

const formatCode = (code) => {
  return code ? code : '——'
}

const formatReceiveTime = (time) => {
  return time ? time : '暂未领取'
}

const formatRoute = (route) => {
  // let routeStr = ''
  if(!route) return '-'
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
    const [e, r] = await api.searchAllParcelByHub(
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
    const [e, r] = await api.searchAllParcelByHub(
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
  if(word) return word[0]
  return item
}

const toDelete = (scope) => {
  ElMessageBox.confirm(
    '确认后将从系统中删除词条记录',
    '确认删除？',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async() => {
      const [e, r] = await api.deleteParcelRecord(scope.row)
      if (r.code == 200) {
        list.value.splice(scope.$index, 1)
        ElMessage({
          message: '登录成功！',
          type: 'success',
        })
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除',
      })
    })
}

const receiveFilter = [
  { text:'24小时内',value:1 },
  { text:'一周内',value:7 },
  { text:'一月内',value:30 }
]

const filterReceiveDate = (value,row) => {
  const dateStr = new Date(row.arrivalTime)
  const start = new Date()
  start.setTime(start.getTime() - 3600 * 1000 * 24 * value)
  return dateStr >= start
}

const receiveDateTime = ref()

const shortcuts = [
  {
    text: '上一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '上个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '过去3月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]

const handleChange = async () =>{
  console.log(typeof receiveDateTime.value[0])
  const [e,r] =  await api.selectAllinHub(receiveDateTime.value[0],receiveDateTime.value[1],hub_id)
}
</script>
