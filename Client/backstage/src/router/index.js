import { createRouter, createWebHistory } from "vue-router";
import { Histogram } from "@element-plus/icons-vue"

const routes = [
  {
    path: "/login",
    name: "Login",
    component: async () => await import("@/views/login/index.vue"),
  },
  {
    path: "/",
    name: "Index",
    component: async () => await import("@/layout/layout.vue"),
    redirect: "/login",
    children: [
      {
        path:"/home",
        name: "Home",
        component: async () => await import("@/views/home/home.vue"),
        meta: {
          title: "统计一览",
          icon: 'Histogram',
        }
      },
      {
        path: "/main",
        name: "Main",
        component: async () => await import("@/views/main/main.vue"),
        meta: {
          title: "驿站主体信息",
          icon: 'HomeFilled'
        },
      },
      {
        path: "/cooperate",
        name: "Cooperate",
        component: async () => await import("@/views/cooperate/cooperate.vue"),
        meta: {
          title: "合作公司",
          icon: 'OfficeBuilding'
        },
      },
      {
        path: "/staff",
        name: "Staff",
        component: async () => await import("@/views/staff/staff.vue"),
        meta: {
          title: "员工管理",
          icon: 'Avatar'
        },
      },
      {
        path: "/parcel",
        name: "Parcel",
        component: async () => await import("@/views/parcel/parcel.vue"),
        children:[
          {
            path: "receive",
            name: "Receive",
            component: async () => await import("@/views/parcel/views/receive.vue"),
            meta:{
              title:'快递入库',
              icon: 'SoldOut'
            }
          },
          {
            path: "out",
            name: "Out",
            component: async () => await import("@/views/parcel/views/out.vue"),
            meta:{
              title:'快递出库',
              icon: 'Sell'
            }
          },
          {
            path: "send",
            name: "Send",
            component: async () => await import("@/views/parcel/views/send.vue"),
            meta:{
              title:'寄送快递',
              icon: 'Van'
            }
          },
          {
            path:'search',
            name:'Search',
            component: async () => await import("@/views/parcel/views/search.vue"),
            meta:{
              title:'快递查询',
              icon: 'Search'
            }
          }
        ],
        meta: {
          title: "快递管理",
          icon: 'Box'
        },
      },
      {
        path: "/delivery",
        name: "Deliver",
        component: async () => await import("@/views/delivery/delivery.vue"),
        meta: {
          title: "配送员管理",
          icon: 'Promotion'
        },
      },
      {
        path: "/appointment",
        name: "Appointment",
        component: async () =>
          await import("@/views/appointment/appointment.vue"),
        meta: {
          title: "预约管理",
          icon: 'Clock'
        },
      },
      {
        path: "/userInfo",
        name: "UserInfo",
        component: async () => await import("@/views/userInfo/userInfo.vue")
      }
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;