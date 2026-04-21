-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes *Annotation*
-keepclassmembers class kotlin.Metadata { *; }

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject <fields>;
}

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keep @kotlinx.serialization.Serializable class * { *; }
