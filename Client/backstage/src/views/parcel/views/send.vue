<template>
    <div class="container flex flex-col w-full">
        <el-tabs v-model="activeName">
            <el-tab-pane label="待出库包裹" name="Out">
                <Out></Out>
            </el-tab-pane>
            <el-tab-pane label="寄送包裹" name="outByHand">
                <outByHand></outByHand>
            </el-tab-pane>
            <el-tab-pane label="寄送记录" name="Outed">
                <Outed></Outed>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup>
import Out from './compoents/out.vue'
import outByHand from './compoents/outByHand.vue';
import Outed from './compoents/outed.vue';
import { reactive, ref } from 'vue';

const activeName = ref('Out')

</script>