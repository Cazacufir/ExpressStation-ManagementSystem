<template>
    <div class="login-container h-full w-full flex justify-center items-center" >
        <article class="login-form min-h-60% w-30% bg-white rounded-20 flex flex-col items-center overflow-hidden" >

            <div class=" <sm:shadow-none p-5 h-60% flex flex-col items-center">
                <div>
                    <button v-for="(_, tab) in tabs" :key="tab" :class="['tab-button', { active: currentTab === tab }]"
                        @click="currentTab = tab">
                        <h2 class="font-semibold">
                            {{ tab === "Login" ? "登录" : "注册" }}
                        </h2>
                    </button>
                </div>

                <Transition name="slide-fade" mode="out-in">
                    <component :is="tabs[currentTab]" class="tab"></component>
                </Transition>
            </div>

        </article>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Login from './login.vue'
import Register from './register.vue'

const tabs = { Login, Register };
const currentTab = ref('Login')
</script>

<style scoped lang="scss">
.login-container {
    background: var(--primary-color);
}

button {
    background: none;
    position: relative;
    cursor: pointer;
    border: none;
}

button::before {
    content: "";
    position: absolute;
    left: 100%;
    bottom: 0;

    height: 4px;
    width: 0;
    border-radius: 4px;
    background-color: var(--primary-color);
    transition: all 200ms;
}

button.active::before {
    left: 0;
    width: 100%;
}

button.active~button::before {
    left: 0;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
    transition: all 300ms cubic-bezier(0.4, 0.4, 0.25, 1.35);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
    transform: translate3d(10%, 0, 0);
    opacity: 0;
}
</style>