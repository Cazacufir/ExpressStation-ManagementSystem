<template>
    <div class="container flex flex-col">
        <div class="flex justify-between">
            <el-button plain type="primary" class="ml-100" icon="Plus" @click="openForm(null)"></el-button>
            <div class="flex mr-120">
                <el-input v-model="searchFor" placeholder="查找快递员"></el-input>
                <el-button type="primary" icon="Search"></el-button>
            </div>
        </div>
        <el-table :data="deliverList" stripe style="width: 90%;margin: auto">
            <el-table-column prop="name" label="姓名" width="140" align="center" />
            <el-table-column prop="sex" label="性别" width="120" align="center" />
            <el-table-column prop="age" label="年龄" width="120" align="center" />
            <el-table-column prop="contact" label="联系方式" width="140" align="center" />
            <el-table-column prop="comName" label="所属公司" width="120" align="center" />
            <el-table-column prop="affair" label="当前事务" width="250" align="center" />
            <el-table-column fixed="right" label="Operations" width="150" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="openForm(scope)">修改</el-button>
                    <el-button link type="danger" @click.prevent="deleteRow(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="isShow" :title="showTitile" width="600px" @close="closeForm">
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
                    <el-input v-model="deliver.comName"></el-input>
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
import { onMounted, reactive, ref } from "vue";

const deliverList = ref([
    {
        name: 'test1',
        sex: 'M',
        age: 22,
        contact: '123',
        affair: 'test now',
        comName: '顺丰'
    },
    {
        name: 'test2',
        sex: 'F',
        age: 20,
        contact: '321',
        affair: 'test now',
        comName: '极兔'
    }
])

const searchFor = ref('')

let isShow = ref(false)

const deliver = reactive({
    name: '',
    sex: 'F',
    age: null,
    contact: '',
    affair: '',
    comName: ''
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

    comName: [
        { required: true, message: '职责不能为空!', trigger: 'blur' }
    ],
})

let showTitile = ref('')

const openForm = (scope) => {
    if (scope == null) {
        showTitile.value = '新增快递员'
    }
    else {
        showTitile.value = '修改快递员信息'
        console.log(scope.row)
        Object.assign(deliver,scope.row)
    }
    isShow.value = true
}

const deleteRow = (index) => {
  deliverList.value.splice(index, 1)
}

const submitDeliver = () =>{
    if(showTitile.value == '新增邮递员'){
        deliverList.value.push(deliver)
    }
    closeForm()
}
</script>