下面为你生成一份**接口测试文档**，涵盖API接口文档中所有主要接口的典型测试用例。你可以用Postman、Apifox、curl等工具直接测试。每个接口都给出请求方式、URL、示例参数、预期返回结构和测试建议。

**注意：所有URL参数名和JSON字段名均为驼峰命名（如roomId、cabinetId、deviceId、parentType等），与后端接口参数和实体类属性保持一致。**

---

# 机房 Room

### 1. 获取机房列表
- **GET** `/api/rooms`
- **预期返回：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": "A机房",
      "location": "一楼东侧",
      "imageId": 1,
      "description": "主生产机房"
    }
  ]
}
```
- **测试建议：**  
  直接访问，检查返回数组内容与数据库一致。

### 2. 获取单个机房详情
- **GET** `/api/rooms/1`
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "name": "A机房",
    "location": "一楼东侧",
    "imageId": 1,
    "description": "主生产机房"
  }
}
```
- **测试建议：**  
  用已存在的id测试，返回内容应与数据库一致。

### 3. 新增机房
- **POST** `/api/rooms`
- **Body:**
```json
{
  "name": "C机房",
  "location": "三楼南侧",
  "imageId": 2,
  "description": "新建机房"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "name": "C机房",
    "location": "三楼南侧",
    "imageId": 2,
    "description": "新建机房"
  }
}
```
- **测试建议：**  
  新增后用GET接口验证是否插入成功。

### 4. 修改机房
- **PUT** `/api/rooms/3`
- **Body:**
```json
{
  "name": "C机房-改",
  "location": "三楼南侧",
  "imageId": 2,
  "description": "机房已改名"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "name": "C机房-改",
    "location": "三楼南侧",
    "imageId": 2,
    "description": "机房已改名"
  }
}
```
- **测试建议：**  
  修改后用GET接口验证内容是否变更。

### 5. 删除机房
- **DELETE** `/api/rooms/3`
- **预期返回：**
```json
{
  "code": 0,
  "message": "删除成功"
}
```
- **测试建议：**  
  删除后用GET接口验证该id是否已不存在。

---

# 机柜 Cabinet

### 1. 获取机柜列表
- **GET** `/api/cabinets?roomId=1`
- **预期返回：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "roomId": 1,
      "name": "A1柜",
      "number": 1,
      "rowNumber": 1,
      "columnNumber": 1,
      "imageFront": 3,
      "imageBack": 4,
      "description": "主交换机柜"
    }
  ]
}
```
- **测试建议：**  
  用不同roomId测试，返回内容应与数据库一致。

### 2. 获取单个机柜详情
- **GET** `/api/cabinets/1`
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "roomId": 1,
    "name": "A1柜",
    "number": 1,
    "rowNumber": 1,
    "columnNumber": 1,
    "imageFront": 3,
    "imageBack": 4,
    "description": "主交换机柜"
  }
}
```
- **测试建议：**  
  用已存在的id测试。

### 3. 新增机柜
- **POST** `/api/cabinets`
- **Body:**
```json
{
  "roomId": 1,
  "name": "A3柜",
  "number": 3,
  "rowNumber": 1,
  "columnNumber": 3,
  "imageFront": 5,
  "imageBack": 6,
  "description": "新机柜"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "roomId": 1,
    "name": "A3柜",
    "number": 3,
    "rowNumber": 1,
    "columnNumber": 3,
    "imageFront": 5,
    "imageBack": 6,
    "description": "新机柜"
  }
}
```
- **测试建议：**  
  新增后用GET接口验证。

### 4. 修改机柜
- **PUT** `/api/cabinets/3`
- **Body:**
```json
{
  "roomId": 1,
  "name": "A3柜-改",
  "number": 3,
  "rowNumber": 1,
  "columnNumber": 3,
  "imageFront": 5,
  "imageBack": 6,
  "description": "机柜已改名"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "roomId": 1,
    "name": "A3柜-改",
    "number": 3,
    "rowNumber": 1,
    "columnNumber": 3,
    "imageFront": 5,
    "imageBack": 6,
    "description": "机柜已改名"
  }
}
```
- **测试建议：**  
  修改后用GET接口验证。

