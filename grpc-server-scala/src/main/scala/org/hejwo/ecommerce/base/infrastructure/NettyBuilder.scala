package org.hejwo.ecommerce.base.infrastructure

import io.grpc.ServerServiceDefinition
import io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService

object NettyBuilder {

  def apply(serverPort: Int)(service: Seq[ServerServiceDefinition]): NettyServerBuilder = {
    val nettyBase = NettyServerBuilder
      .forPort(serverPort)
      .addService(ProtoReflectionService.newInstance())

    service.foreach(s => nettyBase.addService(s))
    nettyBase
  }

}
