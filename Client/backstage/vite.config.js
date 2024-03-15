import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from "path";
import Unocss from "unocss/vite";
import { presetUno, presetAttributify, presetIcons } from "unocss";

function _resolve(dir) {
  return path.resolve(__dirname, dir);
}
// https://vitejs.dev/config/
export default defineConfig({
  resolve:{
    alias:{
      "@": _resolve("src"),
    }
  },
  plugins: [
    vue(),
    Unocss({
      // 使用Unocss
      presets: [
        presetUno(),
        presetAttributify(),
        presetIcons(),
      ],
    }),
  ],
  server:{
    proxy: {
      // 接口地址代理
      "/api": {
        target: "http://localhost:59831",
        ws: true,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  }
})
