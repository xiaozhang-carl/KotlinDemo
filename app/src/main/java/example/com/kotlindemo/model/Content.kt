package example.com.kotlindemo.model

import java.io.Serializable

/**
 * Created by zhanghongqiang on 2017/11/9 下午2:38
 * ToDo:
 */

class Content : Serializable {

    /**
     * pic_urls : [{"image":"http://image.dongdianyun.com:4869/a4b21279aeea964d889c43983466d183","thumbnail_pic":"http://image.dongdianyun.com:4869/a4b21279aeea964d889c43983466d183?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/73690f51881d026474ed6dc51843afb9","thumbnail_pic":"http://image.dongdianyun.com:4869/73690f51881d026474ed6dc51843afb9?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/95b6356a10e010b7c6605655c53f5f52","thumbnail_pic":"http://image.dongdianyun.com:4869/95b6356a10e010b7c6605655c53f5f52?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/44f82fbacf4e5f52e09e1ff5e1118fcc","thumbnail_pic":"http://image.dongdianyun.com:4869/44f82fbacf4e5f52e09e1ff5e1118fcc?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/aca7badb68b64387a525f2e11b5624f4","thumbnail_pic":"http://image.dongdianyun.com:4869/aca7badb68b64387a525f2e11b5624f4?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/f1e14bb9d0cf0b6f9dec1d729deac3f4","thumbnail_pic":"http://image.dongdianyun.com:4869/f1e14bb9d0cf0b6f9dec1d729deac3f4?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/ca173ea4812e4009813d1bbf45a55097","thumbnail_pic":"http://image.dongdianyun.com:4869/ca173ea4812e4009813d1bbf45a55097?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/0eeedb41db23721961cf9f13472566a7","thumbnail_pic":"http://image.dongdianyun.com:4869/0eeedb41db23721961cf9f13472566a7?w=200&h=200"},{"image":"http://image.dongdianyun.com:4869/cf33fc2d68659f5002d3f43416013faf","thumbnail_pic":"http://image.dongdianyun.com:4869/cf33fc2d68659f5002d3f43416013faf?w=200&h=200"}]
     * text : 人家的孩子天天晒娇，你看红红的孩子主动做家务，这就是不同的教育阿。
     */

    var text: String? = null
    var video: Video? = null
    var pic_urls: List<PicUrls>? = null

    class PicUrls : Serializable{
        /**
         * image : http://image.dongdianyun.com:4869/a4b21279aeea964d889c43983466d183
         * thumbnail_pic : http://image.dongdianyun.com:4869/a4b21279aeea964d889c43983466d183?w=200&h=200
         */

        var image: String? = null
        var thumbnail_pic: String? = null
    }

    class Video : Serializable{
        /**
         * image : http://image.dongdianyun.com:4869/a4b21279aeea964d889c43983466d183
         * thumbnail_pic : http://image.dongdianyun.com:4869/a4b21279aeea964d889c43983466d183?w=200&h=200
         */

        var fileId: String? = null
        var groupName: String? = null
        var url: String? = null
        var thumbnail_pic: String? = ""
    }
}
