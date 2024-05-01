<template>
  <el-breadcrumb separator="">
    <el-breadcrumb-item v-for="(item, index) in breadList" :key='index' :to="{ path: item.path }"
      class="cursor-pointer">
      <el-tag closable @close.stop="closeTag(item, index)">{{ item.meta.title }}</el-tag>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { onBeforeMount, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

const breadList = ref([])
const route = useRoute();
const router = useRouter();
let historyRoute = []

onBeforeMount(() => getBreadcrumb())

watch(route, (newValue, oldValue) => {
  console.log("🚀 ~ watch ~ route:", route)
  if (route.path == '/login' || route.path == '/register') return
  getBreadcrumb()
})

const getBreadcrumb = () => {
  let matched = route.matched.filter(item => item.meta && item.meta.title);
  console.log("🚀 ~ getBreadcrumb ~ matched:", matched)
  // const first = matched[0];
  // if (!isHome(first)) {
  //     matched = [{ path: "/home", name: 'Home', meta: { title: "统计一览" } }].concat(matched);
  // }

  // breadList.value = matched.filter(item => item.meta && item.meta.title)
  if (matched[0].children.length) {
    breadList.value.push(matched[1])
    historyRoute.push(matched[1])
  }
  else {
    breadList.value.push(matched[0])
    historyRoute.push(matched[0])
  }

  if (historyRoute.length > 2) {
    if (historyRoute[historyRoute.length - 2].path == historyRoute[historyRoute.length - 1].path) historyRoute.pop()
  }
  breadList.value = Array.from(new Set(breadList.value))
  console.log("🚀 ~ getBreadcrumb ~ breadList.value:", breadList.value)
  console.log("🚀 ~ getBreadcrumb ~ historyRoute:", historyRoute)
}


// const isHome = (route) => {
//     return route.name === "Home";
// }

const closeTag = (item, index) => {
  console.log("🚀 ~ closeTag ~ item:", item.path)
  if (breadList.value.length <= 1) return
  if (breadList.value[index].path == route.matched[route.matched.length - 1].path) {
    router.push(historyRoute[historyRoute.length - 2])
  }
  historyRoute = historyRoute.filter(items => items.path !== item.path);

  console.log("🚀 ~ closeTag ~ histotyRoute:", historyRoute)
  breadList.value.splice(index, 1)
}
</script>