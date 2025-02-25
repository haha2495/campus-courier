import {createRouter, createWebHistory} from "vue-router";

// 布局组件
import SideMenuLayout from "../layout/SideMenuLayout.vue";

// 登录页
import Login from "../views/login/index.vue";

// 首页组件
import AdminHome from "../views/admin/home/index.vue";

const routes = [
    {
        path: "/login",
        name: "Login",
        component: Login,
    },
    // 管理员路由
    {
        path: "/admin",
        component: SideMenuLayout,
        children: [
            {
                path: "home",
                name: "AdminHome",
                component: AdminHome,
            },
            {
                path: "admins",
                name: "AdminManage",
                component: () => import("../views/admin/admins/index.vue"),
            },
            {
                path: "students",
                name: "StudentManage",
                component: () => import("../views/admin/students/index.vue"),
            },
            {
                path: "task-types",
                name: "TaskTypeManage",
                component: () => import("../views/admin/taskTypes/index.vue"),
                meta: {
                    title: "任务类型管理",
                    requiresAuth: true,
                },
            },
            {
                path: "profile",
                name: "AdminProfile",
                component: () => import("../views/admin/profile/index.vue"),
                meta: {
                    title: "个人信息",
                    requiresAuth: true,
                },
            },
        ],
    },
    // 根路径重定向到登录页
    {
        path: "/",
        redirect: "/login",
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem("token");

    // 如果访问的不是登录页，且没有token，则重定向到登录页
    if (to.path !== "/login" && !token) {
        next("/login");
        return;
    }

    // 如果已登录还访问登录页，则重定向到管理员首页
    if (to.path === "/login" && token) {
        next("/admin/home");
        return;
    }

    next();
});

export default router;
