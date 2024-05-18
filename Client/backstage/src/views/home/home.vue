<template>
    <div class="container flex flex-col justify-center items-center overflow-y-hidden gap-20" v-if="!isLoading">
        <div class="flex justify-around items-center w-95% shadow-lg p-10">
            <div v-for="(item, index) in list" :key="index" class="flex flex-col justify-center items-center gap-10">
                <span class="text-7xl font-semibold">{{ item.name }}</span>
                <span class="text-9xl font-semibold text-[#1e80ff]">{{ item.count }}</span>
            </div>
        </div>

        <div class="flex flex-wrap gap-15 flex-1 w-full gap-15">
            <div class="w-600px h-250px" ref="priceRef"></div>
            <div class="w-600px h-250px" ref="kindRef"></div>
            <div class="w-600px h-250px" ref="userRef"></div>
            <div class="w-600px h-250px" ref="parcelRef"></div>
        </div>
    </div>

    <div class="container w-full h-full" v-else>
        <el-skeleton animated />
        <br />
        <el-skeleton style="--el-skeleton-circle-size: 100px" animated>
            <template #template>
                <el-skeleton-item variant="circle" />
            </template>
        </el-skeleton>
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
const kindRef = ref(null)
const userRef = ref(null)
const parcelRef = ref(null)
const store = adminStore();

let isLoading = ref(true)

const route = useRoute()
onMounted(async () => {
    let hub_id = store.getAdminInfo().hub_id
    // console.log("🚀 ~ onMounted ~ hub_id:",  store.getAdminInfo())
    const [e1, r1] = await api.countParcel(hub_id)
    // console.log("🚀 ~ onMounted ~ r1:", r1.data)
    list.value.forEach(item => {
        item.count = r1.data[item.count]
    })
    // console.log("🚀 ~ onMounted ~ list.value:", list.value)
    getPrice(hub_id)
    getKind(hub_id)
    getUserCount(hub_id)
    getParcelCount(hub_id)
    isLoading.value = false
})

const getPrice = async (hub_id) => {
    const [e, r] = await api.getPriceWeek(hub_id)
    // console.log("🚀 ~ getPrice ~ r:", r.data)

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

    let priceArray = []

    r.data.forEach(item => {
        priceArray.push(item.totalPrice)
    })

    if(!r.data.length){
        priceArray = [0,0,0,0,0,0,0,0,0,0,0]
    }

    // console.log("🚀 ~ getPrice ~ priceArray:", priceArray)

    const option = {
        title: {
            text: '驿站营业额'
        },
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

const getKind = async (hub_id) => {
    const [e, r] = await api.getKindsParcel(hub_id)
    // console.log("🚀 ~ getKind ~ r:", r.data)

    const kindDom = kindRef.value
    const kindChart = echarts.init(kindDom)

    const dataArray = []
    for (let item in r.data) {
        dataArray.push({
            value: r.data[item],
            name: item
        })
    }
    // console.log("🚀 ~ getKind ~ dataArray:", dataArray)

    const option = {
        color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de'],
        title: {
            text: '现有快件种类',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                type: 'pie',
                radius: '50%',
                data: dataArray,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
    kindChart.setOption(option)
}

const getUserCount = async (hub_id) => {
    const [e, r] = await api.countUser(hub_id)
    // console.log("🚀 ~ getUserCount ~ r:", r)

    const userDom = userRef.value
    const userChart = echarts.init(userDom)

    const currentDate = new Date();

    const tenDaysAgo = new Date();
    tenDaysAgo.setDate(tenDaysAgo.getDate() - 10);

    function formatDate(date) {
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${month}-${day}`;
    }

    let datesArray = [];
    for (let i = tenDaysAgo; i <= currentDate; i.setDate(i.getDate() + 1)) {
        datesArray.push(formatDate(new Date(i)));
    }

    let sendArray = []
    r.data.send.forEach(item => sendArray.push(item.counts))

    let receiveArray = []
    r.data.receive.forEach(item => receiveArray.push(item.counts))

    if(!r.data.length){
        sendArray = [0,0,0,0,0,0,0,0,0,0,0]
        receiveArray = [0,0,0,0,0,0,0,0,0,0,0]
    }


    const option = {
        title: {
            text: '用户收件与寄件情况'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {},
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: datesArray
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        series: [
            {
                name: '寄件人数',
                type: 'bar',
                data: sendArray
            },
            {
                name: '取件人数',
                type: 'bar',
                data: receiveArray
            }
        ]
    };

    userChart.setOption(option);
}

const getParcelCount = async (hub_id) => {
    const [e, r] = await api.countSRParcel(hub_id)
    // console.log("🚀 ~ getParcelCount ~ r:", r)

    const parcelDom = parcelRef.value
    const parcelChart = echarts.init(parcelDom)

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

    const sendArray = []
    r.data.sendParcel.forEach(item => sendArray.push(item.counts))

    const receiveArray = []
    r.data.receiveParcel.forEach(item => receiveArray.push(item.counts))

    const sumArray = []
    for (let i = 0; i < 10; i++) {
        sumArray[i] = sendArray[i] + receiveArray[i]
    }

    const option = {
        title: {
            text: '快件吞吐量'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['收件数', '寄件数', '吞吐量']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: datesArray
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '收件数',
                type: 'line',
                stack: 'Total',
                data: receiveArray
            },
            {
                name: '寄件数',
                type: 'line',
                stack: 'Total',
                data: sendArray
            },
            {
                name: '吞吐量',
                type: 'line',
                stack: 'Total',
                data: sumArray
            }
        ]
    };

    parcelChart.setOption(option);
}
</script>