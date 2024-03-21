<template>
    <el-form ref="info_ruleFormRef" :model="info" :rules="rules_info" size="large" label-width="auto" hide-required-asterisk="true">

        <el-form-item prop="staffName" label="您的姓名">
            <el-input v-model="info.staffName"></el-input>
        </el-form-item>

        <el-form-item prop="age" label="您的年龄">
            <el-input v-model.number="info.age"></el-input>
        </el-form-item>

        <el-form-item prop="sex" label="您的性别：">
            <el-radio-group v-model="info.sex">
                <el-radio label="F" size="large">男</el-radio>
                <el-radio label="M" size="large">女</el-radio>
            </el-radio-group>
        </el-form-item>

        <el-form-item prop="staffAddress" label="家庭地址">
            <el-input v-model="info.staffAddress"></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary m-auto" @click="toSubmit">已完成，下一步</el-button>
        </el-form-item>

        </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue';

const info_ruleFormRef = ref()
const emit = defineEmits(['getAdmin'])

const info = reactive({
    age:null,
    sex:'F',
    staffName:null,
    staffAddress:null,
})

const checkAge = (role,value,callback) =>{
    if(!value){
        callback(new Error('年龄不能为空!'))
    }
    else if(value < 18 || value >70 ){
        callback(new Error('请输入合法年龄!'))
    }
    callback()
}

const rules_info = reactive({
    age:[
    { validator: checkAge, trigger: 'blur' }
    ],
    sex:[
    { required: true, message: '性别不能为空！', trigger: 'blur' }
    ],
    staffName:[
    { required: true, message: '姓名不能为空！', trigger: 'blur' }
    ],
    staffAddress:[
    { required: true, message: '地址不能为空！', trigger: 'blur' }
    ]
})

const toSubmit = () => {
    emit('getAdmin',info,true)
}
</script>