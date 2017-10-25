package me.twocities

import kotlin.reflect.KClass


object MetadataFactory {
  private val metadataFqName = "kotlin.Metadata"

  fun create(klass: KClass<*>): KMetadata {
    val annotation = klass.java.declaredAnnotations.singleOrNull { it.annotationClass.java.name == metadataFqName } ?: throw IllegalArgumentException(
        "${klass.qualifiedName} has no @$metadataFqName")
    return KMetadataImpl(annotation)
  }


  private class KMetadataImpl(private val instance: Annotation) : KMetadata {
    private val klass = instance.annotationClass.java

    override fun k(): Int {
      return klass.getDeclaredMethod("k").invoke(instance) as Int
    }

    override fun mv(): IntArray {
      return klass.getDeclaredMethod("mv").invoke(instance) as IntArray
    }

    override fun bv(): IntArray {
      return klass.getDeclaredMethod("bv").invoke(instance) as IntArray
    }

    override fun xs(): String {
      return klass.getDeclaredMethod("xs").invoke(instance) as String
    }

    override fun xi(): Int {
      return klass.getDeclaredMethod("xi").invoke(instance) as Int
    }

    @Suppress("UNCHECKED_CAST")
    override fun d1(): Array<String> {
      return klass.getDeclaredMethod("d1").invoke(instance) as Array<String>
    }

    @Suppress("UNCHECKED_CAST")
    override fun d2(): Array<String> {
      return klass.getDeclaredMethod("d2").invoke(instance) as Array<String>
    }

  }
}