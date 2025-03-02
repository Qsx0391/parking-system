// 车牌识别相关接口
export async function recognizePlate(imageData) {
  const response = await fetch(`${import.meta.env.VITE_RECOGNITION_API_URL}/recognize`, {
    method: 'POST',
    body: imageData
  })
  return response.json()
} 