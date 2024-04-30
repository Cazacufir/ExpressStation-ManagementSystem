<template>
    <div class="header w-full h-full text-center flex items-center justify-between">
        <span class="title text-9xl text-light-50">驿 站 管 理 系 统</span>

        <el-dropdown class="text-light-50">
            <span>
                欢迎您，{{ name }}
                <el-icon>
                    <ArrowDown />
                </el-icon>
            </span>

            <template #dropdown>
                <el-dropdown-menu class="logout">
                    <el-dropdown-item icon="Document" @click="router.push('/userInfo')">个人信息</el-dropdown-item>
                    <el-dropdown-item icon="Key" @click="router.push('/password')">修改密码</el-dropdown-item>
                    <el-dropdown-item icon="CircleClose" divided style="color: red;"
                        @click="show = true">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>

        <el-dialog v-model="show" title="确认退出?" @close="show = false" width="400">
            <div class="flex flex-col flex-justify-center items-center gap-10">
                <el-icon size="30" color="#f56c6c"><WarningFilled /></el-icon>
                <el-text>确认后将返回登录页</el-text>
            </div>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="show = false">取消</el-button>
                    <el-button type="primary" @click="logout">
                        确定
                    </el-button>
                </div>
            </template>

        </el-dialog>

    </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js"
import { utils } from "@/utils/session.js";
import { WarningFilled } from '@element-plus/icons-vue'

const store = adminStore()
const router = useRouter();

const name = ref('')

let show = ref(false)

onMounted(() => {
    init()
})

const init = async () => {
    const staffId = store.getAdminInfo().staff_id
    console.log("🚀 ~ init ~ staffId:", store.getAdminInfo().staffId)
    const [e, r] = await api.getStaff(staffId)
    store.setAdminInfo(r.data)
    name.value = r.data.name
}

const logout = () => {
    utils.clearSession()
    utils.removeLocal('saved')
    utils.removeLocal('AutoLogin')
    router.replace("/login")
}
</script>

<style scoped lang="scss">
.header {
    background: #545c64;
}

.el-dropdown {
    color: #fff;
}

:deep(.el-tooltip__trigger:focus-visible) {
    outline: unset;
}
</style>