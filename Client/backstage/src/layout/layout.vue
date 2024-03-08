<template>
    <el-container>
        <el-header class="header">
            <Header></Header>
        </el-header>

        <el-container>
            <el-aside class="carrier w-200px h-100vh" >
                <SideBar></SideBar>
            </el-aside>

            <el-main>
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