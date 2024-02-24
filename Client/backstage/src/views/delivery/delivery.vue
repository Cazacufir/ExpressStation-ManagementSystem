<template>
    <div class="container">
        <el-table :data="deliver" stripe>
            <el-table-column prop="name" label="姓名" width="180" />
            <el-table-column prop="sex" label="性别" width="180" />
            <el-table-column prop="contact" label="联系方式" width="180" />
            <el-table-column prop="affair" label="当前事务" width="180" />
        </el-table>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";

const deliver = ref([
    {
        name:'test1',
        sex:'M',
        contact:'123',
        affair:'test now'
    },
    {
        name:'test2',
        sex:'F',
        contact:'321',
        affair:'test now'
    }
])
</script>