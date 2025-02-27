// 车牌识别相关接口
export async function recognizePlate(imageData) {
  const response = await fetch('http://localhost:8081/api/recognize', {
    method: 'POST',
    body: imageData
  })
  return response.json()
} 