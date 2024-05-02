import { createRouter, createWebHistory } from "vue-router";
import { Histogram } from "@element-plus/icons-vue";
import component from "element-plus/es/components/tree-select/src/tree-select-option.mjs";

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
        path: "/home",
        name: "Home",
        component: async () => await import("@/views/home/home.vue"),
        meta: {
          title: "统计一览",
          icon: "Histogram",
        },
      },
      {
        path: "/main",
        name: "Main",
        component: async () => await import("@/views/main/main.vue"),
        meta: {
          title: "驿站主体信息",
          icon: "HomeFilled",
        },
      },
      {
        path: "/cooperate",
        name: "Cooperate",
        component: async () => await import("@/views/cooperate/cooperate.vue"),
        meta: {
          title: "合作公司",
          icon: "OfficeBuilding",
        },
      },
      {
        path: "/delivery",
        name: "Deliver",
        component: async () => await import("@/views/delivery/delivery.vue"),
        meta: {
          title: "快递员管理",
          icon: "Promotion",
        },
      },
      {
        path: "/staff",
        name: "Staff",
        component: async () => await import("@/views/staff/staff.vue"),
        meta: {
          title: "员工管理",
          icon: "Avatar",
        },
      },
      {
        path: "/parcel",
        name: "Parcel",
        component: async () => await import("@/views/parcel/parcel.vue"),
        children: [
          {
            path: "send",
            name: "Send",
            component: async () =>
              await import("@/views/parcel/views/send.vue"),
            meta: {
              title: "快递出库",
              icon: "Van",
            },
          },
          {
            path: "receive",
            name: "Receive",
            component: async () =>
              await import("@/views/parcel/views/receive.vue"),
            meta: {
              title: "快递入库",
              icon: "SoldOut",
            },
          },
          {
            path: "all",
            name: "All",
            component: async () => await import("@/views/parcel/views/get.vue"),
            meta: {
              title: "取件管理",
              icon: "HelpFilled",
            },
          },
          {
            path: "carrier",
            name: "Carrier",
            component: async () =>
              await import("@/views/parcel/views/carrier.vue"),
            meta: {
              title: "货架一览",
              icon: "MessageBox",
            },
          },
        ],
        meta: {
          title: "快递管理",
          icon: "Box",
        },
      },
      {
        path: "/appointment",
        name: "Appointment",
        component: async () => await import("@/views/appointment/appointment.vue"),
        children:[
          {
            path:"reserve",
            name:"Reserve",
            component:async () => await import("@/views/appointment/reserve.vue"),
            meta:{
              title:'预约取件',
              icon:'Timer'
            }
          },
          {
            path:"delay",
            name:"Delay",
            component:async () => await import("@/views/appointment/delay.vue"),
            meta:{
              title:'延迟取件',
              icon:'Stopwatch'
            }
          },
        ],
        meta: {
          title: "预约管理",
          icon: "Clock",
        },
      },
      {
        path: "/userInfo",
        name: "UserInfo",
        component: async () => await import("@/views/userInfo/userInfo.vue"),
        meta: {
          title: "个人信息"
        }
      },
      {
        path: "/password",
        name: "Password",
        component: async () => await import("@/views/password/password.vue"),
        meta: {
          title: "修改密码"
        }
      },
      {
        path: "/:carrierId",
        name: "CarrierId",
        component: async () => await import("@/views/carrierDetail/[carrierId].vue"),
        meta: {
          title: "货架详情"
        }
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
