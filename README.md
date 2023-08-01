# OneAPI 开放平台

> 该项目目前已经上线：<https://oneapi.charjin.top>，对应的前端项目地址在[此处](https://github.com/jinzcdev/oneapi-frontend)
>
> 管理员/普通用户：admin/user 密码：12345678

<p align="center">
  <img style="width: 95%;" src="https://raw.githubusercontent.com/jinzcdev/oneapi-backend/main/docs/imgs/oneapi.png" alt="OneAPI">
</p>

该平台包含以下模块：

1. 后台管理系统
2. 客户端 SDK
3. 公共类库
4. API 网关模块
5. 后台接口库

## 后端管理系统

后端管理系统主要为前端提供接口，实现如下功能：

普通用户：

1. OneAPI 用户登录/注册（注册后会自动分配 API 密钥）
2. 查看已有接口及用户调用情况

管理员：

1. 接口管理：创建接口、修改接口元数据、发布/下线接口
2. 接口充值：为用户充值接口调用次数
3. 接口统计：使用 Ant Design 图表库展示接口调用统计

## 客户端 SDK

客户端 SDK 提供给开发者使用，其中封装了 API 签名认证算法以简化开发者调用 API 的流程。

SDK 模块的设计花费了较长的时间，因为必须保证 **可扩展性**，即增加不同的接口时避免对原有代码的改动，因此必须对功能做抽象。

### 设计思想

客户端的整体流程：开发者创建请求对象，使用客户端发起 HTTP 请求至后端网关。

为了提高代码的**可复用性**与**可扩展性**，客户端采用面向抽象的设计模式，将客户端请求的各个环节进行抽象，形成了以下几个封装类：

1. `AbstractModel`：模型类，封装了请求参数的基本信息，通过 Gson 注解在请求的后处理中将**请求/响应参数转换为 JSON 格式**
   。实现类包含如下两种：
   - `Request`：请求对象，封装了请求的基本信息。
   - `Response`：响应对象，封装了响应的基本信息。
2. `HttpProfile`：HTTP 配置类，封装了 HTTP 请求的基本信息，如请求方法、请求地址、请求协议、请求超时时间等。
3. `ClientProfile`：客户端配置类，封装了 HttpProfile、签名方法等。
4. `AbstractClient`：客户端抽象类，封装了 ClientProfile、密钥对象、HTTP 请求方法。实现类：
   - `OneApiClient`：OneAPI 客户端实现类，封装自定义接口的请求方法。

注：在后台管理系统中，前端页面需为用户提供**在线调用**功能。为了在后端实现对 API 的动态调用，使用了**反射**
动态创建请求对象以在后端使用 SDK。

## API 网关模块

网关模块需实现的核心功能在于 **抽离出对接口鉴权、请求染色的统一控制等**，该模块在后端接口中可以使用 AOP 实现，但是当有多个后端接口服务时 AOP 就可剥离出来，使用更通用的模块去替代，因此这里使用了 API 网关实现。具体业务功能如下：

1. API 路由：只处理 `/api/**` 的接口地址
2. 使用请求头部信息实现鉴权：通过时间戳避免请求重放，对用户参数重新计算签名验证签名正确性
3. 统一接口调用的响应格式：为了提供“健壮”的服务，即使后端程序出错也要**保证返回给调用者有效的信息（错误码、错误原因等）**

改进点：这里的**请求头部验证**可以使用 Gateway 中 **Predicate** 实现，应该使用声明式设计让实现更简洁。

## 后台接口

后台接口，目前仅仅实现了两个随机头像的接口（使用了第三方），该项目目前的核心在于实现 API 平台本身，而不是提供消费级别的接口调用平台。因此可以考虑后期继续扩展。

问题思考：在设计 SDK 模块时，参考了腾讯云的 SDK 设计，发现他们在调用后端接口时候并不是以 RESTful 的标准形式，而是**统一发送到一个域名地址**，并在头部添加请求的后端功能（用 Action 命名）。我认为他们的请求中间应该会经过相应的 API 网关，但通过 Action 参数，如果有效的将其与实际的后端调用地址或方法做映射，此处还可再构思。

## 项目部署

使用 Nginx 反向代理，不对外开放端口。（请求路径应该更业务化一些，可改进）

| 模块         | 端口 | 路径 | 是否对外开放 |
| ------------ | ---- | ---- | ------------ |
| 后台管理系统 | 8101 | /api | 否           |
| API 网关模块 | 8090 | /api | 否           |
| 后台接口库   | 7089 | /api | 否           |

运行 Docker 命令：

```shell
docker run -d --name one-api -p 8101:8101 -m 2G one-api:1.0.0
```

在部署项目时，发现购买的学生云服务器内存过小，因此使用了 FRP 的内网穿透将服务部署在实验室的服务器上，通过端口映射的形式开发出去。
