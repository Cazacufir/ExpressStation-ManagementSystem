<template>
    <el-menu :default-active="defaultChosen" background-color="#545c64" active-text-color="#fff" text-color="#fff">
        <template v-for="(tab, index) in tabs" :key="index">
            <el-sub-menu v-if="tab.children" :index="StringIndex(index)">
                <template #title>
                    <el-icon>
                        <component :is="tab.meta.icon"></component>
                    </el-icon>
                    <span>{{ tab.meta.title }}</span>
                </template>
                <el-menu-item-group v-for="(item, num) in tab.children" :key="num"
                    @click="toPage(tab.path + '/' + item.path)">
                    <el-menu-item>
                        <el-icon>
                            <component :is="item.meta.icon"></component>
                        </el-icon>
                        <span>{{ item.meta.title }}</span>
                    </el-menu-item>
                </el-menu-item-group>
            </el-sub-menu>

            <el-menu-item v-else :index="StringIndex(index)" @click="toPage(tab.path)">
                <template #title>
                    <el-icon>
                        <component :is="tab.meta.icon"></component>
                    </el-icon>
                    <span>{{ tab.meta.title }}</span>
                </template>
            </el-menu-item>
        </template>
    </el-menu>
</template>

<script setup>

import { ref, onBeforeMount } from "vue";
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const tabs = ref([])
onBeforeMount(() => {
    tabs.value = [...router.options.routes[1].children]
    tabs.value.splice(7)
    console.log(tabs.value)
})

// watch(route, (newValue, oldValue) => {
//     if (route.path == '/login' || route.path == '/register') return
//     let matched = route.matched.filter(item => item.meta && item.meta.title);
    
// })

const toPage = (path) => {
    let arr = document.getElementsByClassName("el-menu-item");
    console.log('arr',arr)
    for (let i = 0; i < arr.length; i++) {
        arr[i].onclick = function () {
            
            arr[i].classList.add("is-active");

            for (let j = 0; j < arr.length; j++) {
                if (j !== i) {
                    arr[j].classList.remove("is-active");
                }
            }
        };
    }
    router.push(path)
}

const defaultChosen = "0"

const StringIndex = (index) => {
    return index.toString()
}
</script>

<style lang="scss">
.el-menu-item.is-active {
    background-color: #1e80ff !important;
}

.el-menu{
    border:none
}
</style>