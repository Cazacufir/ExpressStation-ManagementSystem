<template>
    <div class="container flex flex-col justify-center items-center h-full p-10 overflow-hidden">
        <div class="flex justify-around items-center w-95% shadow-lg p-10">
            <div v-for="(item, index) in list" :key="index" class="flex flex-col justify-center items-center gap-15">
                <span class="text-7xl font-semibold">{{ item.name }}</span>
                <span class="text-9xl font-semibold text-[#1e80ff]">{{ item.count }}</span>
            </div>
        </div>

        <div class="flex flex-wrap gap-15 flex-1 w-full">
            <div class="w-600px h-300px" ref="priceRef"></div>
        </div>
    </div>
</template>

<script setup>
import { useRoute } from "vue-router";
import { onBeforeMount, onMounted, ref, getCurrentInstance } from "vue";
import { adminStore } from "@/stores/admin.js";
import { api } from "@/api"
import * as echarts from 'echarts';

const { appContext } = getCurrentInstance();
const Eapi = appContext.config.globalProperties.$api;
const priceRef = ref(null);

const route = useRoute()
onMounted(async () => {
    let hub_id = adminStore().getAdminInfo().hub_id
    console.log("🚀 ~ onMounted ~ hub_id:", hub_id)
    const [e1, r1] = await api.countParcel(hub_id)
    console.log("🚀 ~ onMounted ~ r1:", r1)
    list.value.forEach(item => {
        item.count = r1.data[item.count]
    })
    console.log("🚀 ~ onMounted ~ list.value:", list.value)
    getPrice(hub_id)


})

const getPrice = async (hub_id) => {
    const [e, r] = await api.getPriceWeek(hub_id)
    console.log("🚀 ~ getPrice ~ r:", r.data)

    const priceDom = priceRef.value
    const priceChart = echarts.init(priceDom)

    const currentDate = new Date();

    const tenDaysAgo = new Date();
    tenDaysAgo.setDate(tenDaysAgo.getDate() - 10);

    function formatDate(date) {
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${month}-${day}`;
    }

    const datesArray = [];
    for (let i = tenDaysAgo; i <= currentDate; i.setDate(i.getDate() + 1)) {
        datesArray.push(formatDate(new Date(i)));
    }

    const priceArray = []
    
    r.data.forEach(item => {
        priceArray.push(item.totalPrice)
    })
        
    console.log("🚀 ~ getPrice ~ priceArray:", priceArray)

    const option = {
        xAxis: {
            type: 'category',
            data: datesArray
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: priceArray,
                type: 'line'
            }
        ]
    }
    priceChart.setOption(option)
}

const list = ref([
    {
        name: '现存快件数',
        count: 'currentParcel'
    },
    {
        name: '待出库',
        count: 'pendingSendParcel'
    },
    {
        name: '待入库',
        count: 'pendingReceiveParcel'
    },
    {
        name: '今日出库',
        count: 'sendParcel'
    },
    {
        name: '今日入库',
        count: 'receiveParcel'
    },
    {
        name: '滞留件',
        count: 'overdue'
    },
])

</script>