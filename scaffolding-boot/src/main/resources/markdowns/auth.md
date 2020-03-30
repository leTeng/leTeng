# OAuth2.0 认证文档

## 获取令牌(token)接口

认证接口包含了多种认证方式，包括了最基本的四种认证方式。简易模式、客户端模式、密码模式、授权码模式。
并且接口提供GET或者POST两种认证方式，用户可自行选择合适自己的认证方式。

### 客户端信息认证
出于安全考虑某些接口需要客户端信息认证，需要验证的接口如下列表。

- /oauth/token  
- /oauth/authorize

推荐的方式是使用 HTTP Basic，将一下参数通过BASE64编码后的得到客户端凭证存放在请求头中去请求获取token接口，参数如下：

| 参数名称         | 描述     |
| ------------ |: -------------------------------- :|
| client_id         |      客户端 id   |
| client_secret         |客户端密码，如果客户机secret是空字符串，则客户机可以省略该参数|

例如：
```text
   假设：

     client_id = web
     client_secret = secret

   1.以`client_id:client_secret` -> `web:secret` 格式使用BASE64算法编码后得到的字符串为: “QmFzaWMgd2ViOnNlY3JldA==”
   2.设置一个头名称为"Authorization",值为"QmFzaWMgd2ViOnNlY3JldA=="的请求头去请求需要客户端认证的接口
```

### 简易模式

**接口地址** `/oauth/authorize`


**请求方式** `GET`


**consumes** `["application/json"]`


**produces** `["application/json"]`


**接口描述** `该接口是使用简易方式授权，请求方法为GET`

**请求参数**

| 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| response_type  | 表示授权类型，此处的值固定为"token" |   query    |   true   |String  |       |
| client_id  | 表示客户端的ID |   query    |   true   |String  |       |
| redirect_uri  | 表示重定向的URI |   query    |   true   |String  |       |
| scope  | 表示权限范围 |   query    |   false   |string  |       |
| state  | 表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。 |   body    |   false   |boolean  |       |
            

**请求方式** `POST`


**consumes** `["application/json"]`


**produces** `["application/json"]`


**接口描述** `该接口是使用简易方式授权，请求方法为POST`

**请求参数**

| 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| response_type  | 表示授权类型，此处的值固定为"token" |   body    |   true   |String  |       |
| client_id  | 表示客户端的ID |   body    |   true   |String  |       |
| redirect_uri  | 表示重定向的URI |   body    |   true   |String  |       |
| scope  | 表示权限范围 |   body    |   false   |String  |       |
| state  | 表示客户端的当前状态 |   body    |   false   |String  |       |

**请求示例**
````json
{
	"response_type": "token",
	"client_id": "web",
	"redirect_uri": "https://www.baidu.com",
	"scope": "",
	"state": ""
}
````            
**响应状态**

`参考统一返回状态码文档`

**响应参数**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
| access_token     | 令牌      |    string   |      |
| token_type     | 令牌类型      |    string   |       |
| expires_in     |过期时间，单位为秒      |    integer   |       |
| scope     |   权限范围      |    string   |       |
| jti     |       |    string   |       |
            

**响应示例**

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU3ODkyODQ0OSwianRpIjoiZTZjOWQwZTAtMzg5Ny00ZjI4LWE0ZDUtYzJjM2RiMmIxMWNhIiwiY2xpZW50X2lkIjoiZXRlbmcifQ.llHXoaYtBM4Daycxpcfm8ZxsYsu0aPtmH6icZOVihxA",
    "token_type": "bearer",
    "expires_in": 299,
    "scope": "read",
    "jti": "e6c9d0e0-3897-4f28-a4d5-c2c3db2b11ca"
}
```

### 客户端模式

**接口地址** `/oauth/token`


**请求方式** `GET`


**consumes** `["application/json"]`


**produces** `["application/json"]`


**接口描述** `该接口是使用客户端方式授权，请求方法为GET`

**请求参数**

| 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| grant_type  | 表示授权类型，此处的值固定为"client_credentials" |   query    |   true   |String  |       |
| scope  | 权限范围 |   query    |   false   |String  |       |
            

**请求方式** `POST`


**consumes** `["application/json"]`


**produces** `["application/json"]`


**接口描述** `该接口是使用客户端方式授权，请求方法为POST`

**请求参数**

| 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| grant_type  | 表示授权类型，此处的值固定为"client_credentials" |   body    |   true   |String  |       |
| scope  | 权限范围 |   body    |   false   |String  |       |

**请求示例**
````json
{
	"grant_type": "client_credentials",
	"scope": ""
}
````            
**响应状态**

`参考统一返回状态码文档`

**响应参数**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
| access_token     | 令牌      |    string   |      |
| token_type     | 令牌类型      |    string   |       |
| expires_in     |过期时间，单位为秒      |    integer   |       |
| scope     |   权限范围      |    string   |       |
| jti     |       |    string   |       |
            

**响应示例**

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU3ODkyODQ0OSwianRpIjoiZTZjOWQwZTAtMzg5Ny00ZjI4LWE0ZDUtYzJjM2RiMmIxMWNhIiwiY2xpZW50X2lkIjoiZXRlbmcifQ.llHXoaYtBM4Daycxpcfm8ZxsYsu0aPtmH6icZOVihxA",
    "token_type": "bearer",
    "expires_in": 299,
    "scope": "read",
    "jti": "e6c9d0e0-3897-4f28-a4d5-c2c3db2b11ca"
}
```

