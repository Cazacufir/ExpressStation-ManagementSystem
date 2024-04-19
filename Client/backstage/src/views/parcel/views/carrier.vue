<template>
    <div class="container w-full flex flex-col gap-10">
        <div>
            <el-button size="large" type="primary" @click="openForm = true">+ 新增货架</el-button>
        </div>
        <div class="flex w-full gap-20 flex-wrap">
            <el-card v-for="(item, index) in carrierList" :key="index" class="card">
                <template #header>
                    <div class="card-header">
                        <el-text type="primary">货架序号</el-text>
                    </div>
                </template>

                <div class="flex justify-center items-center h-100 flex-col detailBar cursor-pointer"
                    @click="toDetail(item.carrierId)">
                    <span class="text-[#409eff] text-9xl num">{{ item.num }}</span>

                    <div class="flex justify-center items-center flex-col gap-10 detail">
                        <el-text>货架层数：{{ item.flats }}</el-text>
                        <el-text>最大容纳数/层：{{ item.maxCount }}</el-text>
                        <el-text>现存放快件数：{{ item.currentCount }}</el-text>
                    </div>

                </div>

                <template #footer>
                    <div class="flex justify-center items-center gap-50">
                        <el-button type="primary" plain size="large" @click="openCarrier(item)">修改</el-button>
                        <el-button type="danger" plain size="large" @click="toDeleteCarrier(item,index)">删除</el-button>
                    </div>
                </template>
            </el-card>

        </div>

        <el-pagination class="m-auto" layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />

        <el-dialog v-model="openForm" title="货架编辑" class="flex flex-col justify-center items-center" width="400px" @close="closeForm">
            <el-form :model="carrier" label-width="120px" v-if="!isDelete">
                <el-form-item prop="num" label="货架序号：">
                    <el-input-number v-model="carrier.num" :min="1" />
                </el-form-item>

                <el-form-item prop="flats" label="货架层数：">
                    <el-input-number v-model="carrier.flats" :min="1" />
                </el-form-item>

                <el-form-item prop="maxCount" label="最大存放数/层：">
                    <el-input-number v-model="carrier.maxCount" :min="5" />
                </el-form-item>
            </el-form>

            <div class="flex flex-col justify-center items-center gap-15" v-else>
                <el-icon size="30" color="#f56c6c"><WarningFilled /></el-icon>
                <el-text>确定删除此货架？</el-text>
            </div>
            
            <template #footer>
                <el-button @click="closeForm">取消</el-button>
                <el-button type="primary" @click="toJudge" v-if="!isDelete">新增货架</el-button>
                <el-button type="danger" @click="toDelete" v-else>确定</el-button>
            </template>
        </el-dialog>

    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { api } from "@/api"
import { ElMessage } from 'element-plus'
import { adminStore } from "@/stores/admin.js";
import { useRouter } from 'vue-router';

const router = useRouter();
const carrierList = ref([])
const store = adminStore();

let hub_id = null

let pageNum = 1
let totalPage = null
let carrierId

let openForm = ref(false)
const carrier = reactive({
    num: 1,
    flats: 1,
    maxCount: 1,
    hub_id: null
})

onMounted(() => {
    hub_id = store.getAdminInfo().hub_id
    carrier.hub_id = hub_id
    console.log("🚀 ~ onMounted ~ hub_id:", hub_id)
    getList()
})

const toDetail = (carrierId) => {
    router.push(`/${carrierId}`)
}

const getList = async () => {
    const [e, r] = await api.getCarrier(
        pageNum,
        6,
        hub_id
    )
    carrierList.value = [...r.data.records]
    totalPage = Math.ceil(r.data.total / r.data.size)
    console.log("🚀 ~ getList ~ list.value:", list.value)
}

const changePage = (value) => {
    pageNum = value
    console.log("🚀 ~ changePage ~ page:", value)
    getList()
}

const addCarrier = async () => {
    const [e, r] = await api.addCarrier(carrier)
    if (r.code == 200) {
        ElMessage({
            message: '添加货架成功',
            type: 'success',
        })
        carrierList.value.push(carrier)
        closeForm()
    }
    else {
        ElMessage.error(r.msg)
    }
}

const checkNum = (role, value, callback) => {
    if (value <= 0) {
        callback(new Error('此项必须为正整数'))
    }
}

const carrier_rules = {
    num: [
        { validator: checkNum, trigger: 'blur' }
    ],
    flats: [
        { validator: checkNum, trigger: 'blur' }
    ],
    maxCount: [
        { validator: checkNum, trigger: 'blur' }
    ]
}

const closeForm = () => {
    carrier.num = 1
    carrier.flats = 1
    carrier.maxCount = 1
    openForm.value = false
    isModify.value = false
    isDelete.value = false
    currentIndex = null
    carrierId = null
}

let isModify = ref(false)

const openCarrier = async (item) => {
    Object.assign(carrier, item)
    openForm.value = true
    isModify.value = true
}

const updateCarrier = async () => {
    const [e, r] = await api.updateCarrier(carrier)
    if (r.code == 200) {
        ElMessage({
            message: '修改成功',
            type: 'success',
        })
        closeForm()
    }
    else {
        ElMessage.error(r.msg)
    }
}

let currentIndex

const toDeleteCarrier = (item,index) => {
    openForm.value = true
    isDelete.value = true
    currentIndex = index
    carrierId = item.carrierId
}

let isDelete = ref(false)

const toJudge = () => {
    if (isModify.value) updateCarrier()
    else{
        addCarrier()
    }
}

const toDelete = async () => {
    console.log(111)
    const [e,r] = await api.deleteCarrier(carrierId)
    console.log('r',r)
    if (r.code == 200) {
        carrierList.value.splice(currentIndex,1)
        ElMessage({
            message: '删除成功',
            type: 'success',
        })
        closeForm()
    }
    else {
        ElMessage.error(r.msg)
    }
}
</script>

<style scoped lang="scss">
.card {
    width: 350px;
}

.detail {
    display: none;
}

.num {
    display: block;
}

.detailBar:hover .detail {
    display: flex;
}

.detailBar:hover .num {
    display: none;
}
</style>