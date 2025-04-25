### 限流速率器使用

`@FrequencyControl`

此文档主要用于声明如何使用，至于为什么要用限流速率器，有什么用，麻烦自行百度。

注解式限流速率器主要是方便在控制层上使用，考虑到大部分的限流都是对接口进行限流，
所以说使用了注解式。

注解中的属性使用方式

- `key`使用的时候需要配合`KeyType`来使用。通过key获取限流速率器。

  - 默认是空字符串，并且KeyType类型为KEY。此种情况是默认的情况，建议用户为限流速率器设置key名称

  - KeyType类型为EL，可以解析`SpringEL`表达式，说明是对于入参进行解析，确保唯一携带方法的全限定名作为前缀进行拼接。一旦使用了EL模式，key = 方法完全限定名 + EL表达式。EL表达式可以解析入参，比如
    ```java
    @FrequencyControl(key = "'id: ' + #id", intervalTimes = 1,rate = 3, type = ControlType.DEFAULT)
    public void requestRateLimiterEl(@RequestParam(value = "id") String id) {}
    ```

  - IP：可以对于IP进行限流，默认只是取IP地址作为key。

- `intervalTimes`是指定时间范围内 ， 时间单位需要配合 unit属性来使用，默认是1秒

- `rate`指定时间内的点击次数，默认是1

- `exceptionClass`默认的异常枚举，如果请求上限，抛出异常，你可以指定异常抛出的类型

- `message`指定异常抛出后的消息

- `type `是指限流策略，默认是`DEFAULT`，误差比较大，使用的是固定窗口。要想误差小一些，可以使用滑动窗口`SLIDING_WINDOW`类型。令牌桶目前未实现。

你可以根据你想要的方式定义。如果范围时间内超过了请求的次数，就会抛出异常，拒绝访问。
