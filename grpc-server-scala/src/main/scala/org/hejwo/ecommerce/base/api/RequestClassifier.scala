//package org.hejwo.ecommerce.base.api
//
//import cats.effect.IO
//import org.http4s.Request
//
//object RequestClassifier {
//
//  def requestLabel(request: Request[IO]): Option[String] =
//    Some(requiredRequestLabel(request))
//
//  def requiredRequestLabel(request: Request[IO]): String =
//    s"${request.method.name}-${request.uri.path}"
//}