### 5. 删除机柜
- **DELETE** `/api/cabinets/3`
- **预期返回：**
```json
{
  "code": 0,
  "message": "删除成功"
}
```
- **测试建议：**  
  删除后用GET接口验证。

---

# 设备 Device

### 1. 获取设备列表
- **GET** `/api/devices?cabinetId=1`
- **预期返回：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "cabinetId": 1,
      "name": "服务器A",
      "number": 1,
      "imageFront": 13,
      "imageBack": 14,
      "description": "应用服务器"
    }
  ]
}
```
- **测试建议：**  
  用不同cabinetId测试。

### 2. 获取单个设备详情
- **GET** `/api/devices/1`
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "cabinetId": 1,
    "name": "服务器A",
    "number": 1,
    "imageFront": 13,
    "imageBack": 14,
    "description": "应用服务器"
  }
}
```
- **测试建议：**  
  用已存在的id测试。

### 3. 新增设备
- **POST** `/api/devices`
- **Body:**
```json
{
  "cabinetId": 1,
  "name": "新服务器",
  "number": 3,
  "imageFront": 15,
  "imageBack": 16,
  "description": "测试服务器"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "cabinetId": 1,
    "name": "新服务器",
    "number": 3,
    "imageFront": 15,
    "imageBack": 16,
    "description": "测试服务器"
  }
}
```
- **测试建议：**  
  新增后用GET接口验证。

### 4. 修改设备
- **PUT** `/api/devices/3`
- **Body:**
```json
{
  "cabinetId": 1,
  "name": "新服务器-改",
  "number": 3,
  "imageFront": 15,
  "imageBack": 16,
  "description": "服务器已改名"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "cabinetId": 1,
    "name": "新服务器-改",
    "number": 3,
    "imageFront": 15,
    "imageBack": 16,
    "description": "服务器已改名"
  }
}
```
- **测试建议：**  
  修改后用GET接口验证。

### 5. 删除设备
- **DELETE** `/api/devices/3`
- **预期返回：**
```json
{
  "code": 0,
  "message": "删除成功"
}
```
- **测试建议：**  
  删除后用GET接口验证。

---

# 接口 Interface

### 1. 获取接口列表
- **GET** `/api/interfaces?deviceId=1`
- **预期返回：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "deviceId": 1,
      "name": "eth0",
      "number": 1,
      "description": "上联端口",
      "targetId": 2
    }
  ]
}
```
- **测试建议：**  
  用不同deviceId测试。

### 2. 获取单个接口详情
- **GET** `/api/interfaces/1`
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "deviceId": 1,
    "name": "eth0",
    "number": 1,
    "description": "上联端口",
    "targetId": 2
  }
}
```
- **测试建议：**  
  用已存在的id测试。

### 3. 新增接口
- **POST** `/api/interfaces`
- **Body:**
```json
{
  "deviceId": 1,
  "name": "eth2",
  "number": 2,
  "description": "新端口",
  "targetId": null
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "deviceId": 1,
    "name": "eth2",
    "number": 2,
    "description": "新端口",
    "targetId": null
  }
}
```
- **测试建议：**  
  新增后用GET接口验证。

### 4. 修改接口
- **PUT** `/api/interfaces/3`
- **Body:**
```json
{
  "deviceId": 1,
  "name": "eth2-改",
  "number": 2,
  "description": "端口已改名",
  "targetId": 1
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 3,
    "deviceId": 1,
    "name": "eth2-改",
    "number": 2,
    "description": "端口已改名",
    "targetId": 1
  }
}
```
- **测试建议：**  
  修改后用GET接口验证。

