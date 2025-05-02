# RedisCache 
Redis的缓存组件
## RedisCacheable
参考Spring的Spring Cacheable所作，虽然没有Cacheable中的EL表达式那么强大。主要是封装并实现了最常用的部分。
该注解的核心思想就是：执行方法后，将最后返回的结果缓存，下次查询的时候会先查询缓存中的数据。
可以查询一下Spring Cacheable
下面是对于注解中的属性的解释
###  key & keyType
先说下KeyType  Key的生成策略，目前有两种key的生成策略
缓存的键，如果KeyType不设置，默认就是加在的方法上的所有参数拼接

如果使用EL形式，就是Spring的EL表达式

###  expireTime & unit
搭配使用，默认是5分钟，unit默认单位为 `TimeUnit.MILLISECONDS`

### nullType
缓存为空的策略，默认是短期缓存空数据（null值），
如果不喜欢这个策略可以使用DEFAULT 如果缓存为空不做任何操作
### randomTime
防止缓存雪崩的随机值，时间单位和unit保持一致

### autoPrefixKey
是否需要拼接方法的完全限定名，作为前缀名。默认是开启的！

## RedisCacheEvict
这里的Evict目前和Spring中的暂时不一致。
> 不过将方法执行结果删除的要求已经提上日程，正在制作中。 √

这里注解的作用就是：考虑到延时双删的痛点就是无法很好的确定延迟时间，所以说可能会带来脏数据，一致性要求的保证不高。
为了解决这个问题，可以通过在注解上直接配置延迟时间的方式，简化延迟时间策略的配置。提高了方案的使用方式。

## key
要删除的键，这里是必填的
## delay & unit
用户自定义延迟多久时间需要二次删除，配合单位使用

## type
三种生成key的类型，同@RedisCacheable注解的keyType属性，见上
> 新增第三种注解类型 RESULT

`RESULT`如果你对于方法的返回题，想要作为key来进行删除，参考SpringCachevict，也推出这个功能。