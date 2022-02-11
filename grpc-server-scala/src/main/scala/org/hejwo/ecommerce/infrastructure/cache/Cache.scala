package org.hejwo.ecommerce.infrastructure.cache

import cats.effect.IO
import com.github.benmanes.caffeine.cache.Caffeine
import scalacache.caffeine.CaffeineCache
import scalacache.Entry

class Cache[K <: AnyRef, V] {

  private val underlyingCaffeineCache = Caffeine.newBuilder().maximumSize(10000L).build[K, Entry[V]]
  private val cache: CaffeineCache[IO, K, V] = CaffeineCache[IO, K, V](underlyingCaffeineCache)

  def put(key: K)(value: V): IO[Unit] = cache.put(key)(value)

  def get(key: K): IO[Option[V]] = cache.get(key)

}
