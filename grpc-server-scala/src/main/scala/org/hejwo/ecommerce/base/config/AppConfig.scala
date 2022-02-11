package org.hejwo.ecommerce.base.config

import org.hejwo.ecommerce.products.config.ProductsModuleConfig

case class AppConfig(
    grpc: GrpcServerConfig,
    productsModule: ProductsModuleConfig
)
