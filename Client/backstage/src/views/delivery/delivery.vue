<template>
    <div class="container flex flex-col">
        <div class="flex justify-between">
            <el-button plain type="primary" class="ml-100" icon="Plus" @click="openForm(null)"></el-button>
            <div class="flex mr-120">
                <el-input v-model="searchFor" placeholder="查找快递员"></el-input>
                <el-button type="primary" icon="Search" @click="searchDeliver"></el-button>
            </div>
        </div>
        <el-table :data="isShowSearch ? searchList : deliverList" stripe>
            <el-table-column prop="deliverId" label="快递员编号" width="100" align="center" />
            <el-table-column prop="name" label="姓名" width="140" align="center" />
            <el-table-column prop="sex" label="性别" width="120" align="center" />
            <el-table-column prop="age" label="年龄" width="120" align="center" />
            <el-table-column prop="contact" label="联系方式" width="140" align="center" />
            <el-table-column prop="comName" label="所属公司" width="120" align="center" />
            <el-table-column prop="affair" label="当前事务" width="250" align="center" />
            <el-table-column fixed="right" label="操作" width="150" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="openForm(scope)">修改</el-button>
                    <el-popconfirm title="确定要删除此配送员?" @confirm="deleteRow(scope)">
                        <template #reference>
                            <el-button link type="danger">删除</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="isShow" :title="showTitile" width="600px" @close="closeForm" ref="delivery_RefForm">
            <el-form :model="deliver" label-width="120px" :rules="deliver_rules">
                <el-form-item prop="name" label="姓名：">
                    <el-input v-model="deliver.name"></el-input>
                </el-form-item>

                <el-form-item prop="age" label="年龄：">
                    <el-input v-model="deliver.age"></el-input>
                </el-form-item>

                <el-form-item prop="sex" label="性别：">
                    <el-radio-group v-model="deliver.sex">
                        <el-radio label="F" size="large">男</el-radio>
                        <el-radio label="M" size="large">女</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item prop="contact" label="联系方式：">
                    <el-input v-model="deliver.contact"></el-input>
                </el-form-item>

                <el-form-item v-show="showTitile === '新增快递员'" prop="comName" label="所属公司：">
                    <el-select v-model="deliver.com_id" clearable placeholder="请选择快递公司">
                        <el-option v-for="(item, index) in companyName" :key="index" :label="item.name"
                            :value="item.comId"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="closeForm">取消</el-button>
                <el-button type="primary" @click="submitDeliver">确认</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref, watchEffect } from "vue";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js";
import { ElMessage } from 'element-plus'

const store = adminStore();

const deliverList = ref([])

let searchFor = ref()

let isShow = ref(false)

let currentIndex = null

let delivery_RefForm = ref()

onMounted(() => {
    init()
})

const companyName = ref([])

const init = async () => {
    const hub = store.getAdminInfo()
    const [e, r] = await api.getAllDeliver(hub.hub_id)
    deliverList.value = [...r.data]
    deliver.hub_id = hub.hub_id
    const [e2,r2] = await api.getCompanyName()
    companyName.value = [...r2.data]
}

const deliver = reactive({
    name: '',
    sex: 'F',
    age: null,
    contact: '',
    affair: '',
    comName: '',
    com_id:null,
    mapId:null
})

const closeForm = () => {
    deliver.name = ''
    deliver.age = null
    deliver.sex = 'F'
    deliver.contact = ''
    deliver.address = ''
    deliver.comName = ''
    isShow.value = false
}

const searchDeliver = async () => {
    if (searchFor.value == null) {
        return
    }
    deliverList.value = []
    const word = parseInt(searchFor.value)
    if (word) {
        const [e, r] = await api.getDeliver(searchFor.value, null)
        if (r.code == 200) {
            deliverList.value.push(r.data)
            isShowSearch.value = true
        }
        else {
            ElMessage.error(r.msg)
        }
    }
    else {
        const [e, r] = await api.getDeliver(null, searchFor.value)
        if (r.code == 200) {
            deliverList.value = [...r.data]
            isShowSearch.value = true
        }
        else {
            ElMessage.error(r.msg)
        }
    }
}

const checkAge = (role, value, callback) => {
    if (!value) {
        callback(new Error('年龄不能为空!'))
    }
    else if (value < 18 || value > 70) {
        callback(new Error('请输入合法年龄!'))
    }
    callback()
}

const deliver_rules = reactive({
    name: [
        { required: true, message: '姓名不能为空！', trigger: 'blur' }
    ],

    age: [
        { validator: checkAge, trigger: 'blur' }
    ],

    contact: [
        { required: true, message: '联系方式不能为空!', trigger: 'blur' }
    ],

    comName: [
        { required: true, message: '所属公司不能为空!', trigger: 'blur' }
    ],
})

let showTitile = ref('')

const openForm = (scope) => {
    if (scope == null) {
        showTitile.value = '新增快递员'
    }
    else {
        showTitile.value = '修改快递员信息'
        currentIndex = scope.$index
        console.log(scope.row)
        Object.assign(deliver, scope.row)
    }
    isShow.value = true
}

const updateForm = async () => {
    const [e, r] = await api.updateDeliver(deliver)
    if (r.code == 200) {
        ElMessage({
            message: '修改成功！',
            type: 'success',
        })
        Object.assign(deliverList.value[currentIndex], deliver)
        closeForm()
    }

    else {
        ElMessage.error('修改失败，请检查网络连接')
    }
}

const addDeliver = async () => {
    const [e, r] = await api.addDeliver(deliver)
    if (r.code == 200) {
        deliverList.value.push(r.data)
        ElMessage({
            message: '新增快递员成功！',
            type: 'success',
        })
        closeForm()
    }

    else {
        ElMessage.error('修改失败，请检查网络连接')
    }
}

const deleteRow = async (scope) => {
    const [e, r] = await api.deleteDeliver(scope.row.mapId)
    if (r.code == 200) {
        deliverList.value.splice(scope.$index, 1)
        ElMessage({
            message: '删除成功！',
            type: 'success',
        })
        closeForm()
    }

    else {
        ElMessage.error('删除失败，请检查网络连接')
    }
}

const submitDeliver = () => {
    if (showTitile.value == '新增快递员') {
        toValidate()
    }
    else {
        toValidateUpdate()
    }
}

const toValidate = () => {
    delivery_RefForm.value.validate((vaild) => {
        if(vaild){
            addDeliver()
        }
        else {
            return false
        }
    })
}

const toValidateUpdate = () => {
    delivery_RefForm.value.validate((vaild) => {
        if(vaild){
            updateForm()
        }
        else {
            return false
        }
    })
}


watchEffect(() => {
    if (searchFor.value == '') {
        isShowSearch.value = false;
    }
})
</script>