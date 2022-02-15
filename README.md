# scala-fs2-grpc-examples
This repository presents usage of `sbt-fs2-grpc` plugin.  
It contains 3 subprojects:


## grpc-server-scala  

gRCP server with various usages of FS2, like:  
- generating random products at constant pace `org.hejwo.ecommerce.products.application.ProductsRandomGenerator` 
- usage of FS2 `Topic` to demonstrate pub/sub possibilities `org.hejwo.ecommerce.products.ProductsModule`
- `ProductService` contains various service methods from unary to both client/server streaming

## grpc-server-protobuf
- all protobufs needed to generate both server and client

## grpc-client-scala  
- client application connection with previously mentioned server  


#### TODOs  
1. Prometheus metrics need to be implemented on server side  
2. Client is not invoking all server methods. 
