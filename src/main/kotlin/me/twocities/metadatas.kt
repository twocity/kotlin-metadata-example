package me.twocities

import me.twocities.KotlinClassProto.Constructor
import me.twocities.KotlinClassProto.Function
import me.twocities.KotlinClassProto.Kind
import me.twocities.KotlinClassProto.Kind.ANNOTATION_CLASS
import me.twocities.KotlinClassProto.Kind.CLASS
import me.twocities.KotlinClassProto.Kind.COMPANION_OBJECT
import me.twocities.KotlinClassProto.Kind.ENUM_CLASS
import me.twocities.KotlinClassProto.Kind.ENUM_ENTRY
import me.twocities.KotlinClassProto.Kind.INTERFACE
import me.twocities.KotlinClassProto.Kind.OBJECT
import me.twocities.KotlinClassProto.MemberKind
import me.twocities.KotlinClassProto.MemberKind.DECLARATION
import me.twocities.KotlinClassProto.MemberKind.FAKE_OVERRIDE
import me.twocities.KotlinClassProto.MemberKind.SYNTHESIZED
import me.twocities.KotlinClassProto.Modality
import me.twocities.KotlinClassProto.Modality.ABSTRACT
import me.twocities.KotlinClassProto.Modality.FINAL
import me.twocities.KotlinClassProto.Modality.OPEN
import me.twocities.KotlinClassProto.Modality.SEALED
import me.twocities.KotlinClassProto.Property
import me.twocities.KotlinClassProto.Visibility
import me.twocities.KotlinClassProto.Visibility.INTERNAL
import me.twocities.KotlinClassProto.Visibility.LOCAL
import me.twocities.KotlinClassProto.Visibility.PRIVATE
import me.twocities.KotlinClassProto.Visibility.PRIVATE_TO_THIS
import me.twocities.KotlinClassProto.Visibility.PROTECTED
import me.twocities.KotlinClassProto.Visibility.PUBLIC
import org.jetbrains.kotlin.serialization.ClassData
import org.jetbrains.kotlin.serialization.Flags
import org.jetbrains.kotlin.serialization.ProtoBuf

internal fun ClassData.toClassMetadata(): KotlinClassProto {
  return KotlinClassProto(
      kind = classProto.kind(),
      modality = classProto.modality(),
      visibility = classProto.visibility(),
      hasAnnotations = classProto.hasAnnotations(),
      isInner = classProto.isInner(),
      isData = classProto.isDataClass(),
      constructors = classProto.constructorList.map {
        Constructor(
            hasAnnotations = it.hasAnnotations(),
            isSecondary = it.isSecondary(),
            visibility = it.visibility()
        )
      }.toList(),
      properties = classProto.propertyList.map {
        Property(
            visibility = it.visibility(),
            modality = it.modality(),
            memberKind = it.memberKind(),
            isVal = it.isVal(),
            isVar = it.isVar(),
            isLateInit = it.isLateInit(),
            nullable = it.isNullable(),
            name = nameResolver.getString(it.name)
        )
      }.toList(),
      functions = classProto.functionList.map {
        Function(
            hasAnnotations = it.hasAnnotations(),
            isOperator = it.isOperator(),
            isInfix = it.isInfix(),
            isInline = it.isInline(),
            isTailrec = it.isTailrec(),
            isExternal = it.isExternal(),
            visibility = it.visibility(),
            modality = it.modality(),
            memberKind = it.memberKind(),
            name = nameResolver.getString(it.name)
        )
      }.toList()
  )
}

// class
internal fun ProtoBuf.Class.modality(): Modality = flagsToModality(flags)

internal fun ProtoBuf.Class.visibility(): Visibility = flagsToVisibility(flags)
internal fun ProtoBuf.Class.hasAnnotations(): Boolean = Flags.HAS_ANNOTATIONS.get(flags)
internal fun ProtoBuf.Class.isInner(): Boolean = Flags.IS_INNER.get(flags)
internal fun ProtoBuf.Class.isDataClass(): Boolean = Flags.IS_DATA.get(flags)

// constructor
internal fun ProtoBuf.Constructor.visibility(): Visibility = flagsToVisibility(flags)

