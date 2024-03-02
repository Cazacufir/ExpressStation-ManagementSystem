<template>
    <div class="container flex justify-center items-center h-50vh">
        <article class="h-100% w-500 flex flex-col justify-center items-center gap-20 h-50vh">
            <el-input v-model="parcelId" placeholder="请输入快递ID码">
                <template #append>
                    <el-button :icon="Search" />
                </template>
            </el-input>

            <el-button round>点击此处上传快递条形码</el-button>

            <el-radio-group v-model="receive">
                <el-radio label="1" size="large">自动入库</el-radio>
                <el-radio label="2" size="large">手动入库</el-radio>
            </el-radio-group>

            <section class="flex" v-show="receive == '2'">
                <label for="carrier" class="w-90 leading-32px">货架号：</label>
                <el-input id="carrier"></el-input>
            </section>

            <el-button type="primary">点击入库</el-button>

        </article>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { Search } from '@element-plus/icons-vue'

let parcelId = ref('')

let receive = ref('1')
</script>