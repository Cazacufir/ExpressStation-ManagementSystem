<template>
    <div class="container flex justify-center items-center h-50vh">
        <article class="h-100% w-500 flex flex-col justify-center items-center gap-20 h-50vh">
            <el-input v-model="parcelId" placeholder="请输入快递编号">
                <template #append>
                    <el-button :icon="Search" />
                </template>
            </el-input>

            <!-- <el-button round>点击此处上传用户二维码</el-button> -->

            <el-button type="primary">点击出库</el-button>

        </article>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { Search } from '@element-plus/icons-vue'

let parcelId = ref('')
</script>