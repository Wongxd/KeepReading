package com.wongxd.w_gank.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wxd1 on 2017/4/18.
 */

public class GankBean implements Serializable {


    /**
     * error : false
     * results : [{"_id":"58f5d527421aa9544b77400f","createdAt":"2017-04-18T16:58:15.238Z","desc":"LingoRecord is a better recorder for Android, you can easily process pcm data from it.","publishedAt":"2017-04-19T11:44:51.925Z","source":"web","type":"Android","url":"https://github.com/lingochamp/LingoRecorder","used":true,"who":"Echo"},{"_id":"58f5e7d5421aa954511ebeba","createdAt":"2017-04-18T18:17:57.834Z","desc":"Understanding Context In Android Application","publishedAt":"2017-04-19T11:44:51.925Z","source":"web","type":"Android","url":"https://blog.mindorks.com/understanding-context-in-android-application-330913e32514","used":true,"who":"AMIT SHEKHAR"},{"_id":"58f5ecd7421aa954511ebebb","createdAt":"2017-04-18T18:39:19.747Z","desc":"仿iOS输入法点击输入框以外区域 自动隐藏软键盘","images":["http://img.gank.io/fa038d13-9cf7-4ed4-8a74-17b40c7c3a52"],"publishedAt":"2017-04-19T11:44:51.925Z","source":"web","type":"Android","url":"https://github.com/yingLanNull/HideKeyboard","used":true,"who":"yingLan"},{"_id":"58f60cc8421aa9544b774014","createdAt":"2017-04-18T20:55:36.879Z","desc":"可自定义动画的卡片切换视图","images":["http://img.gank.io/b9db68a2-aea5-412e-9897-8260ef2b592c"],"publishedAt":"2017-04-19T11:44:51.925Z","source":"web","type":"Android","url":"https://github.com/BakerJQ/Android-InfiniteCards","used":true,"who":"BakerJ"},{"_id":"58f62d3a421aa9544b774017","createdAt":"2017-04-18T23:14:02.368Z","desc":"实现仿 Retrofit 的跳转路由","images":["http://img.gank.io/ef0992b9-ed3a-412e-9d7a-4779e96bde99"],"publishedAt":"2017-04-19T11:44:51.925Z","source":"web","type":"Android","url":"https://github.com/nekocode/Meepo","used":true,"who":"nekocode"},{"_id":"58f6bd3b421aa9544ed88978","createdAt":"2017-04-19T09:28:27.377Z","desc":"Facebook推出的可声明式构建高效UI的库。","publishedAt":"2017-04-19T11:44:51.925Z","source":"web","type":"Android","url":"https://github.com/facebook/litho","used":true,"who":"JohnTsai"},{"_id":"58f6d891421aa9544825f8af","createdAt":"2017-04-19T11:25:05.920Z","desc":"非常规矩的一款 Bottom 导航效果组件","images":["http://img.gank.io/ca1b2f29-39f6-4e95-8300-f7984d2042ca"],"publishedAt":"2017-04-19T11:44:51.925Z","source":"chrome","type":"Android","url":"https://github.com/adib2149/BottomNavBar","used":true,"who":"代码家"},{"_id":"58f6d8cb421aa9544ed8897d","createdAt":"2017-04-19T11:26:03.655Z","desc":"跟随手势滑动，显示隐藏标题栏、底部导航栏及悬浮按钮的 Android Behavior Library。","images":["http://img.gank.io/c728c395-0574-4dab-8496-cd785b20474a"],"publishedAt":"2017-04-19T11:44:51.925Z","source":"chrome","type":"Android","url":"https://github.com/Lauzy/LBehavior","used":true,"who":"allen"},{"_id":"58f57e35421aa9544b774003","createdAt":"2017-04-18T10:47:17.437Z","desc":"Android 多边形绘制组件，做的很漂亮哦~ 用来做动态图表会很有帮助，同时可以学习他的实现。","images":["http://img.gank.io/5b728c1a-5685-4517-adc9-e1590e462097"],"publishedAt":"2017-04-18T11:34:29.203Z","source":"chrome","type":"Android","url":"https://github.com/stkent/PolygonDrawingUtil","used":true,"who":"代码家"},{"_id":"58f57e9f421aa9544b774005","createdAt":"2017-04-18T10:49:03.981Z","desc":"RxJava3 预览版 Demo","publishedAt":"2017-04-18T11:34:29.203Z","source":"chrome","type":"Android","url":"https://github.com/akarnokd/RxJava3-preview","used":true,"who":"带马甲"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Serializable{
        /**
         * _id : 58f5d527421aa9544b77400f
         * createdAt : 2017-04-18T16:58:15.238Z
         * desc : LingoRecord is a better recorder for Android, you can easily process pcm data from it.
         * publishedAt : 2017-04-19T11:44:51.925Z
         * source : web
         * type : Android
         * url : https://github.com/lingochamp/LingoRecorder
         * used : true
         * who : Echo
         * images : ["http://img.gank.io/fa038d13-9cf7-4ed4-8a74-17b40c7c3a52"]
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
