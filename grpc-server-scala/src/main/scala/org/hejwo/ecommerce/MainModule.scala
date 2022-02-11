package org.hejwo.ecommerce

import org.hejwo.ecommerce.base.config.AppConfig
import org.hejwo.ecommerce.products.ProductsModule
import org.hejwo.ecommerce.support.SupportModule
import pureconfig.ConfigSource
import pureconfig.generic.auto._

trait MainModule extends SupportModule with ProductsModule {
  val appConfig: AppConfig = ConfigSource
    .resources("application.conf")
    .withFallback(ConfigSource.default)
    .loadOrThrow[AppConfig]

}