### 5. 删除接口
- **DELETE** `/api/interfaces/3`
- **预期返回：**
```json
{
  "code": 0,
  "message": "删除成功"
}
```
- **测试建议：**  
  删除后用GET接口验证。

---

# 图片资源 Image Resource

### 1. 上传图片
- **POST** `/api/images`
- **form-data参数：**
  - file: 选择图片文件
  - type: device_front
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 17,
    "url": "/upload/xxx.jpg",
    "type": "device_front",
    "created_at": "2024-06-21 16:00:00"
  }
}
```
- **测试建议：**  
  上传后用GET接口验证。

### 2. 获取图片详情
- **GET** `/api/images/1`
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "url": "/images/room_a.jpg",
    "type": "room",
    "created_at": "2024-06-21 16:00:00"
  }
}
```
- **测试建议：**  
  用已存在的id测试。

### 3. 获取所有图片资源
- **GET** `/api/images`
- **预期返回：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "url": "/images/room_a.jpg",
      "type": "room",
      "created_at": "2024-06-21 16:00:00"
    }
  ]
}
```
- **测试建议：**  
  检查图片资源列表。

### 4. 删除图片
- **DELETE** `/api/images/1`
- **预期返回：**
```json
{
  "code": 0,
  "message": "删除成功"
}
```
- **测试建议：**  
  删除后用GET接口验证。

---

# 图标标注 Marker

### 1. 获取标注列表
- **GET** `/api/markers?parentType=device&parentId=1&imageType=front`
- **预期返回：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "parentType": "device",
      "parentId": 1,
      "imageType": "front",
      "x": 0.5,
      "y": 0.7,
      "refType": "interface",
      "refId": 2,
      "info": "主接口"
    }
  ]
}
```
- **测试建议：**  
  用不同parentType/parentId/imageType测试。

### 2. 获取单个标注点详情
- **GET** `/api/markers/1`
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "parentType": "device",
    "parentId": 1,
    "imageType": "front",
    "x": 0.5,
    "y": 0.7,
    "refType": "interface",
    "refId": 2,
    "info": "主接口"
  }
}
```
- **测试建议：**  
  用已存在的id测试。

### 3. 新增标注
- **POST** `/api/markers`
- **Body:**
```json
{
  "parentType": "device",
  "parentId": 1,
  "imageType": "front",
  "x": 0.5,
  "y": 0.7,
  "refType": "interface",
  "refId": 2,
  "info": "主接口"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 2,
    "parentType": "device",
    "parentId": 1,
    "imageType": "front",
    "x": 0.5,
    "y": 0.7,
    "refType": "interface",
    "refId": 2,
    "info": "主接口"
  }
}
```
- **测试建议：**  
  新增后用GET接口验证。

### 4. 修改标注
- **PUT** `/api/markers/2`
- **Body:**
```json
{
  "parentType": "device",
  "parentId": 1,
  "imageType": "front",
  "x": 0.6,
  "y": 0.8,
  "refType": "interface",
  "refId": 2,
  "info": "主接口-改"
}
```
- **预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 2,
    "parentType": "device",
    "parentId": 1,
    "imageType": "front",
    "x": 0.6,
    "y": 0.8,
    "refType": "interface",
    "refId": 2,
    "info": "主接口-改"
  }
}
```
- **测试建议：**  
  修改后用GET接口验证。

### 5. 删除标注
- **DELETE** `/api/markers/2`
- **预期返回：**
```json
{
  "code": 0,
  "message": "删除成功"
}
```
- **测试建议：**  
  删除后用GET接口验证。

---

## 测试建议

- 建议用Postman或Apifox批量导入测试，先测GET，再测POST/PUT/DELETE。
- 新增、修改、删除后用GET接口验证数据是否变更。
- 上传图片接口需用form-data方式，file字段选择本地图片。
- 若有外键约束，先插入父表数据再插入子表数据。

如需Postman/Apifox导入文件或更详细的断言脚本，请告知！
