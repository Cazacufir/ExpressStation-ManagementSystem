<template>
  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item v-for="(item, index) in breadList" :key='index' :to="{ path: item.path }"
       class="cursor-pointer">
      {{ item.meta.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { onBeforeMount, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { ArrowRight } from '@element-plus/icons-vue'

const breadList = ref()
const route = useRoute();

onBeforeMount(() => getBreadcrumb())

watch(route, (newValue, oldValue) => {
    if (route.path == '/login') return
    getBreadcrumb()
})

const getBreadcrumb = () => {
    let matched = route.matched.filter(item => item.meta && item.meta.title);
    const first = matched[0];
    if (!isHome(first)) {
        matched = [{ path: "/home", name: 'Home', meta: { title: "统计一览" } }].concat(matched);
    }

    breadList.value = matched.filter(item => item.meta && item.meta.title)
    console.log("🚀 ~ getBreadcrumb ~ breadList.value:", breadList.value)
}

const isHome = (route) => {
    return route.name === "Home";
}
</script>