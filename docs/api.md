\# 智游云境 API 接口文档



\## 基础信息

\- \*\*基础URL\*\*: `http://localhost:3000/api`

\- \*\*默认编码\*\*: UTF-8

\- \*\*认证方式\*\*: JWT Token (后续添加)



\## 数据结构

\### 用户 (User)

```json

{

&nbsp; "id": "string",

&nbsp; "username": "string",

&nbsp; "email": "string",

&nbsp; "preferences": {

&nbsp;   "uiMode": "normal" | "elderly",

&nbsp;   "fontSize": "medium" | "large",

&nbsp;   "voiceSpeed": "normal" | "slow"

&nbsp; }

}

\### 景点 (ScenicSpot)

```json

{

&nbsp; "id": "string",

&nbsp; "name": "string",

&nbsp; "description": "string",

&nbsp; "modelUrl": "string?",

&nbsp; "panoramaImages": "string\[]?"

}

