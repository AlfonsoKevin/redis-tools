<p align="center">
    <a href="" target="_blank">
      <img src="./imgs/cover.png" width="280" />
    </a>
</p>
<h1 align="center">Redis-Tools</h1>
<p align="center"><strong>致力于简化Redis工具的使用，提供丰富的多种多样的组件和功能<br><em>持续更新中～</em></strong></p>
<div align="center">
    <a href="https://github.com/AlfonsoKevin/redis-tools"><img src="https://img.shields.io/badge/github-项目地址-yellow.svg?style=plasticr"></a></div>



---



## 介绍

---

🚀 专为高效开发打造的Redis工具库 | 基于Redisson深度封装 | 让分布式开发更优雅

## 目录结构&设计🚀

```txt
├─📄 README.md                         # 文档说明
└─📁 src
  └─📁 main
    └─📁 java
      └─📁 io
        └─📁 github
          └─📁 alfonsokevin
            └─📁 core
              ├─📁 base                # 基础包
              │ ├─📁 constants         # 项目中的常量类
              │ ├─📁 docs              # 文档说明
              │ ├─📁 exception         # 统一工具异常类
              │ ├─📁 opt               # 基础操作类
              │ └─📁 utils             # 其他的工具类
              ├─📁 cache               # 缓存包
              ├─📁 config              # 配置包
              └─📁 limiter             # 限流包
```



### ✨详细介绍 & 核心价值

作为平日开发中的一员，提炼出**高频使用但实现繁琐**的Redis操作场景，结合Redisson的优秀特性进行二次封装，为您带来：

- **🔌 无缝Spring集成** - 注解驱动配置，与Spring Boot生态完美契合
- **💡 开箱即用的最佳实践** - Redisson的限流速率器的注解式实现/功能扩展...
- **⚡ 性能优化**-静态缓存构造器等预先调优

#### 😎 目前实现的功能
- 基于Redisson的速率限流器，可自定义异常信息，自定义Key的策略(自定义key/SpringEL表达式解析)，目前提供了默认的限流算法进行计算，
后期将完善其他的限流算法和进一步扩展。[使用文档](./src/main/java/io/github/alfonsokevin/core/limiter/docs/Freq.md)
- Redis的构建key的封装。[使用文档](./src/main/java/io/github/alfonsokevin/core/base/docs/base.md)
- Redis的基本操作类，工具类，这个都比较常见 `DefaultRedisOperations`,`RedisKeyUtils`[使用文档](./src/main/java/io/github/alfonsokevin/core/base/docs/base.md)
- 提供了SpringEL表达式的工具类 `SpELUtils`[使用文档](./src/main/java/io/github/alfonsokevin/core/base/docs/base.md)
- 提供了基于Redis的基本工具类`DefaultRedisOperations`，规范接口，可以自定义扩展的实现[使用文档](./src/main/java/io/github/alfonsokevin/core/base/docs/base.md)
- 提供了`RedisCacheable`注解，注解参考SpringCacheable实现了功能，轻量。提供了查询key为null之后的处理策略[使用文档](./src/main/java/io/github/alfonsokevin/core/cache/docs/cache.md)
- 提供了`RedisCacheEvict注解`,注解可以设置延时时长，极大程度提高了原有延时双删的使用[使用文档](./src/main/java/io/github/alfonsokevin/core/cache/docs/cache.md)
- 提供了工具类的统一异常类，`AbstractRedisToolsException`，抛出组件异常的时候可以使用具体的组件子类[使用文档](./src/main/java/io/github/alfonsokevin/core/base/docs/base.md)

### 🚀快速开始

等待将依赖推送到中央仓库。正确配置即可使用。

1.引入依赖

```xml
<dependency>
    <groupId>io.github.alfonsokevin</groupId>
    <artifactId>redis-tools</artifactId>
    <version>1.3.0-RELEASE</version>
</dependency>
```

2.环境中配置redis的配置 (查看RedisToolsProperties)

```yml
redis:
  tools:
    config:
      host: ${redis.tools.config.host}
      port: ${redis.tools.config.port}
      password: ${redis.tools.config.password}
```

3.Spring 默认**不会自动扫描外部 jar 包中的类**，需要在引导类中指定包路径

```java
@SpringBootApplication(scanBasePackages = {"io.github.alfonsokevin","io.xxx.*"})
```

### 🤖最近更新
- 2025/5/02 对evict组件新增对方法的结果删除的策略，修复该组件的bug，将原有fastjson替换为fastjson2，版本是1.3.1-RELEASE
- 2025/4/28 为新增组件完善文档
- 2025/4/28 推出`@RedisCacheEvict`注解，发布到中央仓库。并且统一指定了工具类异常，每个的组件异常和状态码。使用1.3.0-RELEASE
- 2025/4/27 推出`@RedisCacheable`注解，发布到中央仓库，使用1.2.0-RELEASE;
- 2025/4/25 补充对于IP的key生成策略，对IP限流。补充速率限流器为文档;
- 2025/4/25 将注解属性封装为实体，方便调用;
- 2025/4/24 发布到中央仓库，补充Redis基础工具类(很少一部分，期待后续完善)，规范日志输出，调整Bean名称;
- 2025/4/24 Debug: 原配置的 `@ConditionalOnClass(RedissonClient.class)` 条件错误，导致自动配置仅在`Redisson` 已存在时触发。若项目没有将会抛出异常;
- 2025/4/24 Debug: 自定义Bean名称后未修改完全使得会出现NPE;
### 待办清单 TODO
- Redis其他组件..
- 添加额外参数，并判断是否要拼接prefixkey
- 完善限流策略的不同算法补充..
- 完善文档，补充部分组件的压测

### 注意事项

- This project is licensed under the MIT + Commons Clause – see the [LICENSE](LICENSE) file for details.
- 欢迎提出建议，您可以通过提出 Issue 的方式参与到项目中来。
- 目前项目没有经过大量测试，有些许不足之处，仍处于开发阶段，如果发现了bug，可以提出issue，感谢你的支持。

## 参与贡献

如果您对项目有任何建议或想要贡献代码，欢迎提交 Issue 。

---

感谢您对该项目的关注和支持！🕵️‍♀️
