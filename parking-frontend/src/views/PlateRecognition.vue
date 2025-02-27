<template>
  <div>
    <n-card title="车牌识别">
      <n-space vertical>
        <n-space>
          <n-upload
            accept="image/*"
            :show-file-list="false"
            @change="handleUpload"
          >
            <n-button>上传车牌图片</n-button>
          </n-upload>
          <n-button type="primary" @click="startCamera" v-if="!showCamera">
            开启摄像头
          </n-button>
          <n-button @click="takePhoto" v-if="showCamera" :loading="submitting">
            拍照识别
          </n-button>
          <n-button @click="closeCamera" v-if="showCamera">
            关闭摄像头
          </n-button>
          <n-input
            v-model:value="formData.plateNumber"
            placeholder="请输入车牌号"
            style="width: 160px"
          />
          <n-space>
            <n-button
              type="primary"
              @click="handleEntry"
              :loading="submitting"
            >
              入场
            </n-button>
            <n-button
              type="warning"
              @click="handlePayment"
              :loading="submitting"
            >
              缴费
            </n-button>
            <n-button
              type="error"
              @click="handleExit"
              :loading="submitting"
            >
              离场
            </n-button>
          </n-space>
        </n-space>

        <div class="preview-area">
          <img v-if="previewUrl" :src="previewUrl" class="preview-image"/>
          <video 
            v-if="showCamera" 
            ref="videoRef" 
            class="preview-video"
            autoplay
            playsinline
          ></video>
          <canvas 
            ref="canvasRef" 
            style="display: none"
          ></canvas>
        </div>

        <n-card v-if="recognitionResult" title="识别结果" size="small">
          <n-descriptions>
            <n-descriptions-item label="车牌号">
              {{ recognitionResult.plateNumber }}
            </n-descriptions-item>
            <n-descriptions-item label="置信度">
              {{ (recognitionResult.confidence * 100).toFixed(2) }}%
            </n-descriptions-item>
            <n-descriptions-item label="识别时间">
              {{ recognitionResult.time }}
            </n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-space>
    </n-card>
  </div>
</template>

<script setup>
import { ref, onUnmounted, nextTick } from 'vue'
import { 
  NCard, 
  NSpace, 
  NButton, 
  NUpload,
  NDescriptions,
  NDescriptionsItem,
  NInput, 
  NModal,
  useMessage,
  useDialog
} from 'naive-ui'
import { vehicleEntry, vehicleExit } from '@/api/vehicle'
import { createPayment, payOffline, cancelPayment } from '@/api/payment'
import { recognizePlate } from '@/api/recognition'

const message = useMessage()
const dialog = useDialog()
const previewUrl = ref('')
const showCamera = ref(false)
const videoRef = ref(null)
const recognitionResult = ref(null)
const formData = ref({
  plateNumber: ''
})
const submitting = ref(false)
const currentVid = ref(null)
const currentPid = ref(null)
const canvasRef = ref(null)
let stream = null

const handleUpload = async ({ file }) => {
  recognitionResult.value = null
  previewUrl.value = ''

  if (!file || !file.file) {
    message.error('请选择图片文件')
    return
  }

  // 处理图片预览
  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target.result
  }
  reader.readAsDataURL(file.file)

  // 创建 FormData
  const form = new FormData()
  form.append('image', file.file)

  try {
    const result = await recognizePlate(form)

    if (result.code === '0') {
      recognitionResult.value = {
        plateNumber: result.data.plate_number,
        confidence: result.data.confidence,
        time: new Date().toLocaleString()
      }
      formData.value.plateNumber = result.data.plate_number
    } else if (result.code === 'A000400') {
      message.error('车牌识别失败')
    } else if (result.code === 'A000401') {
      message.error('未检测到车牌')
    } else {
      message.error(result.message || '识别失败')
    }
  } catch (error) {
    console.error('识别请求失败:', error)
    message.error('识别请求失败')
  }
}

const startCamera = async () => {
  try {
    // 先获取媒体流
    stream = await navigator.mediaDevices.getUserMedia({ 
      video: { 
        width: 640,
        height: 480,
        facingMode: 'environment' // 优先使用后置摄像头
      } 
    })

    // 设置状态和清除预览
    showCamera.value = true
    previewUrl.value = ''

    // 使用 nextTick 确保 video 元素已经渲染
    await nextTick()
    
    // 然后设置视频源
    if (videoRef.value) {
      videoRef.value.srcObject = stream
    } else {
      throw new Error('Video element not found')
    }
  } catch (error) {
    message.error('无法访问摄像头')
    console.error('摄像头错误:', error)
    showCamera.value = false
  }
}

