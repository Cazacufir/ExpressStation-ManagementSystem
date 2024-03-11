<template>
    <div class="container flex flex-col">
        <div class="flex justify-between">
            <el-button plain type="primary" class="ml-40" icon="Plus"></el-button>
            <div class="flex mr-160">
                <el-input v-model="searchFor" placeholder="查找员工"></el-input>
                <el-button type="primary" icon="Search"></el-button>
            </div>
        </div>
        <el-table :data="staff" stripe>
            <el-table-column prop="staffId" label="工号" width="120" align="center" />
            <el-table-column prop="name" label="姓名" width="120" align="center" />
            <el-table-column prop="sex" label="性别" width="120" align="center" />
            <el-table-column prop="work" label="职位" width="120" align="center" />
            <el-table-column prop="contact" label="联系方式" width="120" align="center" />
            <el-table-column prop="address" label="家庭住址" width="180" align="center" />
            <el-table-column prop="joinDate" label="入职日期" width="120" align="center" />
            <el-table-column fixed="right" label="Operations" width="150" align="center">
                <template #default>
                    <el-button link type="primary">修改</el-button>
                    <el-button link type="danger">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";

const staff = ref([
    {
        staffId: '123',
        name: 'test1',
        sex: 'M',
        contact: '123',
        address: '123',
        work: '管理员',
        joinDate: '2024-02-24'
    },
    {
        staffId: '124',
        name: 'test2',
        sex: 'F',
        contact: '321',
        address: '123',
        work: '管理员',
        joinDate: '2024-02-24'
    }
])

const searchFor = ref('')
</script>

<style lang="scss" scoped>
.el-input {
    width: 200px;
}
</style>