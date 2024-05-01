<template>
    <div class="container flex flex-col">
        <el-descriptions title="个人信息" column="4" :size="size" direction="vertical" :style="blockMargin" border>
            <el-descriptions-item label="工号">{{ Staff.staffId }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ Staff.name }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ Staff.age }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ Staff.sex == 'M'? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="联系方式" span="2">{{ Staff.contact }}</el-descriptions-item>
            <el-descriptions-item label="地址" span="2">{{ Staff.address }}</el-descriptions-item>
            <el-descriptions-item label="职位" span="2">
                <el-tag size="small">{{ Staff.work }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="入职日期" span="2">{{ formatDate(Staff.joinDate) }}</el-descriptions-item>
        </el-descriptions>

        <div class="m-auto mt-50">
            <el-button plain type="info" size="large" @click="router.go(-1)">返回</el-button>
            <el-button type="primary" size="large" @click="isShow = true">修改信息</el-button>
        </div>
    </div>

    <el-dialog v-model="isShow" title="修改个人信息" width="600px" @close="isShow = false">
        <el-form :model="Staff" label-width="120px" :rules="staff_rules" ref="userInfo_RefForm">
            <el-form-item prop="name" label="姓名：">
                <el-input v-model="Staff.name"></el-input>
            </el-form-item>

            <el-form-item prop="age" label="年龄：">
                <el-input v-model="Staff.age"></el-input>
            </el-form-item>

            <el-form-item prop="sex" label="性别：">
                <el-radio-group v-model="Staff.sex">
                    <el-radio label="M" size="large">男</el-radio>
                    <el-radio label="F" size="large">女</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item prop="address" label="家庭住址：">
                <el-input v-model="Staff.address"></el-input>
            </el-form-item>

            <el-form-item prop="contact" label="联系方式：">
                <el-input v-model="Staff.contact"></el-input>
            </el-form-item>
        </el-form>

        <template #footer>
            <el-button @click="isShow = false">取消</el-button>
            <el-button type="primary" @click="toValidate" :loading="isLoading">确认</el-button>
        </template>
    </el-dialog>

</template>

<script setup>
import { computed, ref, reactive, onMounted} from 'vue'
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js"
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";

const store = adminStore()
const router = useRouter();
const isLoading = ref(false)

let userInfo_RefForm = ref()

onMounted(() => {
    init()
})

const init = async () => {
    Object.assign(Staff, store.getAdminInfo())
}

const size = ref('default')
const blockMargin = computed(() => {
    const marginMap = {
        large: '32px',
        default: '28px',
        small: '24px',
    }
    return {
        marginTop: marginMap[size.value] || marginMap.default,
    }
})

const Staff = reactive({
    staffId: '',
    name: '',
    sex: '',
    age: null,
    contact: '',
    address: '',
    work: '',
    joinDate: ''
})

const isShow = ref(false)

function formatDate(dateString) {
    let date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}年${month}月${day}日`;
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

const staff_rules = reactive({
    name: [
        { required: true, message: '姓名不能为空！', trigger: 'blur' }
    ],

    age: [
        { validator: checkAge, trigger: 'blur' }
    ],

    contact: [
        { required: true, message: '联系方式不能为空!', trigger: 'blur' }
    ],

    address: [
        { required: true, message: '地址不能为空!', trigger: 'blur' }
    ]
})

const submitStaff = async () => {
    isLoading.value = true
    const [e, r] = await api.updateStaffInfo(Staff)
    if (r.code == 200) {
        isLoading.value = false
        ElMessage({
            message: '修改成功！',
            type: 'success',
        })
        isShow.value = false
    }

    else{
        ElMessage.error('修改失败，请检查网络连接')
    }
}

const toValidate = () => {
    userInfo_RefForm.value.validate((vaild) => {
        if(vaild){
            submitStaff()
        }
        else {
            return false
        }
    })
}
</script>