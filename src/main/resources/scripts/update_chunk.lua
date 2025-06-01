-- KEYS[1]: 状态键 (e.g., "upload:chunks:file123:status")
-- ARGV[1]: 分片索引 (从0开始)

local name = KEYS[1]
local statusKey = KEYS[2]

local index = ARGV[1]  -- 转为Lua下标
local expireSeconds = tonumber(ARGV[2]) or 0
local chunkIndex = tonumber(index)
--
-- 参数校验
if not chunkIndex then
    return redis.error_reply("CHUNK_INDEX_MUST_BE_A_NUMBER")
end
-- 1. 参数校验
if chunkIndex < 1 then
    return redis.error_reply("INVALID_CHUNK_INDEX")
end

-- 2. 获取当前状态
local current = redis.call('GET', statusKey) or ""

-- 3. 智能扩容（一次性补足长度）
if #current < chunkIndex then
    -- 一次性补充缺失的0（比循环拼接高效）
    current = current .. string.rep("0", chunkIndex - #current)
end

-- 4. 安全更新（处理最后一个字符的情况）
local updated
if chunkIndex <= #current then
    updated = string.sub(current, 1, chunkIndex-1) ..
            "1" ..
            (chunkIndex <= #current and string.sub(current, chunkIndex+1) or "")
else
    -- 理论上不会走到这里（因为前面已扩容）
    updated = current .. "1"
end

-- 5. 写回Redis
-- 写回并设置过期时间
redis.call('SET', statusKey, updated)
if expireSeconds > 0 then
    redis.call('EXPIRE', statusKey, expireSeconds)
    redis.call('EXPIRE', name, expireSeconds)
end


return 1