<template>
    快递管理页面
    <router-view :key="key"/>
</template>

<script setup>
import { useRoute } from "vue-router";

const route = useRoute();
const key = () => {
    return route.path
}
</script>