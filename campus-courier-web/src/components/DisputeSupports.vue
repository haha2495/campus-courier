<template>
  <div class="dispute-supports">
    <template v-if="disputeSupports?.length">
      <!-- 支持数据对比区域 -->
      <div class="support-compare">
        <div class="compare-side publisher">
          <div class="side-icon">
            <el-icon><UserFilled /></el-icon>
          </div>
          <div class="side-label">发布者支持</div>
          <div class="side-count">{{ publisherCount }}</div>
        </div>

        <div class="compare-middle">
          <div class="vs-badge">VS</div>
          <div class="progress-bar">
            <div class="progress-left" :style="{ width: `${publisherPercent}%` }">
              <span class="percent">{{ publisherPercent }}%</span>
            </div>
            <div class="progress-right" :style="{ width: `${runnerPercent}%` }">
              <span class="percent">{{ runnerPercent }}%</span>
            </div>
          </div>
        </div>

        <div class="compare-side runner">
          <div class="side-icon">
            <el-icon><Medal /></el-icon>
          </div>
          <div class="side-label">跑腿者支持</div>
          <div class="side-count">{{ runnerCount }}</div>
        </div>
      </div>

      <!-- 支持记录列表 -->
      <div class="support-list">
        <div class="list-title">
          <el-icon><ChatLineSquare /></el-icon>
          <span>支持记录</span>
        </div>
        <div class="list-content">
          <div 
            v-for="support in disputeSupports" 
            :key="support.id" 
            class="support-item"
            :class="support.supportType === 1 ? 'publisher' : 'runner'"
          >
            <div class="item-header">
              <el-tag 
                :type="support.supportType === 1 ? 'danger' : 'success'" 
                size="small"
                effect="plain"
              >
                {{ support.supportType === 1 ? '支持发布者' : '支持跑腿者' }}
              </el-tag>
              <span class="time">{{ formatTime(support.createTime) }}</span>
            </div>
            <div class="item-content" v-if="support.reason">
              {{ support.reason }}
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 空状态展示 -->
    <div v-else class="empty-state">
      <el-empty 
        description="暂无支持信息" 
        :image-size="120"
      >
        <template #image>
          <el-icon class="empty-icon"><ChatDotSquare /></el-icon>
        </template>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { UserFilled, Medal, ChatLineSquare, ChatDotSquare } from '@element-plus/icons-vue'

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
  padding: 24px;
  background: #fff;
  border-radius: 12px;
}

.support-compare {
  display: flex;
  align-items: center;
  gap: 32px;
  padding: 32px;
  background: linear-gradient(135deg, #f8fafc, #f1f5f9);
  border-radius: 16px;
  margin-bottom: 24px;

  .compare-side {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;

    .side-icon {
      width: 64px;
      height: 64px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;

      .el-icon {
        font-size: 32px;
      }
    }

    .side-label {
      font-size: 15px;
      color: #64748b;
    }

    .side-count {
      font-size: 36px;
      font-weight: 600;
      font-family: 'DIN Condensed', sans-serif;
    }

    &.publisher {
      .side-icon {
        color: #f43f5e;
        background: #fff1f2;
      }
      .side-count {
        color: #f43f5e;
      }
    }

    &.runner {
      .side-icon {
        color: #10b981;
        background: #ecfdf5;
      }
      .side-count {
        color: #10b981;
      }
    }
  }

  .compare-middle {
    flex: 2;
    display: flex;
    flex-direction: column;
    gap: 20px;
    align-items: center;

    .vs-badge {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      background: #1f2937;
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
      height: 24px;
      background: #e2e8f0;
      border-radius: 12px;
      display: flex;
      overflow: hidden;

      .progress-left,
      .progress-right {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
        position: relative;

        .percent {
          color: white;
          font-size: 13px;
          font-weight: 500;
          text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
        }
      }

      .progress-left {
        background: linear-gradient(90deg, #f43f5e, #fb7185);
      }

      .progress-right {
        background: linear-gradient(90deg, #10b981, #34d399);
      }
    }
  }
}

.support-list {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #e2e8f0;

  .list-title {
    padding: 16px 24px;
    border-bottom: 1px solid #e2e8f0;
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;

    .el-icon {
      font-size: 20px;
      color: #6366f1;
    }
  }

  .list-content {
    padding: 16px;

    .support-item {
      padding: 16px;
      border-radius: 8px;
      background: #f8fafc;
      transition: all 0.3s ease;

      &:not(:last-child) {
        margin-bottom: 12px;
      }

      &:hover {
        transform: translateX(4px);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      }

      .item-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;

        .time {
          font-size: 13px;
          color: #64748b;
        }
      }

      .item-content {
        color: #475569;
        font-size: 14px;
        line-height: 1.6;
      }

      &.publisher {
        border-left: 3px solid #f43f5e;
      }

      &.runner {
        border-left: 3px solid #10b981;
      }
    }
  }
}

.empty-state {
  padding: 48px 0;
  
  :deep(.el-empty) {
    .empty-icon {
      font-size: 60px;
      color: #94a3b8;
    }
  }
}
</style> 