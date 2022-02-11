package org.hejwo.ecommerce.products

import cats.effect.kernel.Fiber
import cats.effect.{IO, Resource}
import fs2._
import fs2.concurrent.Topic
import io.grpc.ServerServiceDefinition
import org.hejwo.ecommerce.base.application.IOLogging
import org.hejwo.ecommerce.infrastructure.cache.Cache
import org.hejwo.ecommerce.productService.ProductServiceFs2Grpc
import org.hejwo.ecommerce.productServiceDto.Product
import org.hejwo.ecommerce.products.application.ProductsRandomGenerator
import org.hejwo.ecommerce.products.config.ProductsModuleConfig

trait ProductsModule extends IOLogging {
  def productsModuleConfig: ProductsModuleConfig

  lazy val productCache = new Cache[String, Product]

  val productServiceGrpcStream: Stream[IO, ServerServiceDefinition] = for {
    productsTopic <- Stream.resource(Resource.eval(Topic[IO, Product]))
    productsGenerator = new ProductsRandomGenerator(productsModuleConfig.productsGenerator, productsTopic)
    _ <- productsGenerator.mainStream.spawn
    _ <- productLogger(productsTopic)
    productService = new ProductService(productCache, productsTopic)
    serviceDefinition <- Stream.resource(ProductServiceFs2Grpc.bindServiceResource[IO](productService))
  } yield serviceDefinition

  def productLogger(productsTopic: Topic[IO, Product]): Stream[IO, Fiber[IO, Throwable, Unit]] =
    productsTopic
      .subscribe(4)
      .evalTap(emittedProduct => logger.info(s"Got product: '$emittedProduct'"))
      .spawn

}
