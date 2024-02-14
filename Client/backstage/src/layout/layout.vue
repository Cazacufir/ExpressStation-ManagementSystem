<template>
    <el-container>
        <el-header>
            <Header></Header>
        </el-header>

        <el-container>
            <el-aside class="w-200px">
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