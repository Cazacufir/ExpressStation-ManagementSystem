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
                    <el-dropdown-item icon="CircleClose" divided style="color: red;">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>

    </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { api } from "@/api"
import { adminStore } from "@/stores/admin.js"

const store = adminStore()
const router = useRouter();

const name = ref('')

onMounted(()=>{
    init()
})

const init = async () => {
    const [e,r] = await api.getStaff(600000)
    store.setAdminInfo(r.data)
    name.value = r.data.name
}
</script>

<style scoped lang="scss">
.header {
    background: #545c64;
}
.el-dropdown{
    color: #fff;
}
:deep(.el-tooltip__trigger:focus-visible) {
      outline: unset;
}
</style>