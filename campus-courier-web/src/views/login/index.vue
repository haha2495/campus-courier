<template>
  <div class="login-container">
    <!-- 校园背景元素 -->
    <div class="campus-elements">
      <div class="campus-building left-building"></div>
      <div class="campus-building right-building"></div>
      <div class="campus-road"></div>
      <div class="courier courier-1"></div>
      <div class="courier courier-2"></div>
      <div class="courier courier-3"></div>
      <div class="package package-1"></div>
      <div class="package package-2"></div>
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
    </div>

    <!-- 主要内容区 - 改为上下结构 -->
    <div class="main-content">
      <!-- 顶部品牌区域 -->
      <div class="brand-section">
        <div class="logo-container">
          <div class="logo-wrapper">
            <el-icon class="logo-icon"><Van /></el-icon>
          </div>
          <h1 class="system-title">校园跑腿系统</h1>
        </div>
        <p class="system-subtitle">高效便捷的校园跑腿服务管理平台</p>
      </div>

      <!-- 底部卡片区域 -->
      <div class="cards-section">
        <!-- 左侧特点卡片 -->
        <div class="features-card">
          <h2 class="card-title">系统特点</h2>
          <div class="features-list">
            <div class="feature-item" v-for="(feature, index) in features" :key="index">
              <div class="feature-icon-wrapper">
                <el-icon class="feature-icon">
                  <component :is="feature.icon" />
                </el-icon>
              </div>
              <div class="feature-content">
                <h3>{{ feature.title }}</h3>
                <p>{{ feature.desc }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧登录卡片 -->
        <div class="login-card">
          <div class="card-header">
            <h2 class="card-title">管理员登录</h2>
          </div>

          <el-form
            :model="loginForm"
            :rules="rules"
            ref="loginFormRef"
            class="login-form"
            @validate="handleValidate"
          >
            <el-form-item prop="username">
              <div class="input-label">
                <el-icon><User /></el-icon>
                <span>账号</span>
                <span class="validation-status" v-if="fieldStatus.username">{{ fieldStatus.username }}</span>
              </div>
              <el-input
                v-model="loginForm.username"
                placeholder="请输入管理员账号"
                :class="{ 'is-valid': isFieldValid('username') }"
                @focus="handleFocus('username')"
                @blur="handleBlur('username')"
              />
            </el-form-item>

            <el-form-item prop="password">
              <div class="input-label">
                <el-icon><Lock /></el-icon>
                <span>密码</span>
                <span class="validation-status" v-if="fieldStatus.password">{{ fieldStatus.password }}</span>
              </div>
              <el-input
                v-model="loginForm.password"
                type="password"
                show-password
                placeholder="请输入密码"
                :class="{ 'is-valid': isFieldValid('password') }"
                @focus="handleFocus('password')"
                @blur="handleBlur('password')"
              />
            </el-form-item>

            <el-form-item prop="captcha">
              <!-- 第一行：验证码标题 -->
              <div class="captcha-label">
                <el-icon><Key /></el-icon>
                <span>验证码</span>
                <span class="validation-status" v-if="fieldStatus.captcha">{{ fieldStatus.captcha }}</span>
              </div>
              
              <!-- 第二行：输入框和验证码问题 -->
              <div class="captcha-inputs">
                <el-input
                  v-model="loginForm.captcha"
                  placeholder="请输入验证码"
                  :class="{ 'is-valid': isFieldValid('captcha') }"
                  @focus="handleFocus('captcha')"
                  @blur="handleBlur('captcha')"
                />
                <div class="captcha-box" @click="refreshCaptcha">
                  {{ captcha.question }}
                  <el-icon class="refresh-icon"><RefreshRight /></el-icon>
                </div>
              </div>
            </el-form-item>

            <div class="login-actions">
              <el-button
                type="primary"
                class="login-button"
                :loading="loading"
                :disabled="!isFormValid"
                @click="handleLogin"
              >
                <el-icon><Key /></el-icon>
                <span>登录系统</span>
              </el-button>
            </div>

            <div class="security-tips">
              <div class="tip-item">
                <el-icon><Warning /></el-icon>
                <span>请勿在公共场所保存账号密码</span>
              </div>
              <div class="tip-item">
                <el-icon><Clock /></el-icon>
                <span>登录后请及时处理跑腿任务</span>
              </div>
            </div>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 底部版权信息 -->
    <div class="footer">
      <p>© 2023 校园跑腿系统 - 为校园生活提供便捷服务</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import {
  Lock,
  RefreshRight,
  Warning,
  User,
  Key,
  Van,
  Location,
  ShoppingCart,
  Guide,
  InfoFilled,
  Clock,
} from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { adminLogin } from "@/api/admin";

const loginFormRef = ref(null);
const loading = ref(false);

// 表单字段状态
const fieldStatus = ref({
  username: "",
  password: "",
  captcha: "",
});

// 登录表单数据
const loginForm = ref({
  username: "admin",
  password: "123456",
  captcha: "",
});

// 系统特点
const features = [
  {
    icon: "Van",
    title: "任务调度",
    desc: "智能分配跑腿任务，提高配送效率",
  },
  {
    icon: "Location",
    title: "路线优化",
    desc: "智能规划跑腿路线，减少等待时间",
  },
  {
    icon: "ShoppingCart",
    title: "订单管理",
    desc: "多维度统计分析，优化运营策略",
  },
  {
    icon: "Guide",
    title: "跑腿员管理",
    desc: "全面管理跑腿员，提升服务质量",
  },
];

const router = useRouter();

// 处理输入框焦点
const handleFocus = (field) => {
  fieldStatus.value[field] = "请输入...";
};

// 处理输入框失焦
const handleBlur = (field) => {
  loginFormRef.value?.validateField(field, (valid) => {
    if (valid) {
      fieldStatus.value[field] = "✓ 验证通过";
    }
  });
};

// 处理表单验证
const handleValidate = (prop, valid) => {
  if (valid) {
    fieldStatus.value[prop] = "✓ 验证通过";
  } else {
    fieldStatus.value[prop] = "";
  }
};

// 检查字段是否有效
const isFieldValid = (field) => {
  return fieldStatus.value[field] === "✓ 验证通过";
};

// 检查表单是否全部有效
const isFormValid = computed(() => {
  return (
    isFieldValid("username") &&
    isFieldValid("password") &&
    isFieldValid("captcha")
  );
});

const captcha = ref({
  question: "",
  answer: "",
});

// 生成数学验证码
const generateMathCaptcha = () => {
  const num1 = Math.floor(Math.random() * 10);
  const num2 = Math.floor(Math.random() * 10);
  const operators = ["+", "-", "×"];
  const operator = operators[Math.floor(Math.random() * operators.length)];

  let answer;
  switch (operator) {
    case "+":
      answer = num1 + num2;
      break;
    case "-":
      answer = num1 - num2;
      break;
    case "×":
      answer = num1 * num2;
      break;
  }

  captcha.value = {
    question: `${num1} ${operator} ${num2} = ?`,
    answer: answer.toString(),
  };
};

// 刷新验证码
const refreshCaptcha = () => {
  generateMathCaptcha();
  loginForm.value.captcha = "";
};

// 验证码校验规则
const validateCaptcha = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入验证码"));
  } else if (value !== captcha.value.answer) {
    callback(new Error("验证码错误"));
  } else {
    callback();
  }
};

