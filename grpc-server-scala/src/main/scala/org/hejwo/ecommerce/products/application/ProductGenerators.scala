package org.hejwo.ecommerce.products.application

import org.hejwo.ecommerce.productServiceDto.Product
import org.scalacheck.Gen

object ProductGenerators {
  private val productNames = Seq(
    "Laptop Dell Zoom",
    "Android Alt",
    "Xiaomi Prime",
    "Dell Connect",
    "Samsung Blink",
    "iPhone Veritas",
    "Sony Savvy",
    "HP Acuter XL",
    "Mazda 3",
    "Mazda 6",
    "iPhone Lift 11 PRO",
    "Xiaomi DoorFrame",
    "Dell Dash",
    "Android Recover",
    "Lattice Board",
    "Boeing 247",
    "Samsung Deluxe",
    "Samsung Elevate",
    "Xiaomi Omega Y21",
    "Linux Ubuntu"
  )

  def strGen(n: Int): Gen[String] = Gen.listOfN(n, Gen.alphaChar).map(_.mkString)

  def sampleProduct(): Gen[Product] =
    for {
      id <- Gen.uuid.map(_.toString)
      name <- Gen.oneOf(productNames)
      description <- strGen(10)
    } yield Product(id, name, description)

  def randomProduct(): Product = sampleProduct().sample.get

}
