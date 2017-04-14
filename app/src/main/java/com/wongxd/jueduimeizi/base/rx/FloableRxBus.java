package com.wongxd.jueduimeizi.base.rx;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 *  有背压处理的 RxBus
 */
public class FloableRxBus {

    private final FlowableProcessor<Object> mBus;

    private FloableRxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create().toSerialized();
    }

    // 单例RxBus
    public static FloableRxBus getDefault() {
        return Holder.RX_BUS;
    }


    /**
     * 提供了一个新的事件,单一类型
     *
     * @param obj 事件数据
     */
    public void post(Object obj) {
        mBus.onNext(obj);
    }


    /**
     * 提供了一个新的事件,根据code进行分发
     *
     * @param code 事件code
     * @param o
     */
    public void post(int code, Object o) {
        mBus.onNext(new Message(code, o));
    }


    /***
     * 获取被观察者
     *
     * @return
     */
    public Flowable<Object> toFlowable() {
        return mBus;
    }


    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param tClass 事件类型
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }



    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param code      事件code
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(final int code, final Class<T> eventType) {
        return mBus.ofType(Message.class)
                .filter(new Predicate<Message>() {
                    @Override
                    public boolean test(@NonNull Message message) throws Exception {
                        return message.getCode() == code;
                    }
                })
                .map(new Function<Message, Object>() {
                    @Override
                    public Object apply(@NonNull Message message) throws Exception {
                        return message.getObject();
                    }
                })
                .cast(eventType);
    }


    /**
     * 判断是否有订阅者
     *
     * @return
     */
    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }


    private static class Holder {
        private static final FloableRxBus RX_BUS = new FloableRxBus();
    }
}