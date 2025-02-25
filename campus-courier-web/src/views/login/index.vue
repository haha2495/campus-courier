<template>
  <div class="login-container">
    <!-- 背景动画 -->
    <div class="animated-background">
      <div class="gradient-circle circle-1"></div>
      <div class="gradient-circle circle-2"></div>
      <div class="gradient-circle circle-3"></div>
    </div>

    <!-- 主要内容区 -->
    <div class="content-wrapper">
      <!-- 左侧信息区 -->
      <div class="info-section">
        <div class="brand">
          <div class="logo-wrapper">
            <div class="logo-circle">
              <el-icon class="logo-icon">
                <School />
              </el-icon>
            </div>
          </div>
          <div class="brand-text">
            <h1 class="animate-text">校园跑腿系统管理后台</h1>
            <p class="subtitle">高效管理校园跑腿服务，提升用户体验</p>
          </div>
        </div>
        <div class="features">
          <div
            class="feature-item"
            v-for="(feature, index) in features"
            :key="index"
          >
            <el-icon class="feature-icon">
              <component :is="feature.icon" />
            </el-icon>
            <div class="feature-text">
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.desc }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录区 -->
      <div class="login-section">
        <div class="login-box">
          <div class="security-tip">
            <el-alert title="安全提示" type="info" :closable="false" show-icon>
              <template #default>
                <div class="security-tip-content">
                  <span class="tip-text">请注意保护您的账号安全：</span>
                  <div class="tip-items">
                    <div class="tip-item">
                      <el-icon>
                        <Warning />
                      </el-icon>
                      <span>请勿在公共场所保存密码</span>
                    </div>
                    <div class="tip-item">
                      <el-icon>
                        <Lock />
                      </el-icon>
                      <span>请使用复杂密码提高安全性</span>
                    </div>
                    <div class="tip-item">
                      <el-icon>
                        <Timer />
                      </el-icon>
                      <span>登录后请及时处理您的事务</span>
                    </div>
                  </div>
                </div>
              </template>
            </el-alert>
          </div>

          <!-- 登录表单 -->
          <el-form
            :model="loginForm"
            :rules="rules"
            ref="loginFormRef"
            class="login-form"
            @validate="handleValidate"
          >
            <!-- 用户名输入框 -->
            <el-form-item prop="username">
              <div class="form-label">
                请输入管理员账号
                <span class="input-status" v-if="fieldStatus.username">
                  {{ fieldStatus.username }}
                </span>
              </div>
              <el-input
                v-model="loginForm.username"
                class="form-input"
                :class="{ 'is-valid': isFieldValid('username') }"
                @focus="handleFocus('username')"
                @blur="handleBlur('username')"
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <!-- 密码输入框 -->
            <el-form-item prop="password">
              <div class="form-label">
                密码
                <span class="input-status" v-if="fieldStatus.password">
                  {{ fieldStatus.password }}
                </span>
              </div>
              <el-input
                v-model="loginForm.password"
                type="password"
                show-password
                class="form-input"
                :class="{ 'is-valid': isFieldValid('password') }"
                @focus="handleFocus('password')"
                @blur="handleBlur('password')"
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <!-- 验证码 -->
            <el-form-item prop="captcha">
              <div class="form-label">
                验证码
                <span class="input-status" v-if="fieldStatus.captcha">
                  {{ fieldStatus.captcha }}
                </span>
              </div>
              <div class="captcha-wrapper">
                <el-input
                  v-model="loginForm.captcha"
                  class="form-input"
                  :class="{ 'is-valid': isFieldValid('captcha') }"
                  @focus="handleFocus('captcha')"
                  @blur="handleBlur('captcha')"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
                <div class="captcha-box" @click="refreshCaptcha">
                  {{ captcha.question }}
                  <el-icon class="refresh-icon">
                    <RefreshRight />
                  </el-icon>
                </div>
              </div>
            </el-form-item>

            <!-- 登录按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                class="login-button"
                :loading="loading"
                :disabled="!isFormValid"
                @click="handleLogin"
              >
                <span> 管理员登录 </span>
                <el-icon class="button-icon">
                  <Right />
                </el-icon>
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import {
  Lock,
  RefreshRight,
  Right,
  School,
  Timer,
  Warning,
  User,
  Key,
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
  username: "",
  password: "",
  captcha: "",
});

