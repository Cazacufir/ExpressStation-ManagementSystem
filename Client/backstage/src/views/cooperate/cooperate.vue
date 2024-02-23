<template>
    <div class="container flex gap-20 flex-wrap">
        <el-card v-for="(item, index) in test" :key="index" class="card">
            <template #header>
                {{ item.name }}
            </template>

            <article class="flex flex-col gap-10">
                <span>联系方式：{{ item.contact }}</span>
                <span>地址：{{ item.address }}</span>
                <span>合作日期：{{ item.co_date }}</span>
            </article>

            <template #footer>
                <el-button type="danger">终止合作</el-button>
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

    <el-dialog v-model="openForm" title="添加合作公司" width="500px">
        <el-form :model="company" label-width="120px">
            <el-form-item label="公司名称：">
                <el-input v-model="company.name"></el-input>
            </el-form-item>

            <el-form-item label="联系方式：">
                <el-input v-model="company.contact"></el-input>
            </el-form-item>

            <el-form-item label="公司地址：">
                <el-input v-model="company.address"></el-input>
            </el-form-item>
        </el-form>

        <template #footer>
            <el-button @click=closeForm>取消</el-button>
            <el-button type="primary">提交申请</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";

const openForm = ref(false)

const test = ref([
    {
        name: '顺丰快递',
        contact: '123',
        address: '深圳',
        co_date: '2024-02-23'
    },
    {
        name: '申通快递',
        contact: '123',
        address: '北京',
        co_date: '2024-02-23'
    },
])

const company = reactive({
    name: '',
    contact: '',
    addresses: '',
})

const closeForm = () =>{
    company.name = '';
    company.contact = '';
    company.address = ''
    openForm.value = false
}
</script>

<style scoped lang="scss">
.card {
    width: 300px;
}

</style>