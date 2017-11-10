#美恰Glide相关，按照它的官方配置
#不然 vovi ，联想手机会crash：必须在你的 build.gradle 文件中配置「Glide、Picasso、universal-image-loader、XUtils3」中的某一个图片加载库的依赖,或者检查是否添加了图库的混淆配置
#其他手机暂时没发现

-keep class com.bumptech.glide.Glide { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.**