// 系统特点
const features = [
  {
    icon: "Document",
    title: "接单任务管理",
    desc: "高效管理校园跑腿任务，实时跟踪任务状态",
  },
  {
    icon: "User",
    title: "用户管理",
    desc: "管理用户信息，提升服务质量",
  },
  {
    icon: "DataLine",
    title: "数据统计",
    desc: "实时查看跑腿服务数据，优化运营策略",
  },
  {
    icon: "Notification",
    title: "通知管理",
    desc: "及时通知用户任务状态，提升用户体验",
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
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e0f7fa, #80deea);
  overflow: hidden;
}

.animated-background {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;

  .gradient-circle {
    position: absolute;
    border-radius: 50%;
    filter: blur(40px);
    opacity: 0.5;
    animation: float 10s infinite ease-in-out;

    &.circle-1 {
      width: 600px;
      height: 600px;
      background: linear-gradient(45deg, #4f46e5, #06b6d4);
      top: -200px;
      left: -200px;
      animation-delay: 0s;
    }

    &.circle-2 {
      width: 450px;
      height: 450px;
      background: linear-gradient(45deg, #8b5cf6, #ec4899);
      bottom: -150px;
      right: -150px;
      animation-delay: -3s;
    }

    &.circle-3 {
      width: 300px;
      height: 300px;
      background: linear-gradient(45deg, #10b981, #3b82f6);
      top: 40%;
      left: 60%;
      animation-delay: -6s;
    }
  }

  .floating-shapes {
    position: absolute;
    width: 100%;
    height: 100%;

    .shape {
      position: absolute;
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(5px);
      border-radius: 20%;
      animation: floatShape 15s infinite linear;

      &.shape-1 {
        width: 30px;
        height: 30px;
        left: 15%;
        top: 20%;
        animation-delay: 0s;
        animation-duration: 20s;
      }

      &.shape-2 {
        width: 45px;
        height: 45px;
        left: 35%;
        top: 45%;
        animation-delay: 2s;
        animation-duration: 25s;
      }

      &.shape-3 {
        width: 25px;
        height: 25px;
        left: 55%;
        top: 70%;
        animation-delay: 4s;
        animation-duration: 22s;
      }

      &.shape-4 {
        width: 40px;
        height: 40px;
        left: 75%;
        top: 25%;
        animation-delay: 6s;
        animation-duration: 28s;
      }

      &.shape-5 {
        width: 35px;
        height: 35px;
        left: 85%;
        top: 55%;
        animation-delay: 8s;
        animation-duration: 24s;
      }

      &.shape-6 {
        width: 50px;
        height: 50px;
        left: 25%;
        top: 85%;
        animation-delay: 10s;
        animation-duration: 26s;
      }

      &.shape-7 {
        width: 45px;
        height: 45px;
        left: 65%;
        top: 15%;
        animation-delay: 12s;
        animation-duration: 23s;
      }

      &.shape-8 {
        width: 30px;
        height: 30px;
        left: 95%;
        top: 75%;
        animation-delay: 14s;
        animation-duration: 27s;
      }
    }
  }
}

.content-wrapper {
  width: 90%;
  max-width: 1400px;
  min-height: 600px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  display: flex;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
}

.info-section {
  flex: 1.2;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;

  &::after {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    width: 1px;
    height: 100%;
    background: linear-gradient(
      to bottom,
      transparent,
      rgba(99, 102, 241, 0.1),
      transparent
    );
  }

  .brand {
    text-align: center;
    margin-bottom: 60px;

    .logo-wrapper {
      margin-bottom: 24px;
      position: relative;

      .logo-circle {
        width: 80px;
        height: 80px;
        background: linear-gradient(135deg, #4f46e5, #06b6d4);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
        position: relative;

        &::after {
          content: "";
          position: absolute;
          width: 100%;
          height: 100%;
          border-radius: 50%;
          background: inherit;
          filter: blur(20px);
          opacity: 0.3;
          z-index: -1;
        }

        .logo-icon {
          font-size: 36px;
          color: white;
          animation: float 3s ease-in-out infinite;
        }
      }
    }

    .brand-text {
      h1 {
        font-size: 36px;
        color: #00796b;
        margin-bottom: 16px;
        font-weight: 600;
        letter-spacing: -0.5px;
      }

      .subtitle {
        font-size: 16px;
        color: #004d40;
        line-height: 1.6;
        max-width: 400px;
        margin: 0 auto;
        position: relative;

        &::after {
          content: "";
          display: block;
          width: 40px;
          height: 2px;
          background: linear-gradient(to right, #4f46e5, #06b6d4);
          margin: 20px auto 0;
          border-radius: 2px;
        }
      }
    }
  }

  .features {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;

    .feature-item {
      display: flex;
      align-items: flex-start;
      gap: 16px;
      padding: 24px;
      background: rgba(255, 255, 255, 0.8);
      border-radius: 12px;
      transition: all 0.3s;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      position: relative;
      overflow: hidden;

      &::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          45deg,
          transparent,
          rgba(79, 70, 229, 0.03),
          transparent
        );
        transform: translateX(-100%);
      }

      &:hover {
        transform: translateY(-5px);
        background: rgba(255, 255, 255, 0.9);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);

        &::before {
          transform: translateX(100%);
          transition: transform 0.8s;
        }

        .feature-icon {
          transform: scale(1.1) rotate(5deg);
          color: #4f46e5;
        }
      }

      .feature-icon {
        font-size: 24px;
        color: #6366f1;
        transition: all 0.3s;
        background: rgba(99, 102, 241, 0.1);
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .feature-text {
        flex: 1;

        h3 {
          font-size: 18px;
          color: #1f2937;
          margin-bottom: 8px;
          font-weight: 500;
        }

        p {
          font-size: 14px;
          color: #6b7280;
          line-height: 1.6;
        }
      }

      @for $i from 1 through 4 {
        &:nth-child(#{$i}) {
          animation: fadeInUp 0.5s ease-out;
          animation-delay: #{$i * 0.1}s;
          animation-fill-mode: both;
        }
      }
    }
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}

.login-section {
  flex: 1;
  background: white;
  border-radius: 24px;
  padding: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 100%;
  max-width: 380px;
  margin: 0 auto;
}

.security-tip {
  margin-bottom: 20px;
}

:deep(.el-alert) {
  background-color: #f8fafc;
  padding: 12px 16px;
  border: none;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

:deep(.el-alert .el-alert__icon) {
  font-size: 16px;
  color: #94a3b8;
  margin-right: 12px;
}

:deep(.el-alert__title) {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.security-tip-content {
  margin-top: 4px;
}

.tip-text {
  font-size: 13px;
  color: #64748b;
  display: block;
  margin-bottom: 8px;
}

.tip-items {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748b;

  .el-icon {
    font-size: 14px;
    color: #94a3b8;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }
}

.form-label {
  font-size: 14px;
  color: #4b5563;
  margin-bottom: 8px;
  padding-left: 2px;
}

.input-status {
  font-size: 0.8rem;
  color: #67c23a;
}

.form-input {
  :deep(.el-input__wrapper) {
    height: 42px;
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    box-shadow: none;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
    position: relative;
    border-radius: 12px !important;

    &::before {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(79, 70, 229, 0.1),
        transparent
      );
      transform: translateX(-100%);
    }

    &:hover {
      border-color: #4f46e5;
      transform: translateY(-1px);
    }

    &.is-focus {
      background: white;
      border-color: #4f46e5;
      box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
    }
  }

  :deep(.el-input__prefix) {
    margin-right: 8px;

    .el-icon {
      transition: all 0.3s;
      color: #9ca3af;

      &:hover {
        transform: rotate(15deg);
        color: #4f46e5;
      }
    }
  }
}

.captcha-wrapper {
  display: flex;
  gap: 12px;

  .form-input {
    flex: 1;
  }

  .captcha-box {
    width: 120px;
    height: 42px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    background: #f3f4f6;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    color: #4f46e5;
    font-size: 15px;
    cursor: pointer;
    transition: all 0.3s;
    user-select: none;
    position: relative;
    overflow: hidden;

    &::before {
      content: "";
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: linear-gradient(
        45deg,
        transparent,
        rgba(79, 70, 229, 0.1),
        transparent
      );
      transform: rotate(45deg);
      animation: shine 3s infinite;
    }

    .refresh-icon {
      font-size: 14px;
      opacity: 0;
      transition: all 0.3s;
    }

    &:hover {
      background: #e5e7eb;
      transform: translateY(-2px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

      .refresh-icon {
        opacity: 1;
        animation: spin 1s ease-in-out;
      }
    }
  }
}

.login-button {
  width: 100%;
  height: 42px;
  background: linear-gradient(90deg, #00796b, #004d40);
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.2),
      transparent
    );
    transform: translateX(-100%);
  }

  span {
    position: relative;
    z-index: 1;
  }

  .button-icon {
    position: relative;
    z-index: 1;
    opacity: 0;
    transform: translateX(-10px);
    transition: all 0.3s;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(79, 70, 229, 0.2);

    &::before {
      transform: translateX(100%);
      transition: transform 0.8s;
    }

    .button-icon {
      opacity: 1;
      transform: translateX(0);
      animation: slideRight 0.8s infinite alternate ease-in-out;
    }
  }
}

@keyframes shine {
  0% {
    transform: rotate(45deg) translateY(-100%);
  }
  100% {
    transform: rotate(45deg) translateY(100%);
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes slideRight {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(4px);
  }
}

// 添加表单验证动画
.el-form-item {
  &.is-error {
    .el-input__wrapper {
      animation: shake 0.5s;
    }
  }
}

@keyframes shake {
  0%,
  100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-5px);
  }
  75% {
    transform: translateX(5px);
  }
}

@keyframes floatShape {
  0% {
    transform: translate(0, 0) rotate(0deg);
  }
  100% {
    transform: translate(-400px, 400px) rotate(360deg);
  }
}

// 响应式设计
@media screen and (max-width: 1200px) {
  .content-wrapper {
    width: 95%;
  }

  .info-section {
    padding: 40px;
  }

  .login-section {
    padding: 40px;
  }
}

@media screen and (max-width: 992px) {
  .content-wrapper {
    flex-direction: column;
    min-height: auto;
  }

  .info-section {
    .features {
      grid-template-columns: 1fr;
    }
  }
}

@media screen and (max-width: 768px) {
  .content-wrapper {
    width: 100%;
    border-radius: 0;
    min-height: 100vh;
  }

  .info-section {
    padding: 30px;
  }

  .login-section {
    padding: 30px;
  }

  .login-box {
    max-width: 100%;
  }
}

@media screen and (max-width: 480px) {
  .info-section {
    padding: 20px;

    .brand {
      margin-bottom: 30px;

      h1 {
        font-size: 28px;
      }
    }
  }

  .login-section {
    padding: 20px;
  }

  .captcha-wrapper {
    flex-direction: column;

    .captcha-box {
      width: 100%;
    }
  }
}
</style>