// 表单验证规则
const rules = {
  username: [
    {
      required: true,
      message: "请输入账号",
      trigger: "blur",
    },
    {
      min: 3,
      message: "账号长度不能小于3位",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能小于6位", trigger: "blur" },
  ],
  captcha: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { validator: validateCaptcha, trigger: "blur" },
  ],
};

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return;

  try {
    await loginFormRef.value.validate();
    loading.value = true;

    let res;
    let userInfo = {
      username: loginForm.value.username,
      role: "admin", // 直接设置为管理员
    };

    // 管理员登录
    res = await adminLogin({
      username: loginForm.value.username,
      password: loginForm.value.password,
    });

    // 存储 token 到 localStorage
    localStorage.setItem("token", res);

    // 存储用户信息到 localStorage
    localStorage.setItem("userInfo", JSON.stringify(userInfo));

    ElMessage.success("登录成功");

    // 添加过渡动画
    document.body.classList.add("page-transitioning");
    setTimeout(() => {
      // 跳转到管理员首页
      router.push("/admin/home");
      setTimeout(() => {
        document.body.classList.remove("page-transitioning");
      }, 1000);
    }, 600);
  } finally {
    loading.value = false;
  }
};

// 初始化验证码
generateMathCaptcha();
</script>

<style scoped lang="scss">
.login-container {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(180deg, #e6f7ff 0%, #fff 100%);
  position: relative;
  overflow: hidden;
}

/* 校园背景元素 */
.campus-elements {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;

  .campus-building {
    position: absolute;
    background-repeat: no-repeat;
    background-size: contain;
    z-index: 1;

    &.left-building {
      width: 180px;
      height: 300px;
      left: 5%;
      bottom: 10%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 160' fill='%23b3e0ff'%3E%3Crect x='20' y='40' width='60' height='120' /%3E%3Cpolygon points='20,40 50,10 80,40' /%3E%3Crect x='35' y='60' width='10' height='15' fill='%23ffffff' /%3E%3Crect x='55' y='60' width='10' height='15' fill='%23ffffff' /%3E%3Crect x='35' y='90' width='10' height='15' fill='%23ffffff' /%3E%3Crect x='55' y='90' width='10' height='15' fill='%23ffffff' /%3E%3Crect x='35' y='120' width='10' height='15' fill='%23ffffff' /%3E%3Crect x='55' y='120' width='10' height='15' fill='%23ffffff' /%3E%3Crect x='45' y='140' width='10' height='20' fill='%23ffffff' /%3E%3C/svg%3E");
      opacity: 0.6;
      animation: sway 20s ease-in-out infinite;
    }

    &.right-building {
      width: 220px;
      height: 380px;
      right: 8%;
      bottom: 5%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 120 200' fill='%23b3e0ff'%3E%3Crect x='20' y='20' width='80' height='180' /%3E%3Crect x='30' y='40' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='55' y='40' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='75' y='40' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='30' y='70' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='55' y='70' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='75' y='70' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='30' y='100' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='55' y='100' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='75' y='100' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='30' y='130' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='55' y='130' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='75' y='130' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='30' y='160' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='55' y='160' width='15' height='20' fill='%23ffffff' /%3E%3Crect x='75' y='160' width='15' height='20' fill='%23ffffff' /%3E%3C/svg%3E");
      opacity: 0.7;
      animation: sway 15s ease-in-out infinite reverse;
    }
  }

  .campus-road {
    position: absolute;
    width: 100%;
    height: 60px;
    bottom: 0;
    background-color: #e0e0e0;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='100' height='60' viewBox='0 0 100 60'%3E%3Crect width='100' height='60' fill='%23e0e0e0'/%3E%3Cline x1='0' y1='30' x2='100' y2='30' stroke='%23ffffff' stroke-width='4' stroke-dasharray='10,10'/%3E%3C/svg%3E");
    background-repeat: repeat-x;
    z-index: 2;
  }

  .courier {
    position: absolute;
    width: 40px;
    height: 40px;
    background-repeat: no-repeat;
    background-size: contain;
    z-index: 3;

    &.courier-1 {
      bottom: 60px;
      left: 10%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%234fc3f7'%3E%3Cpath d='M4,16c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S4,14.9,4,16z M6,13c2.8,0,5,2.2,5,5s-2.2,5-5,5s-5-2.2-5-5S3.2,13,6,13z M6,15 c-1.7,0-3,1.3-3,3s1.3,3,3,3s3-1.3,3-3S7.7,15,6,15z M10,5c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S10,3.9,10,5z M12,2 c1.7,0,3,1.3,3,3s-1.3,3-3,3S9,6.7,9,5S10.3,2,12,2z M12,4c-0.6,0-1,0.4-1,1s0.4,1,1,1s1-0.4,1-1S12.6,4,12,4z M19,16 c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S19,14.9,19,16z M16,13c0-0.6,0.4-1,1-1h1c1.1,0,2,0.9,2,2v1h-1v-1c0-0.6-0.4-1-1-1h-1 c-1.1,0-2,0.9-2,2v9h-2v-6H9.9c-0.5,0-0.9-0.2-1.2-0.6l-2-2.4C6.4,14.8,6,14.5,6,14c0-0.3,0.1-0.5,0.4-0.8L9,10.5C9.5,10.2,10,10,10.5,10 c0.8,0,1.5,0.4,1.9,1l2.5,3l3.1-0.1l0,0c0.5,0,1,0.4,1,1S18.5,16,18,16l-4,0.1L12,13v-1h-1v1c0,0.3,0.1,0.6,0.3,0.8l2.5,3.4 c0.3,0.4,0.5,0.8,0.5,1.2L14,23h2V13z'/%3E%3C/svg%3E");
      animation: moveBicycle 15s linear infinite;
    }

    &.courier-2 {
      bottom: 60px;
      left: 40%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ff7043'%3E%3Cpath d='M4,16c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S4,14.9,4,16z M6,13c2.8,0,5,2.2,5,5s-2.2,5-5,5s-5-2.2-5-5S3.2,13,6,13z M6,15 c-1.7,0-3,1.3-3,3s1.3,3,3,3s3-1.3,3-3S7.7,15,6,15z M10,5c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S10,3.9,10,5z M12,2 c1.7,0,3,1.3,3,3s-1.3,3-3,3S9,6.7,9,5S10.3,2,12,2z M12,4c-0.6,0-1,0.4-1,1s0.4,1,1,1s1-0.4,1-1S12.6,4,12,4z M19,16 c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S19,14.9,19,16z M16,13c0-0.6,0.4-1,1-1h1c1.1,0,2,0.9,2,2v1h-1v-1c0-0.6-0.4-1-1-1h-1 c-1.1,0-2,0.9-2,2v9h-2v-6H9.9c-0.5,0-0.9-0.2-1.2-0.6l-2-2.4C6.4,14.8,6,14.5,6,14c0-0.3,0.1-0.5,0.4-0.8L9,10.5C9.5,10.2,10,10,10.5,10 c0.8,0,1.5,0.4,1.9,1l2.5,3l3.1-0.1l0,0c0.5,0,1,0.4,1,1S18.5,16,18,16l-4,0.1L12,13v-1h-1v1c0,0.3,0.1,0.6,0.3,0.8l2.5,3.4 c0.3,0.4,0.5,0.8,0.5,1.2L14,23h2V13z'/%3E%3C/svg%3E");
      animation: moveBicycle 10s linear infinite;
      animation-delay: 2s;
    }

    &.courier-3 {
      bottom: 60px;
      left: 70%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%239ccc65'%3E%3Cpath d='M4,16c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S4,14.9,4,16z M6,13c2.8,0,5,2.2,5,5s-2.2,5-5,5s-5-2.2-5-5S3.2,13,6,13z M6,15 c-1.7,0-3,1.3-3,3s1.3,3,3,3s3-1.3,3-3S7.7,15,6,15z M10,5c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S10,3.9,10,5z M12,2 c1.7,0,3,1.3,3,3s-1.3,3-3,3S9,6.7,9,5S10.3,2,12,2z M12,4c-0.6,0-1,0.4-1,1s0.4,1,1,1s1-0.4,1-1S12.6,4,12,4z M19,16 c0,1.1,0.9,2,2,2s2-0.9,2-2s-0.9-2-2-2S19,14.9,19,16z M16,13c0-0.6,0.4-1,1-1h1c1.1,0,2,0.9,2,2v1h-1v-1c0-0.6-0.4-1-1-1h-1 c-1.1,0-2,0.9-2,2v9h-2v-6H9.9c-0.5,0-0.9-0.2-1.2-0.6l-2-2.4C6.4,14.8,6,14.5,6,14c0-0.3,0.1-0.5,0.4-0.8L9,10.5C9.5,10.2,10,10,10.5,10 c0.8,0,1.5,0.4,1.9,1l2.5,3l3.1-0.1l0,0c0.5,0,1,0.4,1,1S18.5,16,18,16l-4,0.1L12,13v-1h-1v1c0,0.3,0.1,0.6,0.3,0.8l2.5,3.4 c0.3,0.4,0.5,0.8,0.5,1.2L14,23h2V13z'/%3E%3C/svg%3E");
      animation: moveBicycle 12s linear infinite;
      animation-delay: 5s;
    }
  }

  .package {
    position: absolute;
    width: 30px;
    height: 30px;
    background-repeat: no-repeat;
    background-size: contain;
    z-index: 3;

    &.package-1 {
      top: 30%;
      left: 25%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ffa726'%3E%3Cpath d='M19,3H5C3.9,3,3,3.9,3,5v14c0,1.1,0.9,2,2,2h14c1.1,0,2-0.9,2-2V5C21,3.9,20.1,3,19,3z M19,19H5V5h14V19z M13.96,12.29 l-2.75,3.54l-1.96-2.36L6.5,17h11L13.96,12.29z'/%3E%3C/svg%3E");
      animation: float 3s ease-in-out infinite, dropPackage 15s ease-in-out infinite;
    }

    &.package-2 {
      top: 20%;
      right: 30%;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ef5350'%3E%3Cpath d='M19,3H5C3.9,3,3,3.9,3,5v14c0,1.1,0.9,2,2,2h14c1.1,0,2-0.9,2-2V5C21,3.9,20.1,3,19,3z M19,19H5V5h14V19z M13.96,12.29 l-2.75,3.54l-1.96-2.36L6.5,17h11L13.96,12.29z'/%3E%3C/svg%3E");
      animation: float 4s ease-in-out infinite, dropPackage 18s ease-in-out infinite;
      animation-delay: 2s;
    }
  }

  .cloud {
    position: absolute;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 50' fill='%23ffffff'%3E%3Cpath d='M85,35.5C85,38,83,40,80.5,40h-58C17,40,12.5,35.5,12.5,30c0-5.5,4.5-10,10-10c0.5,0,1,0,1.5,0.1C26,12,33,5,42,5c9,0,16,7,18,16c1-0.6,2.2-1,3.5-1c4.1,0,7.5,3.4,7.5,7.5c0,1.2-0.3,2.4-0.8,3.4C75,33,80,31,85,35.5z'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-size: contain;
    z-index: 0;

    &.cloud-1 {
      width: 120px;
      height: 60px;
      top: 15%;
      left: 10%;
      opacity: 0.8;
      animation: moveCloud 60s linear infinite;
    }

    &.cloud-2 {
      width: 180px;
      height: 90px;
      top: 25%;
      left: 60%;
      opacity: 0.7;
      animation: moveCloud 80s linear infinite;
      animation-delay: 10s;
    }

    &.cloud-3 {
      width: 150px;
      height: 75px;
      top: 10%;
      left: 30%;
      opacity: 0.6;
      animation: moveCloud 70s linear infinite;
      animation-delay: 5s;
    }
  }
}

@keyframes moveBicycle {
  0% { transform: translateX(0); }
  100% { transform: translateX(calc(100vw + 40px)); }
}

@keyframes moveCloud {
  0% { transform: translateX(0); }
  100% { transform: translateX(calc(100vw + 180px)); }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

@keyframes dropPackage {
  0%, 15%, 100% { transform: translateY(0); }
  20% { transform: translateY(20px); }
  25% { transform: translateY(40px); }
  30% { transform: translateY(60px); }
  35% { transform: translateY(80px); }
  40% { transform: translateY(100px); }
  45% { transform: translateY(120px); }
  50% { transform: translateY(140px); }
  55% { transform: translateY(160px); }
  60% { transform: translateY(180px); }
  65% { transform: translateY(200px); }
  70% { transform: translateY(220px); opacity: 1; }
  75%, 100% { transform: translateY(240px); opacity: 0; }
}

@keyframes sway {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(1deg); }
}

/* 主内容区样式 */
.main-content {
  width: 90%;
  max-width: 1200px;
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 40px;
  z-index: 10;
  margin-top: 30px;
}

/* 品牌区域样式 */
.brand-section {
  text-align: center;
  margin-bottom: 20px;

  .logo-container {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 20px;
    margin-bottom: 10px;

    .logo-wrapper {
      width: 70px;
      height: 70px;
      background: linear-gradient(145deg, #42a5f5, #1976d2);
      border-radius: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
      position: relative;
      overflow: hidden;

      &::after {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: linear-gradient(
          to bottom right,
          rgba(255, 255, 255, 0) 0%,
          rgba(255, 255, 255, 0.1) 100%
        );
        transform: rotate(45deg);
      }

      .logo-icon {
        font-size: 36px;
        color: white;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
        animation: bounce 2s infinite;
      }
    }
  }

  .system-title {
    font-size: 36px;
    font-weight: bold;
    color: #1976d2;
    margin: 0;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background: linear-gradient(to right, #1976d2, #42a5f5);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .system-subtitle {
    font-size: 18px;
    color: #546e7a;
    margin: 10px 0 0;
  }
}

/* 卡片区域样式 */
.cards-section {
  display: flex;
  gap: 40px;
  width: 100%;
}

/* 卡片共用样式 */
.features-card,
.login-card {
  flex: 1;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  padding: 30px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  backdrop-filter: blur(10px);
  transition: transform 0.3s, box-shadow 0.3s;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(
      145deg,
      rgba(255, 255, 255, 0.5),
      rgba(255, 255, 255, 0)
    );
    pointer-events: none;
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 25px 30px -5px rgba(0, 0, 0, 0.15), 0 15px 15px -5px rgba(0, 0, 0, 0.07);
  }

  .card-title {
    font-size: 24px;
    color: #1976d2;
    margin: 0 0 20px;
    position: relative;
    padding-bottom: 10px;

    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 50px;
      height: 3px;
      background: linear-gradient(to right, #1976d2, #42a5f5);
      border-radius: 3px;
    }
  }
}

/* 特点卡片样式 */
.features-card {
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.85));

  .features-list {
    display: grid;
    gap: 20px;
  }

  .feature-item {
    display: flex;
    align-items: flex-start;
    gap: 15px;
    padding: 15px;
    background: rgba(255, 255, 255, 0.7);
    border-radius: 16px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
    transition: all 0.3s;

    &:hover {
      background: white;
      box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.08);
      transform: translateY(-3px);

      .feature-icon-wrapper {
        background: linear-gradient(145deg, #42a5f5, #1976d2);
        
        .feature-icon {
          color: white;
          transform: scale(1.1);
        }
      }
    }

    .feature-icon-wrapper {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      background: rgba(66, 165, 245, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s;

      .feature-icon {
        font-size: 24px;
        color: #1976d2;
        transition: all 0.3s;
      }
    }

    .feature-content {
      flex: 1;

      h3 {
        font-size: 18px;
        color: #1976d2;
        margin: 0 0 5px;
      }

      p {
        font-size: 14px;
        color: #546e7a;
        margin: 0;
        line-height: 1.5;
      }
    }
  }
}

/* 登录卡片样式 */
.login-card {
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.85));

  .card-header {
    display: flex;
    flex-direction: column;
    gap: 15px;
    margin-bottom: 25px;
  }

  .login-form {
    .el-form-item {
      margin-bottom: 20px;
    }

    .input-label {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
      font-size: 14px;
      color: #546e7a;

      .el-icon {
        font-size: 16px;
        color: #1976d2;
      }

      .validation-status {
        margin-left: auto;
        font-size: 12px;
        color: #4caf50;
      }
    }

    :deep(.el-input__wrapper) {
      background: rgba(255, 255, 255, 0.8);
      border-radius: 12px;
      box-shadow: none;
      border: 1px solid #e0e0e0;
      padding: 0 15px;
      height: 42px;
      transition: all 0.3s;

      &:hover {
        border-color: #42a5f5;
      }

      &.is-focus {
        border-color: #1976d2;
        box-shadow: 0 0 0 3px rgba(66, 165, 245, 0.2);
        background: white;
      }
    }

    :deep(.el-input.is-valid .el-input__wrapper) {
      border-color: #4caf50;
      box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.2);
    }

    .captcha-label {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
      font-size: 14px;
      color: #546e7a;

      .el-icon {
        font-size: 16px;
        color: #1976d2;
      }
      
      .validation-status {
        margin-left: auto;
        font-size: 12px;
        color: #4caf50;
      }
    }

    .captcha-inputs {
      display: flex;
      gap: 10px;
      width: 100%;

      .el-input {
        flex: 1;
      }

      .captcha-box {
        min-width: 120px;
        width: auto;
        height: 42px;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0 12px;
        background: rgba(66, 165, 245, 0.1);
        border: 1px solid #e0e0e0;
        border-radius: 12px;
        font-size: 15px;
        color: #1976d2;
        cursor: pointer;
        user-select: none;
        transition: all 0.3s;
        
        .refresh-icon {
          font-size: 14px;
          opacity: 0.5;
          transition: all 0.3s;
          margin-left: 8px;
        }
        
        &:hover {
          background: rgba(66, 165, 245, 0.2);
          border-color: #42a5f5;
          
          .refresh-icon {
            opacity: 1;
            transform: rotate(180deg);
          }
        }
      }
    }

    .validation-message {
      font-size: 12px;
      color: #4caf50;
      margin-top: 4px;
      text-align: right;
    }

    .login-actions {
      margin-top: 30px;
      display: flex;
      justify-content: center;
    }

    .login-button {
      width: 100%;
      height: 44px;
      border-radius: 12px;
      background: linear-gradient(145deg, #42a5f5, #1976d2);
      border: none;
      font-size: 16px;
      font-weight: 500;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      transition: all 0.3s;
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          90deg,
          transparent,
          rgba(255, 255, 255, 0.2),
          transparent
        );
        transition: all 0.5s;
      }

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 15px -3px rgba(25, 118, 210, 0.4);

        &::before {
          left: 100%;
        }
      }

      .el-icon {
        font-size: 18px;
      }
    }

    .security-tips {
      margin-top: 20px;
      display: flex;
      flex-direction: column;
      gap: 8px;

      .tip-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 12px;
        color: #78909c;

        .el-icon {
          font-size: 14px;
          color: #78909c;
        }
      }
    }
  }
}

