<template>
    <div class="container"></div>
    此处放echarts
</template>

<script setup>
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";

const route = useRoute()
onMounted(()=>{
    console.log(route)
})
const test = {
    icon:'Histogram'
}
</script>