from flask import Flask, request, jsonify
from flask_cors import CORS
import hyperlpr3 as lpr
import cv2
import numpy as np

app = Flask(__name__)
# 更详细的CORS配置
CORS(app, resources={
    r"/api/recognize": {
        "origins": ["*"],  # 允许所有域名访问，生产环境建议设置具体的域名
        "methods": ["POST"],  # 只允许POST方法
        "allow_headers": ["Content-Type"]  # 允许的请求头
    }
})

# 初始化车牌识别器
catcher = lpr.LicensePlateCatcher()

@app.route('/api/recognize', methods=['POST'])
def recognize_plate():
    if 'image' not in request.files:
        return jsonify({
            'code': 'A000400',
            'message': '未上传文件',
            'data': {}
        })
    
    file = request.files['image']
    if file.filename == '':
        return jsonify({
            'code': 'A000400',
            'message': '未指定文件',
            'data': {}
        })

    try:
        # 读取图片文件
        file_bytes = np.frombuffer(file.read(), np.uint8)
        img = cv2.imdecode(file_bytes, cv2.IMREAD_COLOR)
        
        # 识别车牌
        results = catcher(img)
        
        if not results:
            return jsonify({
                'code': 'A000401',
                'message': '未检测到车牌',
                'data': {}
            })

        # 取置信度最高的结果
        plate_number = results[0][0]
        confidence = float(results[0][1])
        
        return jsonify({
            'code': '0',
            'message': 'success',
            'data': {
                'plate_number': plate_number,
                'confidence': confidence
            }
        })
        
    except Exception as e:
        return jsonify({
            'code': 'A000400',
            'message': f'recognition failure: {str(e)}',
            'data': {}
        })

if __name__ == '__main__':
    app.run(debug=True, port=8081) 