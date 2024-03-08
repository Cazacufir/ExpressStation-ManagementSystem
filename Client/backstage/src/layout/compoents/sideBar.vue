<template>
    <el-menu default-active="1" background-color="#545c64" --active-color="#1e80ff" text-color="#fff">
        <template v-for="(tab, index) in tabs" :key="index">
            <el-sub-menu v-if="tab.children" :index="index">
                <template #title>
                    <span>{{ tab.meta.title }}</span>
                </template>
                <el-menu-item-group v-for="(item, num) in tab.children" :key="num" @click="toPage(tab.path + '/' + item.path)">
                    <el-menu-item>{{ item.meta.title }}</el-menu-item>
                </el-menu-item-group>
            </el-sub-menu>

            <el-menu-item v-else :index="index" @click="toPage(tab.path)">
                <template #title>
                    <span>{{ tab.meta.title }}</span>
                </template>
            </el-menu-item>
        </template>
    </el-menu>
</template>

<script setup>

import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const tabs = ref([])
onMounted(() => {
    tabs.value = [...router.options.routes[1].children]
    console.log(tabs.value)
})

const toPage = (path) => {
    router.replace(path)
    console.log('path',path)
}
</script>