### 密码模式

**接口地址** `/oauth/token`


**请求方式** `GET`


**consumes** `["application/json"]`


**produces** `["application/json"]`


**接口描述** `该接口是使用密码方式授权，请求方法为GET`

**请求参数**

| 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| grant_type  | 表示授权类型，此处的值固定为"password" |   query    |   true   |String  |       |
| scope  | 权限范围 |   query    |   false   |String  |       |
| username  | 用户名 |   query    |   true   |String  |       |
| password  | 用户的密码 |   query    |   true   |String  |       |
            

**请求方式** `POST`


**consumes** `["application/json"]`


**produces** `["application/json"]`


**接口描述** `该接口是使用客户端方式授权，请求方法为POST`

**请求参数**

| 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| grant_type  | 表示授权类型，此处的值固定为"password" |   body    |   true   |String  |       |
| scope  | 权限范围 |   body    |   false   |String  |       |
| username  | 用户名 |   body    |   true   |String  |       |
| password  | 用户的密码 |   body    |   true   |String  |       |

**请求示例**
````json
{
	"grant_type": "client_credentials",
	"scope": "",
	"username": "username",
	"password": "password"
}
````            
**响应状态**

`参考统一返回状态码文档`

**响应参数**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
| access_token     | 令牌      |    string   |      |
| token_type     | 令牌类型      |    string   |       |
| refresh_token     | 刷新令牌的口令      |    string   |       |
| expires_in     |过期时间，单位为秒      |    integer   |       |
| scope     |   权限范围      |    string   |       |
| jti     |       |    string   |       |
            

