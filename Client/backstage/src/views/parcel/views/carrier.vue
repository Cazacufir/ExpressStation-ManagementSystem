<template>
    <div class="container w-full flex flex-col gap-10">
        <div>
            <el-button size="large" type="primary">+ 新增货架</el-button>
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
                        <el-button type="primary" plain size="large">修改</el-button>
                        <el-button type="danger" plain size="large">删除</el-button>
                    </div>
                </template>
            </el-card>

        </div>

        <el-pagination class="m-auto" layout="prev, pager, next" :page-count="totalPage" @current-change="changePage" />
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

onMounted(async () => {
    hub_id = store.getAdminInfo().hub_id
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