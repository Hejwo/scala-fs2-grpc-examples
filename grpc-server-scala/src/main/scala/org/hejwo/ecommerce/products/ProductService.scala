package org.hejwo.ecommerce.products

import cats.effect.IO
import fs2._
import fs2.concurrent.Topic
import io.grpc.Metadata
import org.hejwo.ecommerce.base.application.IOLogging
import org.hejwo.ecommerce.infrastructure.cache.Cache
import org.hejwo.ecommerce.productService.ProductServiceFs2Grpc
import org.hejwo.ecommerce.productServiceDto._

import scala.util.control.NoStackTrace

class ProductService(productCache: Cache[String, Product], productsTopic: Topic[IO, Product])
    extends ProductServiceFs2Grpc[IO, Metadata]
    with IOLogging {

  override def addProduct(request: Product, ctx: Metadata): IO[ProductID] =
    for {
      _ <- productCache.put(request.id)(request)
      newProduct <- IO.pure(ProductID(request.id))
      _ <- productsTopic.publish1(request)
    } yield newProduct

  override def getProduct(request: ProductID, ctx: Metadata): IO[Product] =
    productCache.get(request.value).flatMap {
      case Some(value) => IO.pure(value)
      case None        => IO.raiseError(new ProductServiceException("No product! :/"))
    }

  override def subscribeToProducts(request: ProductsSubscriber, ctx: Metadata): Stream[IO, Product] =
    Stream.eval(logger.info(s"New subscriber joined! appId: '${request.appId}'")) >>
      productsTopic
        .subscribe(10)
        .take(request.howMuch)
        .onFinalizeCase(exitCase => logger.info(s"subscribeToProducts ended. Reason: $exitCase"))

  override def addProductsBatch(request: Stream[IO, Product], ctx: Metadata): IO[BatchResponse] =
    request
      .evalMap(addProduct(_, ctx))
      .onFinalizeCase(exitCase => logger.info(s"addProductsBatch ended. Reason: $exitCase"))
      .compile
      .toList
      .map(BatchResponse(_))

  override def addProductsStream(request: Stream[IO, Product], ctx: Metadata): Stream[IO, ProductID] =
    request
      .evalMap(addProduct(_, ctx))
      .onFinalizeCase(exitCase => logger.info(s"addProductsStream ended. Reason: $exitCase"))
}

class ProductServiceException(whatHappened: String) extends Exception with NoStackTrace
