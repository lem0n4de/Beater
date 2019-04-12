package com.lem0n.common.communicators

/**
 * Created by lem0n on 12/04/19.
 */
class DuplicateKeysException(val msg : String? = null) : Exception(msg)

class NonOverridingHashMap<K, V> : HashMap<K, V>() {
    @Throws(DuplicateKeysException::class)
    override fun put(key: K, value: V): V? {
        if (containsKey(key)) {
            throw DuplicateKeysException()
        }
        return super.put(key, value)
    }

    @Throws(DuplicateKeysException::class)
    override fun putAll(from: Map<out K, V>) {
        from.forEach { key, _ ->
            if (containsKey(key)) {
                throw DuplicateKeysException()
            }
        }
        super.putAll(from)
    }
}