internal fun ProtoBuf.Constructor.hasAnnotations(): Boolean = Flags.HAS_ANNOTATIONS.get(flags)
internal fun ProtoBuf.Constructor.isSecondary(): Boolean = Flags.IS_SECONDARY.get(flags)

// function
internal fun ProtoBuf.Function.hasAnnotations(): Boolean = Flags.HAS_ANNOTATIONS.get(flags)

internal fun ProtoBuf.Function.isOperator(): Boolean = Flags.IS_OPERATOR.get(flags)
internal fun ProtoBuf.Function.isInfix(): Boolean = Flags.IS_INFIX.get(flags)
internal fun ProtoBuf.Function.isInline(): Boolean = Flags.IS_INLINE.get(flags)
internal fun ProtoBuf.Function.isTailrec(): Boolean = Flags.IS_TAILREC.get(flags)
internal fun ProtoBuf.Function.isExternal(): Boolean = Flags.IS_EXTERNAL_FUNCTION.get(flags)
internal fun ProtoBuf.Function.visibility(): Visibility = flagsToVisibility(flags)
internal fun ProtoBuf.Function.modality(): Modality = flagsToModality(flags)
internal fun ProtoBuf.Function.memberKind(): MemberKind = flagsToMemberKind(flags)

// property
internal fun ProtoBuf.Property.visibility(): Visibility = flagsToVisibility(flags)

internal fun ProtoBuf.Property.modality(): Modality = flagsToModality(flags)
internal fun ProtoBuf.Property.memberKind(): MemberKind = flagsToMemberKind(flags)
internal fun ProtoBuf.Property.isVar(): Boolean = Flags.IS_VAR.get(flags)
internal fun ProtoBuf.Property.isVal(): Boolean = !isVar()
internal fun ProtoBuf.Property.isLateInit(): Boolean = Flags.IS_LATEINIT.get(flags)
internal fun ProtoBuf.Property.isNullable(): Boolean = returnType.hasNullable()

private fun ProtoBuf.Class.kind(): Kind {
  val kind = Flags.CLASS_KIND.get(flags)
  @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
  when (kind) {
    ProtoBuf.Class.Kind.CLASS -> return CLASS
    ProtoBuf.Class.Kind.INTERFACE -> return INTERFACE
    ProtoBuf.Class.Kind.ENUM_CLASS -> return ENUM_CLASS
    ProtoBuf.Class.Kind.ENUM_ENTRY -> return ENUM_ENTRY
    ProtoBuf.Class.Kind.ANNOTATION_CLASS -> return ANNOTATION_CLASS
    ProtoBuf.Class.Kind.OBJECT -> return OBJECT
    ProtoBuf.Class.Kind.COMPANION_OBJECT -> return COMPANION_OBJECT
  }
}

private fun flagsToVisibility(flags: Int): Visibility {
  @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
  when (Flags.VISIBILITY.get(flags)) {
    ProtoBuf.Visibility.INTERNAL -> return INTERNAL
    ProtoBuf.Visibility.PRIVATE -> return PRIVATE
    ProtoBuf.Visibility.PROTECTED -> return PROTECTED
    ProtoBuf.Visibility.PUBLIC -> return PUBLIC
    ProtoBuf.Visibility.PRIVATE_TO_THIS -> return PRIVATE_TO_THIS
    ProtoBuf.Visibility.LOCAL -> return LOCAL
  }
}

private fun flagsToModality(flags: Int): Modality {
  @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
  when (Flags.MODALITY.get(flags)) {
    ProtoBuf.Modality.FINAL -> return FINAL
    ProtoBuf.Modality.OPEN -> return OPEN
    ProtoBuf.Modality.ABSTRACT -> return ABSTRACT
    ProtoBuf.Modality.SEALED -> return SEALED
  }
}

private fun flagsToMemberKind(flags: Int): MemberKind {
  @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
  when (Flags.MEMBER_KIND.get(flags)) {
    ProtoBuf.MemberKind.DECLARATION -> return DECLARATION
    ProtoBuf.MemberKind.FAKE_OVERRIDE -> return FAKE_OVERRIDE
    ProtoBuf.MemberKind.DELEGATION -> return DECLARATION
    ProtoBuf.MemberKind.SYNTHESIZED -> return SYNTHESIZED
  }
}