**响应示例**

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5Mjk5MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiODg5NjNhY2EtNmU0YS00NWJlLWIwYTItOTVjYTgwZTZiODA1IiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl19.xYO8EfDNrLYdkw0IdLgeO7_j2pMfqLPQadZySGnhtjA",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5MzA4MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiOGM5Y2FjMWMtMzNkNy00ZDc5LTg5MjMtOTQ3NGQ3MDBjYmYzIiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl0sImF0aSI6Ijg4OTYzYWNhLTZlNGEtNDViZS1iMGEyLTk1Y2E4MGU2YjgwNSJ9.Y9sdZpetYKu-mq1IRq5x3AVCi7LmH9eobG31XzOzr0A",
    "expires_in": 299,
    "scope": "read",
    "jti": "88963aca-6e4a-45be-b0a2-95ca80e6b805"
}
```

### 授权码模式

#### 1.获取授权码

 **接口地址** `/oauth/authorize`
 
 
 **请求方式** `GET`
 
 
 **consumes** `["application/json"]`
 
 
 **produces** `["application/json"]`
 
 
 **接口描述** `该接口是获取授权码，请求方法为GET`
 
 **请求参数**
 
 | 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
 | ------------ | -------------------------------- |-----------|--------|----|--- |
 | response_type  | 表示授权类型，此处的值固定为"code" |   query    |   true   |String  |       |
 | client_id  | 表示客户端的ID |   query    |   true   |String  |       |
 | redirect_uri  | 表示重定向的URI |   query    |   true   |String  |       |
 | scope  | 表示权限范围 |   query    |   false   |string  |       |
 | state  | 表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。 |   body    |   false   |boolean  |       |
             
 
 **请求方式** `POST`
 
 
 **consumes** `["application/json"]`
 
 
 **produces** `["application/json"]`
 
 
 **接口描述** `该接口是使用简易方式授权，请求方法为POST`
 
 **请求参数**
 
 | 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
 | ------------ | -------------------------------- |-----------|--------|----|--- |
 | response_type  | 表示授权类型，此处的值固定为"code" |   body    |   true   |String  |       |
 | client_id  | 表示客户端的ID |   body    |   true   |String  |       |
 | redirect_uri  | 表示重定向的URI |   body    |   true   |String  |       |
 | scope  | 表示权限范围 |   body    |   false   |String  |       |
 | state  | 表示客户端的当前状态 |   body    |   false   |String  |       |
 
 **请求示例**
 ````json
 {
 	"response_type": "token",
 	"client_id": "web",
 	"redirect_uri": "https://www.baidu.com",
 	"scope": "",
 	"state": ""
 }
 ````            
 **响应状态**
 
 `参考统一返回状态码文档`
 
 **响应参数**
 
 | 参数名称         | 参数说明                             |    类型 |  schema |
 | ------------ | -------------------|-------|----------- |
 | access_token     | 令牌      |    string   |      |
 | token_type     | 令牌类型      |    string   |       |
 | expires_in     |过期时间，单位为秒      |    integer   |       |
 | scope     |   权限范围      |    string   |       |
 | jti     |       |    string   |       |
             
 
 **响应示例**
 
 ```json
 {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU3ODkyODQ0OSwianRpIjoiZTZjOWQwZTAtMzg5Ny00ZjI4LWE0ZDUtYzJjM2RiMmIxMWNhIiwiY2xpZW50X2lkIjoiZXRlbmcifQ.llHXoaYtBM4Daycxpcfm8ZxsYsu0aPtmH6icZOVihxA",
     "token_type": "bearer",
     "expires_in": 299,
     "scope": "read",
     "jti": "e6c9d0e0-3897-4f28-a4d5-c2c3db2b11ca"
 }
 ```
#### 2.授权码换取token

 **接口地址** `/oauth/token`
 
 
 **请求方式** `GET`
 
 
 **consumes** `["application/json"]`
 
 
 **produces** `["application/json"]`
 
 
 **接口描述** `该接口是通过授权码获取到的code来换取token，code是由调用获取code接口，由认证服务器通过用户设置的重定向地址
               请求参数上，请求方法为GET`
 
 **请求参数**
 
 | 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
 | ------------ | -------------------------------- |-----------|--------|----|--- |
 | grant_type  | 表示授权类型，此处的值固定为"authorization_code" |   query    |   true   |String  |       |
 | code  | 上一步获得的授权码 |   query    |   true   |String  |       |
 | redirect_uri  | 重定向URI，必须与获取授权码提交的重定向url地址值保持一致 |   query    |   true   |String  |       |
 | client_id  | 客户端ID |   query    |   true   |String  |       |
             
 
 **请求方式** `POST`
 
 
 **consumes** `["application/json"]`
 
 
 **produces** `["application/json"]`
 
 
 **接口描述** `该接口是使用客户端方式授权，请求方法为POST`
 
 **请求参数**
 
 | 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
 | ------------ | -------------------------------- |-----------|--------|----|--- |
 | grant_type  | 表示授权类型，此处的值固定为"authorization_code" |   body    |   true   |String  |       |
 | code  | 上一步获得的授权码 |   body    |   true   |String  |       |
 | redirect_uri  |  重定向URI，必须与获取授权码提交的重定向url地址值保持一致  |   body    |   true   |String  |       |
 | client_id  | 客户端ID |   body    |   true   |String  |       |
 
 **请求示例**
 ````json
 {
 	"grant_type": "client_credentials",
 	"scope": "",
 	"username": "username",
 	"password": "password"
 }
 ````            
 **响应状态**
 
 `参考统一返回状态码文档`
 
 **响应参数**
 
 | 参数名称         | 参数说明                             |    类型 |  schema |
 | ------------ | -------------------|-------|----------- |
 | access_token     | 令牌      |    string   |      |
 | token_type     | 令牌类型      |    string   |       |
 | refresh_token     | 刷新令牌的口令      |    string   |       |
 | expires_in     |过期时间，单位为秒      |    integer   |       |
 | scope     |   权限范围      |    string   |       |
 | jti     |       |    string   |       |
             
 
 **响应示例**
 
 ```json
 {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5Mjk5MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiODg5NjNhY2EtNmU0YS00NWJlLWIwYTItOTVjYTgwZTZiODA1IiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl19.xYO8EfDNrLYdkw0IdLgeO7_j2pMfqLPQadZySGnhtjA",
     "token_type": "bearer",
     "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5MzA4MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiOGM5Y2FjMWMtMzNkNy00ZDc5LTg5MjMtOTQ3NGQ3MDBjYmYzIiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl0sImF0aSI6Ijg4OTYzYWNhLTZlNGEtNDViZS1iMGEyLTk1Y2E4MGU2YjgwNSJ9.Y9sdZpetYKu-mq1IRq5x3AVCi7LmH9eobG31XzOzr0A",
     "expires_in": 299,
     "scope": "read",
     "jti": "88963aca-6e4a-45be-b0a2-95ca80e6b805"
 }
 ```
## 刷新token

**接口地址** `/oauth/token`
 
 
 **请求方式** `GET`
 
 
 **consumes** `["application/json"]`
 
 
 **produces** `["application/json"]`
 
 
 **接口描述** `该接口是通过授权码获取到的code来换取token，code是由调用获取code接口，由认证服务器通过用户设置的重定向地址
               请求参数上，请求方法为GET`
 
 **请求参数**
 
 | 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
 | ------------ | -------------------------------- |-----------|--------|----|--- |
 | grant_type  | 表示授权类型，此处的值固定为"refresh_token" |   query    |   true   |String  |       |
 | refresh_token  | 通过获取token接口返回的刷新token凭证 |   query    |   true   |String  |       |
 | scope  | 权限范围 |   query    |   false   |String  |       |
             
 
 **请求方式** `POST`
 
 
 **consumes** `["application/json"]`
 
 
 **produces** `["application/json"]`
 
 
 **接口描述** `该接口是使用客户端方式授权，请求方法为POST`
 
 **请求参数**
 
 | 参数名称         | 参数说明     |     请求类型 |  是否必须      |  数据类型   |  schema  |
 | ------------ | -------------------------------- |-----------|--------|----|--- |
 | grant_type  | 表示授权类型，此处的值固定为"refresh_token" |   body    |   true   |String  |       |
 | refresh_token  | 通过获取token接口返回的刷新token凭证 |   body    |   true   |String  |       |
 | scope  |  权限范围  |   body    |   true   |String  |       |
 
 **请求示例**
 ````json
 {
 	"grant_type": "refresh_token",
 	"refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5MzA4MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiOGM5Y2FjMWMtMzNkNy00ZDc5LTg5MjMtOTQ3NGQ3MDBjYmYzIiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl0sImF0aSI6Ijg4OTYzYWNhLTZlNGEtNDViZS1iMGEyLTk1Y2E4MGU2YjgwNSJ9.Y9sdZpetYKu-mq1IRq5x3AVCi7LmH9eobG31XzOzr0A",
 	"scope": ""
 }
 ````            
 **响应状态**
 
 `参考统一返回状态码文档`
 
 **响应参数**
 
 | 参数名称         | 参数说明                             |    类型 |  schema |
 | ------------ | -------------------|-------|----------- |
 | access_token     | 令牌      |    string   |      |
 | token_type     | 令牌类型      |    string   |       |
 | refresh_token     | 刷新令牌的口令      |    string   |       |
 | expires_in     |过期时间，单位为秒      |    integer   |       |
 | scope     |   权限范围      |    string   |       |
 | jti     |       |    string   |       |
             
 
 **响应示例**
 
 ```json
 {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5Mjk5MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiODg5NjNhY2EtNmU0YS00NWJlLWIwYTItOTVjYTgwZTZiODA1IiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl19.xYO8EfDNrLYdkw0IdLgeO7_j2pMfqLPQadZySGnhtjA",
     "token_type": "bearer",
     "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg5MzA4MzYsInVzZXJfbmFtZSI6IjQwMjg4MDM5NmY1NGI2MmEwMTZmNTRiNjNlM2YwMDAwIiwianRpIjoiOGM5Y2FjMWMtMzNkNy00ZDc5LTg5MjMtOTQ3NGQ3MDBjYmYzIiwiY2xpZW50X2lkIjoiZXRlbmciLCJzY29wZSI6WyJyZWFkIl0sImF0aSI6Ijg4OTYzYWNhLTZlNGEtNDViZS1iMGEyLTk1Y2E4MGU2YjgwNSJ9.Y9sdZpetYKu-mq1IRq5x3AVCi7LmH9eobG31XzOzr0A",
     "expires_in": 299,
     "scope": "read",
     "jti": "88963aca-6e4a-45be-b0a2-95ca80e6b805"
 }
 ```

## 检查token

## 获取签名秘钥


