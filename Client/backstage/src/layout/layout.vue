<template>
    <el-container class="h-full overflow-y-hidden">
        <el-header class="header">
            <Header></Header>
        </el-header>

        <el-container class="h-full bg-gray-200">
            <el-aside class="carrier h-full" width="250px">
                <SideBar></SideBar>
            </el-aside>

            <el-main class="ml-15 mt-15 bg-white">
                <AppMain></AppMain>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { Header,SideBar,AppMain } from './compoents'
</script>

<style scoped lang="scss">
.header {
    background:#545c64
}
.carrier {
    background:#545c64
}
</style>