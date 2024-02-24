import { createRouter, createWebHistory } from "vue-router";

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
        path: "/main",
        name: "Main",
        component: async () => await import("@/views/main/main.vue"),
        meta: {
          title: "驿站主体信息"
        },
      },
      {
        path: "/cooperate",
        name: "Cooperate",
        component: async () => await import("@/views/cooperate/cooperate.vue"),
        meta: {
          title: "合作公司"
        },
      },
      {
        path: "/staff",
        name: "Staff",
        component: async () => await import("@/views/staff/staff.vue"),
        meta: {
          title: "员工管理"
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
              title:'快递入库'
            }
          },
          {
            path: "out",
            name: "Out",
            component: async () => await import("@/views/parcel/views/out.vue"),
            meta:{
              title:'快递出库'
            }
          },
          {
            path: "send",
            name: "Send",
            component: async () => await import("@/views/parcel/views/send.vue"),
            meta:{
              title:'寄送快递'
            }
          },
          {
            path:'search',
            name:'Search',
            component: async () => await import("@/views/parcel/views/search.vue"),
            meta:{
              title:'快递查询'
            }
          }
        ],
        meta: {
          title: "快递管理"
        },
      },
      {
        path: "/delivery",
        name: "Deliver",
        component: async () => await import("@/views/delivery/delivery.vue"),
        meta: {
          title: "配送员管理"
        },
      },
      {
        path: "/appointment",
        name: "Appointment",
        component: async () =>
          await import("@/views/appointment/appointment.vue"),
        meta: {
          title: "预约管理"
        },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
