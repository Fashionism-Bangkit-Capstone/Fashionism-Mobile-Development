# Keep line numbers so release crash stack traces stay readable (map them back with mapping.txt).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Gson (via Retrofit) maps JSON by field name through reflection, and these classes
# have no @SerializedName, so their fields must not be renamed or removed.
-keep class com.fashionism.fashionismuserapp.data.model.** { *; }
-keep class com.fashionism.fashionismuserapp.data.remote.request.** { *; }
-keep class com.fashionism.fashionismuserapp.data.remote.response.** { *; }

# Retrofit, OkHttp, Gson, Glide and Lottie ship their own consumer rules.
