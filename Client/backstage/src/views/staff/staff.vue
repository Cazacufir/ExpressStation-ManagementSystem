<template>
    <div class="container flex flex-col gap-20">
        <div class="flex justify-between">
            <el-button plain type="primary" class="ml-10" icon="Plus" @click="openForm(null)"
                v-if="work === '站长'">新增员工</el-button>
            <div class="flex ml-auto">
                <el-input v-model="searchFor" placeholder="查找员工"></el-input>
                <el-button type="primary" icon="Search" @click="searchStaff"></el-button>
            </div>
        </div>
        <el-table :data="isShowSearch ? searchList : staffList" stripe :default-sort="{ prop: 'work', order: 'ascending' }">
            <el-table-column type="expand">
                <template #default="scope">
                    <div class="ml-15" style="white-space: pre-line;">
                        <p m="t-0 b-4">当前任务列表：</p>
                        <p m="t-0 b-4">{{ formatExpandMission(scope.row.affair) }}</p>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="staffId" label="工号" width="120" align="center" />
            <el-table-column prop="name" label="姓名" width="120" align="center" />
            <el-table-column prop="sex" sortable label="性别" width="120" align="center" />
            <el-table-column prop="age" sortable label="年龄" width="120" align="center" />
            <el-table-column prop="contact" label="联系方式" width="120" align="center" />
            <el-table-column prop="address" label="家庭住址" width="180" align="center" show-overflow-tooltip="true" />

            <el-table-column prop="work" sortable label="职位" width="120" align="center">
                <template #default="scope">
                    <el-tag :type="judgeTag(scope.row.work)">{{ scope.row.work }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="affair" label="任务列表" width="180" align="center" show-overflow-tooltip="true">
                <template #default="scope">
                    <span>{{ formatMission(scope.row.affair) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="joinDate" sortable label="入职日期" width="140" align="center">
                <template #default="scope">
                    <span>{{ formatDate(scope.row.joinDate) }}</span>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="130" align="center">
                <template #default="scope">
                    <el-button link type="primary" @click.prevent="openForm(scope)">修改</el-button>
                    <el-popconfirm title="确定要删除此员工?" @confirm="deleteRow(scope)" v-if="work === '站长'">
                        <template #reference>
                            <el-button link type="danger">解雇</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="isShow" :title="showTitile" width="600px" @close="closeForm"
            class="flex flex-col justify-center items-center">
            <el-form :model="Staff" label-width="120px" :rules="staff_rules" ref="staffForm_Ref">
                <el-form-item prop="name" label="员工姓名：">
                    <el-input v-model="Staff.name"></el-input>
                </el-form-item>

                <el-form-item prop="age" label="员工年龄：">
                    <el-input v-model="Staff.age"></el-input>
                </el-form-item>

                <el-form-item prop="sex" label="员工性别：">
                    <el-radio-group v-model="Staff.sex">
                        <el-radio label="F" size="large">男</el-radio>
                        <el-radio label="M" size="large">女</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item prop="work" label="职位：">
                    <el-select v-model="Staff.work" placeholder="请选择职位">
                        <el-option v-for="(item, index) in workType" :key="index" :label="item"
                            :value="item"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item prop="address" label="家庭住址：">
                    <el-input v-model="Staff.address"></el-input>
                </el-form-item>

                <el-form-item prop="contact" label="联系方式：">
                    <el-input v-model="Staff.contact"></el-input>
                </el-form-item>

            </el-form>

            <template #footer>
                <el-button @click="closeForm">取消</el-button>
                <el-button type="primary" @click="submitStaff">确认</el-button>
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

const staffList = ref([])

let staffForm_Ref = ref()

let currentIndex = null

let work = ref()

onMounted(() => {
    init()
})

const init = async () => {
    const hub = store.getAdminInfo()
    work.value = hub.work
    const [e, r] = await api.getStaffList(hub.hub_id)
    staffList.value = [...r.data]
    Staff.hub_id = hub.hub_id
}

let isShow = ref(false)

const searchList = ref([])

let isShowSearch = ref(false)

const Staff = reactive({
    name: '',
    age: null,
    sex: 'F',
    contact: '',
    address: '',
    work: '',
    affair: ''
})

const searchFor = ref()

const searchStaff = async () => {
    if (searchFor.value == null) {
        return
    }
    searchList.value = []
    const word = parseInt(searchFor.value)
    if (word) {
        const [e, r] = await api.getStaff(searchFor.value, null)
        if (r.code == 200) {
            searchList.value.push(r.data)
            isShowSearch.value = true
        }
        else {
            ElMessage.error(r.msg)
        }
    }
    else {
        const [e, r] = await api.getStaff(null, searchFor.value)
        if (r.code == 200) {
            searchList.value = [...r.data]
            isShowSearch.value = true
        }
        else {
            ElMessage.error(r.msg)
        }
    }
}

const closeForm = () => {
    Staff.name = ''
    Staff.age = null
    Staff.sex = 'F'
    Staff.contact = ''
    Staff.address = ''
    Staff.work = ''
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

    address: [
        { required: true, message: '地址不能为空!', trigger: 'blur' }
    ],

    work: [
        { required: true, message: '职责不能为空!', trigger: 'blur' }
    ],
})

let showTitile = ref('')

const openForm = (scope) => {
    if (scope == null) {
        showTitile.value = '新增员工'
    }
    else {
        showTitile.value = '修改员工信息'
        currentIndex = scope.$index
        console.log('currentIndex', currentIndex)
        console.log(scope.row)
        Object.assign(Staff, scope.row)
    }
    isShow.value = true
}

const updateForm = async () => {
    const [e, r] = await api.updateStaffInfo(Staff)
    if (r.code == 200) {
        ElMessage({
            message: '修改成功！',
            type: 'success',
        })
        Object.assign(staffList.value[currentIndex], Staff)
        closeForm()
    }

    else {
        ElMessage.error('修改失败，请检查网络连接')
    }
}

const toValidateUpdate = () => {
    staffForm_Ref.value.validate((vaild) => {
        if (vaild) {
            updateForm()
        }
        else {
            return false
        }
    })
}

const addStaff = async () => {
    const [e, r] = await api.addStaff(Staff)
    if (r.code == 200) {
        // staffList.value.push(JSON.parse(JSON.stringify(Staff)))
        staffList.value.push(r.data)
        ElMessage({
            message: '新增员工成功！',
            type: 'success',
        })
        closeForm()
    }

    else {
        ElMessage.error('修改失败，请检查网络连接')
    }
}

const toValidate = () => {
    staffForm_Ref.value.validate((vaild) => {
        if (vaild) {
            addStaff()
        }
        else {
            return false
        }
    })
}

const deleteRow = async (scope) => {
    const [e, r] = await api.deleteStaff(scope.row.staffId)
    if (r.code == 200) {
        staffList.value.splice(scope.$index, 1)
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

const submitStaff = () => {
    if (showTitile.value == '新增员工') {
        toValidate()
    }
    else {
        toValidateUpdate()
    }
}

function formatDate(dateString) {
    let date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}年${month}月${day}日`;
}

watchEffect(() => {
    if (searchFor.value == '') {
        isShowSearch.value = false;
    }
})

const workType = ["站长", "管理员", "客服", "入库员", "配送员"]

const formatMission = (item) => {
    if (item) {
        if (item[0] == ',') return item.substring(1)
        else return item
    }
    else {
        return '-'
    }
}

const formatExpandMission = (item) => {
    console.log("🚀 ~ formatExpandMission ~ item:", item)
    if (item) {
        if (item[0] == ',') item.substring(1)
        let arr = ''
        item.split(',').forEach(str => {
            if(str != '')
            arr += '● ' + str + '\n'
        })
        return arr
    }
    else {
        return '-'
    }
}

const judgeTag = (item) => {
    switch(item){
        case '站长' :
            return 'danger'
        case '管理员' :
            return 'primary'
        case '客服' :
            return 'success'
        case '入库员' :
            return 'info'
        case '配送员' :
            return 'warning'
    }
}

</script>

<style lang="scss" scoped>
.el-input {
    width: 200px;
}
</style>