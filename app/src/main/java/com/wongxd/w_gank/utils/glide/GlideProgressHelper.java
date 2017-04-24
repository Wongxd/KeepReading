package com.wongxd.w_gank.utils.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by wxd1 on 2017/4/24.
 * <p>
 * Glide.with(MainActivity.this).using(
 * new GlideProgressHelper.ProgressModelLoader(new GlideProgressHelper.ImageProgressListener() {
 * <p>@Override public void onProgress(int progress, long readLen, long total) {
 * Log.e("progress", progress + "");
 * }
 * }))
 * .load("http://image2.sina.com.cn/dy/o/2004-11-10/1100077821_2laygS.jpg")
 * .diskCacheStrategy(DiskCacheStrategy.NONE).into(progressImageView.getImageView());
 *
 */
public class GlideProgressHelper {

    public interface ImageProgressListener {
        /**
         * @param progress 0-100
         * @param readLen
         * @param total
         */
        void onProgress(int progress, long readLen, long total);
    }

    public static class ProgressModelLoader implements StreamModelLoader<String> {

        private ImageProgressListener imageProgressListener;

        public ProgressModelLoader(ImageProgressListener listener) {
            this.imageProgressListener = listener;
        }

        @Override
        public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
            return new ProgressDataFetcher(model, imageProgressListener);
        }


        /**
         * Created by chenpengfei on 2016/11/9.
         */
        public static class ProgressDataFetcher implements DataFetcher<InputStream> {

            private String url;
            private ImageProgressListener listener;
            private Call progressCall;
            private InputStream stream;
            private boolean isCancelled;

            public ProgressDataFetcher(String url, ImageProgressListener listener) {
                this.url = url;
                this.listener = listener;
            }

            @Override
            public InputStream loadData(Priority priority) throws Exception {
                Request request = new Request.Builder().url(url).build();
                OkHttpClient client = new OkHttpClient();
                client.interceptors().add(new ProgressInterceptor(getProgressListener()));
                try {
                    progressCall = client.newCall(request);
                    Response response = progressCall.execute();
                    if (isCancelled) {
                        return null;
                    }
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    stream = response.body().byteStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return stream;
            }

            private ProgressListener getProgressListener() {
                ProgressListener progressListener = new ProgressListener() {

                    @Override
                    public void progress(long bytesRead, long contentLength, boolean done) {
                        if (listener != null && !done) {
                            int progress = (int) (bytesRead * 100 / contentLength);
                            listener.onProgress(progress, bytesRead, contentLength);
                        }
                    }
                };
                return progressListener;
            }

            @Override
            public void cleanup() {
                if (stream != null) {
                    try {
                        stream.close();
                        stream = null;
                    } catch (IOException e) {
                        stream = null;
                    }
                }
                if (progressCall != null) {
                    progressCall.cancel();
                }
            }

            @Override
            public String getId() {
                return url;
            }

            @Override
            public void cancel() {
                isCancelled = true;
            }


            /**
             * Created by chenpengfei on 2016/11/9.
             */
            public static class ProgressInterceptor implements Interceptor {

                private ProgressListener progressListener;

                public ProgressInterceptor(ProgressListener progressListener) {
                    this.progressListener = progressListener;
                }

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
                }


                /**
                 * Created by chenpengfei on 2016/11/9.
                 */
                public static class ProgressResponseBody extends ResponseBody {

                    private ResponseBody responseBody;
                    private ProgressListener progressListener;
                    private BufferedSource bufferedSource;

                    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
                        this.responseBody = responseBody;
                        this.progressListener = progressListener;
                    }

                    @Override
                    public MediaType contentType() {
                        return responseBody.contentType();
                    }

                    @Override
                    public long contentLength() {
                        try {
                            return responseBody.contentLength();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        if (bufferedSource == null) {
                            try {
                                bufferedSource = Okio.buffer(source(responseBody.source()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return bufferedSource;
                    }

                    private Source source(Source source) {
                        return new ForwardingSource(source) {

                            long totalBytesRead = 0;

                            @Override
                            public long read(Buffer sink, long byteCount) throws IOException {
                                long bytesRead = super.read(sink, byteCount);
                                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                                if (progressListener != null)
                                    progressListener.progress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                                return bytesRead;
                            }
                        };
                    }
                }
            }
        }


    }

    public interface ProgressListener {

        void progress(long bytesRead, long contentLength, boolean done);

    }

}