const takePhoto = async () => {
  if (!showCamera.value) return
  submitting.value = true

  try {
    const video = videoRef.value
    const canvas = canvasRef.value
    canvas.width = video.videoWidth
    canvas.height = video.videoHeight
    const context = canvas.getContext('2d')
    context.drawImage(video, 0, 0, canvas.width, canvas.height)
    
    closeCamera()
    
    canvas.toBlob(async (blob) => {
      const form = new FormData()
      form.append('image', blob, 'camera.jpg')

      try {
        const result = await recognizePlate(form)

        if (result.code === '0') {
          recognitionResult.value = {
            plateNumber: result.data.plate_number,
            confidence: result.data.confidence,
            time: new Date().toLocaleString()
          }
          formData.value.plateNumber = result.data.plate_number
          previewUrl.value = canvas.toDataURL('image/jpeg')
        } else {
          message.error(result.message || '识别失败')
        }
      } catch (error) {
        message.error('识别请求失败')
      } finally {
        submitting.value = false
      }
    }, 'image/jpeg')
  } catch (error) {
    message.error('拍照失败')
    submitting.value = false
    closeCamera()
  }
}

const closeCamera = () => {
  if (stream) {
    stream.getTracks().forEach(track => {
      track.stop()
    })
    stream = null
  }
  if (videoRef.value) {
    videoRef.value.srcObject = null
  }
  showCamera.value = false
}

const validatePlateNumber = () => {
  const plateNumber = formData.value.plateNumber
  if (!plateNumber) {
    message.error('请输入车牌号')
    return false
  }
  const plateNumberPattern = new RegExp(
     '^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|' +
     '([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|' +
     '([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$'
  )
  if (!plateNumberPattern.test(plateNumber)) {
    message.error('请输入正确的车牌号格式')
    return false
  }
  return true
}

const handleEntry = async () => {
  if (!validatePlateNumber()) return
  
  submitting.value = true
  try {
    const response = await vehicleEntry(formData.value.plateNumber)
    
    if (response.data.code === '0') {
      message.success('入场成功')
    } else {
      message.error(response.data.message || '入场失败')
    }
  } catch (error) {
    message.error('入场失败')
  } finally {
    submitting.value = false
  }
}

const handlePayment = async () => {
  if (!validatePlateNumber()) return
  
  submitting.value = true
  try {
    const paymentResponse = await createPayment(formData.value.plateNumber)
    
    if (paymentResponse.data.code === '0') {
      const { vid, pid, fee, enteredAt, exitedAt } = paymentResponse.data.data
      currentVid.value = vid.toString()
      currentPid.value = pid.toString()
      
      // 修改格式化时间显示函数
      const formatTime = (timeStr) => {
        if (!timeStr) return '未出场'
        return new Date(timeStr).toLocaleString()
      }
      
      // 显示确认对话框
      dialog.warning({
        title: '支付确认',
        content: `
          车牌号：${formData.value.plateNumber}
          入场时间：${formatTime(enteredAt)}
          出场时间：${formatTime(exitedAt)}
          应付金额：${fee}元
          
          是否确认支付？
        `,
        positiveText: '确认支付',
        negativeText: '取消',
        onPositiveClick: async () => {
          return await handleConfirmPayment(vid, pid)
        },
        onNegativeClick: async () => {
          return await handleCancelPayment(vid, pid)
        }
      })
    } else {
      message.error(paymentResponse.data.message || '获取支付信息失败')
    }
  } catch (error) {
    message.error('缴费失败')
  } finally {
    submitting.value = false
  }
}

// 确认支付处理
const handleConfirmPayment = async (vid, pid) => {
  try {
    const payResult = await payOffline(vid, pid)
    if (payResult.data.code === '0') {
      message.success('支付成功，请在15分钟内离场')
      return true
    } else {
      message.error(payResult.data.message || '支付失败')
    }
    return false
  } catch (error) {
    message.error('支付处理失败')
    return false
  }
}

// 取消支付处理
const handleCancelPayment = async (vid, pid) => {
  try {
    const cancelResult = await cancelPayment(vid, pid)
    if (cancelResult.data.code === '0') {
      message.success('已取消支付')
      return true
    } else {
      message.error(cancelResult.data.message || '取消支付失败')
    }
    return false
  } catch (error) {
    message.error('取消支付失败')
    return false
  }
}

const handleExit = async () => {
  if (!validatePlateNumber()) return
  
  submitting.value = true
  try {
    const exitResult = await vehicleExit(formData.value.plateNumber)
    if (exitResult.data.code === '0') {
      message.success('请驶出停车场')
    } else {
      message.error(exitResult.data.message || '出场失败')
    }
  } catch (error) {
    message.error('出场处理失败')
  } finally {
    submitting.value = false
  }
}

// 组件卸载时清理
onUnmounted(() => {
  closeCamera()
})
</script>

<style scoped>
.preview-area {
  width: 100%;
  height: 300px;
  border: 1px dashed #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
}

.preview-image, .preview-video {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.preview-video {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style> 