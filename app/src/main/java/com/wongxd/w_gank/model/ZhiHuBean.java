package com.wongxd.w_gank.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wxd1 on 2017/4/7.
 */

public class ZhiHuBean implements Serializable {

    /**
     * date : 20170407
     * stories : [{"images":["https://pic2.zhimg.com/v2-96b62d39ab1c644b97a4b34a1d1be8f9.jpg"],"type":0,"id":9340188,"ga_prefix":"040711","title":"用科学来解释「一夜白头」，其实是这么回事"},{"images":["https://pic1.zhimg.com/v2-70052b77d28b4406898db59a69105964.jpg"],"type":0,"id":9341354,"ga_prefix":"040710","title":"什么样的老师才是好老师？"},{"images":["https://pic2.zhimg.com/v2-c66392e788b4fb101229cf3b2abb6071.jpg"],"type":0,"id":9341571,"ga_prefix":"040709","title":"买房合同都签了，房主又要涨价，你有 3 种办法"},{"images":["https://pic3.zhimg.com/v2-f273c04e244b43a592b632596bffb9fa.jpg"],"type":0,"id":9341357,"ga_prefix":"040708","title":"技术进步真的会减少岗位数量吗？"},{"images":["https://pic3.zhimg.com/v2-1ab80f77b7636ad5622628c50628381a.jpg"],"type":0,"id":9340511,"ga_prefix":"040707","title":"有了这份手册，完全不会日语也能在日本点菜"},{"images":["https://pic1.zhimg.com/v2-0b9a275633035bfbd9a8d8bf7fdc7864.jpg"],"type":0,"id":9340652,"ga_prefix":"040707","title":"特斯拉和谷歌搞自动驾驶，传统车企当然不会坐以待毙"},{"title":"《三体 3》入围的雨果奖，跟很多熟悉的电影美剧都有关系","ga_prefix":"040707","images":["https://pic4.zhimg.com/v2-4ab5de5c76cef87bb2dee4ce2a32aa33.jpg"],"multipic":true,"type":0,"id":9340644},{"images":["https://pic3.zhimg.com/v2-5c1d6baee92afd490672df88be385fd6.jpg"],"type":0,"id":9339802,"ga_prefix":"040706","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic3.zhimg.com/v2-43172adf4d6a931ec0cf0e045c95bb0e.jpg","type":0,"id":9341354,"ga_prefix":"040710","title":"什么样的老师才是好老师？"},{"image":"https://pic3.zhimg.com/v2-048b0f11937d50d380cf4e7263120a8a.jpg","type":0,"id":9340511,"ga_prefix":"040707","title":"有了这份手册，完全不会日语也能在日本点菜"},{"image":"https://pic2.zhimg.com/v2-8a77b3b20b5e92c574821b32dab6f7e5.jpg","type":0,"id":9340644,"ga_prefix":"040707","title":"《三体 3》入围的雨果奖，跟很多熟悉的电影美剧都有关系"},{"image":"https://pic1.zhimg.com/v2-ce45d7f0bf5139e33a1c2198146d2984.jpg","type":0,"id":9340102,"ga_prefix":"040614","title":"人工智能要和人类打德州扑克，这场比赛注定很精彩"},{"image":"https://pic3.zhimg.com/v2-87c968324465e4bfab1495162bed1af2.jpg","type":0,"id":9338552,"ga_prefix":"040608","title":"全球只有 7 人完成过，这是比攀登珠峰更令人惊叹的探险"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean implements Serializable{
        /**
         * images : ["https://pic2.zhimg.com/v2-96b62d39ab1c644b97a4b34a1d1be8f9.jpg"]
         * type : 0
         * id : 9340188
         * ga_prefix : 040711
         * title : 用科学来解释「一夜白头」，其实是这么回事
         * multipic : true
         */

        private int headId=-1;     //组id

        public int getHeadId() {
            return headId;
        }

        public void setHeadId(int headId) {
            this.headId = headId;
        }

        private String date;//时间

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean implements Serializable{
        /**
         * image : https://pic3.zhimg.com/v2-43172adf4d6a931ec0cf0e045c95bb0e.jpg
         * type : 0
         * id : 9341354
         * ga_prefix : 040710
         * title : 什么样的老师才是好老师？
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
