<template>
    <div class="container flex w-full gap-20 flex-wrap">
        <el-card v-for="(item, index) in carrierList" :key="index" class="card">
            <template #header>
                <div class="card-header">
                    <el-text type="primary">货架序号</el-text>
                </div>
            </template>

            <div class="flex justify-center items-center h-100 flex-col detailBar cursor-pointer">
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

        <el-card class="cursor-pointer hover:bg-light-900" @click="openForm = true">
            <div class="w-300px h-200px flex items-center justify-center">
                <el-icon color="gray" size="30">
                    <Plus />
                </el-icon>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { api } from "@/api"
import { ElMessage } from 'element-plus'
import { adminStore } from "@/stores/admin.js";

const carrierList = ref([])
const store = adminStore();

let hub_id = null

onMounted(async () => {
    hub_id = store.getAdminInfo().hub_id
    const [e, r] = await api.getCarrier(hub_id)
    if (r.code == 200) {
        carrierList.value = [...r.data]
    }
})
</script>

<style scoped lang="scss">
.card {
    width: 350px;
}

.detail{
    display: none;
}

.num{
    display: block;
}

.detailBar:hover .detail{
    display: flex;
}

.detailBar:hover .num{
    display: none;
}
</style>