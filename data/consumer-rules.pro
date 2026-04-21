-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Database class * { *; }
-keepclassmembers @androidx.room.Entity class * { *; }

-keepclassmembers class * {
    @androidx.room.TypeConverter <methods>;
}
