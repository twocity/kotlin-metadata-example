package me.twocities

data class KotlinClassProto(
    val kind: Kind,
    val modality: Modality,
    val visibility: Visibility,
    val hasAnnotations: Boolean,
    val isInner: Boolean,
    val isData: Boolean,
    val constructors: List<Constructor>,
    val properties: List<Property>,
    val functions: List<Function>
) {
  data class Constructor(val hasAnnotations: Boolean,
      val isSecondary: Boolean,
      val visibility: Visibility
  )

  data class Property(
      val visibility: Visibility,
      val modality: Modality,
      val memberKind: MemberKind,
      val isVar: Boolean,
      val isVal: Boolean,
      val isLateInit: Boolean,
      val nullable: Boolean,
      val name: String
  )

  data class Function(
      val hasAnnotations: Boolean,
      val isOperator: Boolean,
      val isInfix: Boolean,
      val isInline: Boolean,
      val isTailrec: Boolean,
      val isExternal: Boolean,
      val visibility: Visibility,
      val modality: Modality,
      val memberKind: MemberKind,
      val name: String
  )

  enum class Kind {
    CLASS,
    INTERFACE,
    ENUM_CLASS,
    ENUM_ENTRY,
    ANNOTATION_CLASS,
    OBJECT,
    COMPANION_OBJECT
  }

  enum class Visibility {
    INTERNAL, PRIVATE,
    PROTECTED, PUBLIC,
    PRIVATE_TO_THIS,
    LOCAL
  }

  enum class Modality {
    FINAL,
    OPEN,
    ABSTRACT,
    SEALED
  }

  enum class MemberKind {
    DECLARATION,
    FAKE_OVERRIDE,
    DELEGATION,
    SYNTHESIZED
  }
}