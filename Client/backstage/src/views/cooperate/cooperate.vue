<template>
    <div class="container flex gap-20 flex-wrap">
        <el-card v-for="(item, index) in companyList" :key="index" class="card">
            <template #header>
                {{ item.name }}
            </template>

            <article class="flex flex-col gap-10">
                <span>联系方式：{{ item.contact }}</span>
                <span>地址：{{ item.address }}</span>
                <span>合作时间：{{ formatDate(item.co_date) }}</span>
            </article>

            <template #footer>
                <el-button type="danger" @click="deleteCompany(item.mapId,index)">终止合作</el-button>
            </template>
        </el-card>

        <el-card class="cursor-pointer hover:bg-light-900" @click="openForm = true">
            <div class="w-300px h-200px flex items-center justify-center">
                <el-icon color="gray" size="30">
                    <Plus />
                </el-icon>
            </div>
        </el-card>
    </div>

    <el-dialog v-model="openForm" title="添加合作公司" :rules="company_rules" width="500px">
        <el-form :model="company" label-width="120px">
            <el-form-item prop="com_id" label="公司名称：">
                <el-select v-model="company.com_id" clearable placeholder="请选择快递公司">
                    <el-option v-for="(item,index) in companyName" :key="index" :label="item.name" :value="item.comId"></el-option>
                </el-select>
            </el-form-item>

            <!-- <el-form-item prop="contact" label="联系方式：">
                <el-input v-model="company.contact"></el-input>
            </el-form-item>

            <el-form-item prop="address" label="公司地址：">
                <el-input v-model="company.address"></el-input>
            </el-form-item> -->
        </el-form>

        <template #footer>
            <el-button @click="closeForm">取消</el-button>
            <el-button type="primary" @click="submitForm">提交申请</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'

const store = adminStore();
const openForm = ref(false)

const companyList = ref([])

const companyName = ref([])

onMounted(()=>{
    init()
})

let hub_id = ref()

const init = async () => {
    const hub = store.getAdminInfo()
    hub_id.value = hub.hub_id
    const[e,r] = await api.getCompanyList(hub.hub_id)
    companyList.value = [...r.data]
    const [e2,r2] = await api.getCompanyName()
    companyName.value = [...r2.data]
}

const company = reactive({
    com_id:null,
    // name: '',
    // contact: '',
    // address: '',
})

const closeForm = () =>{
    company.com_id = null,
    // company.name = ''
    // company.contact = ''
    // company.address = ''
    openForm.value = false
}

function formatDate(dateString) {
    let date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
}

const company_rules = reactive({
    com_id: [
        { required: true, message: '公司名称不能为空！', trigger: 'blur' }
    ],

    // contact: [
    //     { required: true, message: '联系方式不能为空!', trigger: 'blur' }
    // ],

    // address: [
    //     { required: true, message: '公司地址不能为空!', trigger: 'blur' }
    // ]
})

const submitForm = async () =>{
    console.log('id',company.com_id,hub_id.value)
    if(company.com_id == null){
        ElMessage.error('请选择快递公司!')
        return
    }
    const[e,r] = await api.addCompany(company.com_id,hub_id.value)
    console.log('r',r)
    if(r.code == 200){
        companyList.value.push(r.data)
        ElMessage({
            message: '添加成功！',
            type: 'success',
        })
        closeForm()
    }
    else{
        ElMessage.error('添加失败，请检查网络连接')
    }
}

const deleteCompany = async (mapId,index) =>{
    const[e,r] = await api.deleteCompany(mapId)
    if(r.code == 200){
        ElMessage({
            message: '终止合成成功！',
            type: 'success',
        })
        companyList.value.splice(index,1)
        closeForm()
    }
    else{
        ElMessage.error('请求失败，请检查网络连接')
    }
}
</script>

<style scoped lang="scss">
.card {
    width: 350px;
}
</style>