package org.hejwo.ecommerce.products.application

import cats.effect.IO
import fs2.Stream
import fs2.concurrent.Topic
import org.hejwo.ecommerce.base.application.IOLogging
import org.hejwo.ecommerce.productServiceDto.Product
import org.hejwo.ecommerce.products.config.ProductsGeneratorConfig

class ProductsRandomGenerator(productsRandomGenerator: ProductsGeneratorConfig, productsTopic: Topic[IO, Product]) extends IOLogging {

  val mainStream: Stream[IO, Product] = Stream
    .fixedRate[IO](productsRandomGenerator.rate)
    .map(_ => ProductGenerators.randomProduct())
    .evalTap(productsTopic.publish1)

}
