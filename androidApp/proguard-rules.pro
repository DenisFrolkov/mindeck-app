-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes *Annotation*
-keepclassmembers class kotlin.Metadata { *; }

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

-keep class * extends androidx.room.RoomDatabase { <init>(); }

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keep @kotlinx.serialization.Serializable class * { *; }
