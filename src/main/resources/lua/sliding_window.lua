-- KEYS[1]: 滑动窗口的 Redis Key
-- ARGV[1]: 当前时间戳（毫秒）
-- ARGV[2]: 窗口大小（毫秒）
-- ARGV[3]: 最大请求数

local key = KEYS[1]
local now = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])
local min = now - window

-- 移除窗口之外的请求
redis.call('ZREMRANGEBYSCORE', key, 0, min)

-- 获取当前窗口内的请求数量
local currentCount = redis.call('ZCARD', key)

if currentCount < limit then
    -- 允许通过，添加当前请求
    redis.call('ZADD', key, now, tostring(now) .. "-" .. tostring(math.random()))
    redis.call('PEXPIRE', key, window)
    return 1
else
    -- 拒绝请求
    return 0
end
