<template>
    <div class="container flex flex-col">
        <el-descriptions title="个人信息" column="4" :size="size" direction="vertical" :style="blockMargin" border>
            <el-descriptions-item label="工号">{{ Staff.staffId }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ Staff.name }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ Staff.age }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ Staff.sex }}</el-descriptions-item>
            <el-descriptions-item label="联系方式" span="2">{{ Staff.contact }}</el-descriptions-item>
            <el-descriptions-item label="地址" span="2">{{ Staff.address }}</el-descriptions-item>
            <el-descriptions-item label="职位" span="2">
                <el-tag size="small">{{ Staff.work }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="入职日期" span="2">{{ Staff.joinDate }}</el-descriptions-item>
        </el-descriptions>

        <div class="m-auto mt-50">
            <el-button type="primary" size="large" @click="isShow = true">修改信息</el-button>
        </div>
    </div>

    <el-dialog v-model="isShow" title="修改个人信息" width="600px" @close="isShow = false">
            <el-form :model="Staff" label-width="120px" :rules="staff_rules">
                <el-form-item prop="name" label="姓名：">
                    <el-input v-model="Staff.name"></el-input>
                </el-form-item>

                <el-form-item prop="age" label="年龄：">
                    <el-input v-model="Staff.age"></el-input>
                </el-form-item>

                <el-form-item prop="sex" label="性别：">
                    <el-radio-group v-model="Staff.sex">
                        <el-radio label="F" size="large">男</el-radio>
                        <el-radio label="M" size="large">女</el-radio>
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
                <el-button type="primary" @click="submitStaff">确认</el-button>
            </template>
        </el-dialog>

</template>

<script setup>
import { computed, ref, reactive } from 'vue'

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
    staffId: '123',
    name: 'test1',
    sex: 'M',
    age: 21,
    contact: '123',
    address: '123',
    work: '管理员',
    joinDate: '2024-02-24'
})

const isShow = ref(false)

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

const submitStaff = () =>{
    isShow.value = false
}
</script>