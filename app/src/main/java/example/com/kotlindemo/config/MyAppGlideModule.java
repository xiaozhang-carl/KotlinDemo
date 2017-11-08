package example.com.kotlindemo.config;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by jinliangshan on 17/2/13.
 * Glide 全局配置, 包括缓存策略、Https等。。<br/>
 * <p>
 * <a href="https://github.com/bumptech/glide/wiki/Configuration">文档</a>
 * <p>
 * <a href="https://github.com/bumptech/glide/issues/305>使用 ARGB_8888 防止图片过度压缩时变绿</a>
 */


@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
}