<template>
    <div class="container flex flex-col w-full">
        <el-tabs v-model="activeName">
            <el-tab-pane label="出库订单" name="Out">
                <Out></Out>
            </el-tab-pane>
            <el-tab-pane label="手动出库" name="outByHand">
                <outByHand></outByHand>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup>
import Out from './compoents/out.vue'
import outByHand from './compoents/outByHand.vue';
import { reactive, ref } from 'vue';

const activeName = ref('Out')

</script>