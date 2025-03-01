<template>
  <div class="dispute-supports">
    <template v-if="disputeSupports?.length">
      <!-- PK展示区域 -->
      <div class="support-pk">
        <div class="pk-side publisher">
          <div class="side-header">
            <el-icon><UserFilled /></el-icon>
            <span>发布者支持</span>
          </div>
          <div class="support-count">{{ publisherCount }}</div>
          <div class="support-percent">{{ publisherPercent }}%</div>
        </div>
        
        <div class="pk-middle">
          <div class="vs-circle">
            <span>VS</span>
          </div>
          <div class="progress-bar">
            <div 
              class="progress-publisher" 
              :style="{ width: `${publisherPercent}%` }"
            ></div>
            <div 
              class="progress-runner" 
              :style="{ width: `${runnerPercent}%` }"
            ></div>
          </div>
        </div>

        <div class="pk-side runner">
          <div class="side-header">
            <el-icon><Medal /></el-icon>
            <span>跑腿者支持</span>
          </div>
          <div class="support-count">{{ runnerCount }}</div>
          <div class="support-percent">{{ runnerPercent }}%</div>
        </div>
      </div>

      <!-- 支持列表 -->
      <div class="support-list">
        <div 
          v-for="support in disputeSupports" 
          :key="support.id" 
          class="support-item"
          :class="{ 'publisher-support': support.supportType === 1, 'runner-support': support.supportType === 2 }"
        >
          <div class="support-header">
            <div class="support-type">
              <el-tag :type="support.supportType === 1 ? 'danger' : 'success'" size="small">
                {{ support.supportType === 1 ? '支持发布者' : '支持跑腿者' }}
              </el-tag>
            </div>
            <div class="support-time">{{ formatTime(support.createTime) }}</div>
          </div>
          <div class="support-reason" v-if="support.reason">
            {{ support.reason }}
          </div>
        </div>
      </div>
    </template>

    <!-- 空状态展示 -->
    <div v-else class="empty-state">
      <div class="empty-icon">
        <el-icon><Opportunity /></el-icon>
      </div>
      <div class="empty-text">暂无支持信息</div>
      <div class="empty-desc">等待用户提供支持意见</div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { UserFilled, Medal, Opportunity } from '@element-plus/icons-vue'

const props = defineProps({
  disputeSupports: {
    type: Array,
    default: () => []
  }
})

// 计算发布者支持数量
const publisherCount = computed(() => {
  return props.disputeSupports.filter(s => s.supportType === 1).length
})

// 计算跑腿者支持数量
const runnerCount = computed(() => {
  return props.disputeSupports.filter(s => s.supportType === 2).length
})

// 计算支持百分比
const publisherPercent = computed(() => {
  const total = publisherCount.value + runnerCount.value
  return total ? Math.round((publisherCount.value / total) * 100) : 50
})

const runnerPercent = computed(() => {
  return 100 - publisherPercent.value
})

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}
</script>

<style scoped lang="scss">
.dispute-supports {
  padding: 20px;
}

.support-pk {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  position: relative;

  .pk-side {
    flex: 1;
    text-align: center;
    padding: 20px;
    border-radius: 8px;
    transition: all 0.3s;

    &:hover {
      background: rgba(255, 255, 255, 0.8);
    }

    .side-header {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      margin-bottom: 16px;
      font-size: 15px;
      color: #475569;

      .el-icon {
        font-size: 18px;
      }
    }

    .support-count {
      font-size: 32px;
      font-weight: 600;
      margin-bottom: 8px;
      font-family: 'DIN Condensed', sans-serif;
    }

    .support-percent {
      font-size: 16px;
      color: #64748b;
      font-family: 'DIN Condensed', sans-serif;
    }

    &.publisher .support-count {
      color: #f43f5e;
    }

    &.runner .support-count {
      color: #10b981;
    }
  }

  .pk-middle {
    width: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;

    .vs-circle {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      background: linear-gradient(135deg, #f43f5e, #10b981);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 600;
      font-size: 16px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .progress-bar {
      width: 100%;
      height: 8px;
      background: #e2e8f0;
      border-radius: 4px;
      overflow: hidden;
      position: relative;
      display: flex;

      .progress-publisher {
        background: linear-gradient(90deg, #fecdd3, #f43f5e);
        height: 100%;
        transition: width 0.3s ease;
      }

      .progress-runner {
        background: linear-gradient(90deg, #10b981, #059669);
        height: 100%;
        transition: width 0.3s ease;
      }
    }
  }
}

.support-list {
  .support-item {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 16px;
    border: 1px solid #e2e8f0;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .support-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;

      .support-time {
        font-size: 13px;
        color: #64748b;
      }
    }

    .support-reason {
      font-size: 14px;
      color: #475569;
      line-height: 1.6;
      background: #f8fafc;
      padding: 12px;
      border-radius: 6px;
    }

    &.publisher-support {
      border-left: 3px solid #f43f5e;
    }

    &.runner-support {
      border-left: 3px solid #10b981;
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 0;
  background: #f8fafc;
  border-radius: 12px;
  
  .empty-icon {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    background: #e2e8f0;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16px;
    
    .el-icon {
      font-size: 32px;
      color: #64748b;
    }
  }

  .empty-text {
    font-size: 16px;
    color: #475569;
    margin-bottom: 8px;
    font-weight: 500;
  }

  .empty-desc {
    font-size: 14px;
    color: #64748b;
  }
}
</style> 