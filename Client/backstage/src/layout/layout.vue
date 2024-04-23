<template>
    <el-container class="h-full overflow-y-hidden">
        <el-header class="header">
            <Header></Header>
        </el-header>

        <el-container class="h-full bg-gray-200">
            <el-aside class="carrier" width="250px">
                <el-scrollbar height="690">
                    <SideBar></SideBar>
                </el-scrollbar>
            </el-aside>

            <!-- <el-main class="ml-15 mt-15 bg-white h-100% overflow-y-hidden">
                <AppMain></AppMain>
            </el-main> -->
            <el-container class="overflow-hidden">
                <el-header height="30px" class="flex items-center bg-white w-full ml-15 mt-5">
                    <Bread></Bread>
                </el-header>
                <el-main class="ml-15 mt-5 bg-white h-100% overflow-y-hidden">
                    <AppMain></AppMain>
                </el-main>
            </el-container>
        </el-container>
    </el-container>
</template>

<script setup>
import { Header, SideBar, AppMain,Bread } from './compoents'
</script>

<style scoped lang="scss">
.header {
    background: #545c64
}

.carrier {
    background: #545c64
}
</style>