/* 底部版权信息 */
.footer {
  margin-top: auto;
  padding: 20px;
  font-size: 14px;
  color: #78909c;
  text-align: center;
  width: 100%;
  z-index: 10;
}

/* 动画效果 */
@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}

/* 响应式设计 */
@media screen and (max-width: 992px) {
  .cards-section {
    flex-direction: column;
  }

  .features-card, .login-card {
    width: 100%;
  }
}

@media screen and (max-width: 768px) {
  .main-content {
    width: 95%;
    padding: 20px 0;
  }

  .brand-section .system-title {
    font-size: 28px;
  }

  .brand-section .system-subtitle {
    font-size: 16px;
  }

  .logo-container .logo-wrapper {
    width: 60px;
    height: 60px;
  }

  .logo-container .logo-icon {
    font-size: 30px;
  }
}

@media screen and (max-width: 480px) {
  .brand-section .system-title {
    font-size: 24px;
  }

  .brand-section .system-subtitle {
    font-size: 14px;
  }

  .cards-section {
    gap: 20px;
  }

  .features-card, .login-card {
    padding: 20px;
  }

  .card-title {
    font-size: 20px;
  }

  .feature-icon-wrapper {
    width: 40px;
    height: 40px;
  }

  .feature-icon {
    font-size: 20px;
  }

  .feature-content h3 {
    font-size: 16px;
  }

  .feature-content p {
    font-size: 12px;
  }
}

@media screen and (max-width: 576px) {
  .captcha-inputs {
    flex-direction: column;
  }
  
  .input-part {
    width: 100%;
  }
}
</style>
