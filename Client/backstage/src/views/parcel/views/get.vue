<template>
    <div class="container flex flex-col w-full">
        <el-tabs v-model="activeName">
            <el-tab-pane label="站内包裹" name="All">
                <All></All>
            </el-tab-pane>
            <el-tab-pane label="取件记录" name="Received">
                <Received></Received>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup>
import All from './compoents/all.vue'
import Received from './compoents/received.vue';
import { reactive, ref } from 'vue';

const activeName = ref('All')

</script>