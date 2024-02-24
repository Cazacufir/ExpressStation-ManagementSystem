<template>
    <div class="container">
        <el-table :data="staff" stripe>
            <el-table-column prop="staffId" label="工号" width="180" />
            <el-table-column prop="name" label="姓名" width="180" />
            <el-table-column prop="sex" label="性别" width="180" />
            <el-table-column prop="contact" label="联系方式" width="180" />
            <el-table-column prop="joinDate" label="入职日期" width="180" />
        </el-table>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";

const staff = ref([
    {
        staffId:'123',
        name:'test1',
        sex:'M',
        contact:'123',
        joinDate:'2024-02-24'
    },
    {
        staffId:'124',
        name:'test2',
        sex:'F',
        contact:'321',
        joinDate:'2024-02-24'
    }
])
</script>