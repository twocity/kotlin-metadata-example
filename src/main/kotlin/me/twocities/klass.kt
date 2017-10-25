package me.twocities

import org.jetbrains.kotlin.serialization.jvm.JvmProtoBufUtil
import kotlin.reflect.KClass

fun KClass<*>.classProto(): KotlinClassProto {
  val metadata = loadMetadata()
  val classData = JvmProtoBufUtil.readClassDataFrom(metadata.d1(), metadata.d2())
  return classData.toClassMetadata()
}

fun KClass<*>.loadMetadata(): KMetadata {
  return MetadataFactory.create(this)
}

interface KMetadata {
  fun k(): Int
  fun mv(): IntArray
  fun bv(): IntArray
  fun xs(): String
  fun xi(): Int
  fun d1(): Array<String>
  fun d2(): Array<String>
}