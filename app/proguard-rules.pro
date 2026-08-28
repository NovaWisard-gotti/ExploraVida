# ExploraVida - reglas ProGuard
-keep class com.educalab.exploravida.data.local.entity.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { public *; }
