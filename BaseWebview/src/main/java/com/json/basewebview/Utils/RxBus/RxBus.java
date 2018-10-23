package com.json.basewebview.Utils.RxBus;


import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * RxBus
 *用法:
 *      1,发送消息,已经注册过的无论sticky是否为true,都可以收到消息;
 *      2,未注册过的,发送消息,只有sticky为true,才可以收到消息;
 *      3,发送消息,注册过的收到消息,应用退到后台,再次启动,sticky为true时可以再次收到消息,需要移除注册的事件
 *      4.sticky为true时,延迟加载的页面注册之后,监听方法就会收到消息,可能会先于页面初始化
 * @author jhj 2018/6/12
 */
public class RxBus {
    private static volatile RxBus defaultInstance;

    private Map<Class, List<Disposable>> subscriptionsByEventType = new HashMap<>();


    private Map<Object, List<Class>> eventTypesBySubscriber = new HashMap<>();


    private Map<Class, List<SubscriberMethod>> subscriberMethodByEventType = new HashMap<>();

    /*stick数据*/
    private final Map<Class<?>, Object> stickyEvent =new ConcurrentHashMap<>();

    // 主题
    private final Subject bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    // 单例RxBus
    public static RxBus get() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 提供了一个新的事件,单一类型
     *
     * @param o 事件数据
     */
    public void post(Object o) {
        synchronized (stickyEvent) {
            stickyEvent.put(o.getClass(), o);
        }
        bus.onNext(o);
    }


    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 提供了一个新的事件,根据code进行分发
     *
     * @param code 事件code
     * @param o
     */
    public void post(int code, Object o) {
        bus.onNext(new Message(code, o));
    }


    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param code      事件code
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
        return bus.ofType(Message.class)
                .filter(new Predicate<Message>(){
                    @Override
                    public boolean test(Message message) throws Exception {
                        return message.getCode() == code && eventType.isInstance(message.getObject());
                    }
                }).map(new Function<Message, Object>() {
                    @Override
                    public Object apply(Message message) throws Exception {
                        return message.getObject();
                    }
                }).cast(eventType);
    }


    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (stickyEvent) {
            Observable<T> observable = bus.ofType(eventType);
            final Object event = stickyEvent.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>(){
                    @Override
                    public void subscribe(ObservableEmitter<T> e) throws Exception {
                        e.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }



    /**
     * 注册
     *
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
          /*避免重复创建*/
        if(eventTypesBySubscriber.containsKey(subscriber)){
            return;
        }
        Class<?> subClass = subscriber.getClass();
        Method[] methods = subClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                //获得参数类型
                Class[] parameterType = method.getParameterTypes();
                //参数不为空 且参数个数为1
                if (parameterType != null && parameterType.length == 1) {

                    Class eventType = parameterType[0];

                    addEventTypeToMap(subscriber, eventType);
                    Subscribe sub = method.getAnnotation(Subscribe.class);
                    int code = sub.code();
                    ThreadMode threadMode = sub.threadMode();
                    boolean sticky = sub.sticky();

                    SubscriberMethod subscriberMethod = new SubscriberMethod(subscriber, method, eventType, code, threadMode,
                            sticky);

                    if (isAdd(eventType, subscriberMethod)) {
                        addSubscriberToMap(eventType, subscriberMethod);
                        addSubscriber(subscriberMethod);
                    }
                }
            }
        }
    }


    /**
     * 检查是否已经添加过sub事件
     * @param eventType
     * @param subscriberMethod
     * @return
     */
    private boolean isAdd(Class eventType,SubscriberMethod subscriberMethod){
        boolean resulte=true;
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if(subscriberMethods!=null&&subscriberMethods.size()>0){
            for (SubscriberMethod subscriberMethod1 : subscriberMethods) {
                if(subscriberMethod1.code==subscriberMethod.code&&subscriberMethod.subscriber==subscriberMethod1.subscriber){
                    resulte=false;
                }
                if(subscriberMethod1.eventType==subscriberMethod.eventType&&subscriberMethod.subscriber==subscriberMethod1.subscriber){
                    resulte=false;
                }
            }
        }
        return resulte;
    }


    /**
     * 将event的类型以订阅中subscriber为key保存到map里
     *
     * @param subscriber 订阅者
     * @param eventType  event类型
     */
    private void addEventTypeToMap(Object subscriber, Class eventType) {
        List<Class> eventTypes = eventTypesBySubscriber.get(subscriber);
        if (eventTypes == null) {
            eventTypes = new ArrayList<>();
            eventTypesBySubscriber.put(subscriber, eventTypes);
        }

        if (!eventTypes.contains(eventType)) {
            eventTypes.add(eventType);
        }
    }

    /**
     * 将注解方法信息以event类型为key保存到map中
     *
     * @param eventType        event类型
     * @param subscriberMethod 注解方法信息
     */
    private void addSubscriberToMap(Class eventType, SubscriberMethod subscriberMethod) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods == null) {
            subscriberMethods = new ArrayList<>();
            subscriberMethodByEventType.put(eventType, subscriberMethods);
        }

        if (!subscriberMethods.contains(subscriberMethod)) {
            subscriberMethods.add(subscriberMethod);
        }
    }


    /**
     * 将订阅事件以event类型为key保存到map,用于取消订阅时用
     *
     * @param eventType    event类型
     * @param disposable 订阅事件
     */
    private void addSubscriptionToMap(Class eventType, Disposable disposable) {
        List<Disposable> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new ArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        }

        if (!subscriptions.contains(disposable)) {
            subscriptions.add(disposable);
        }
    }


    /**
     * 用RxJava添加订阅者
     *
     * @param subscriberMethod
     */
    public void addSubscriber(final SubscriberMethod subscriberMethod) {
        Observable observable;
        if(subscriberMethod.sticky){
            observable=toObservableSticky(subscriberMethod.eventType);
        }else{
            if (subscriberMethod.code == -1) {
                observable = toObservable(subscriberMethod.eventType);
            } else {
                observable = toObservable(subscriberMethod.code, subscriberMethod.eventType);
            }
        }
        Disposable disposable = postToObservable(observable, subscriberMethod)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        callEvent(subscriberMethod.code, o);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("tag", "error--->" + throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
        addSubscriptionToMap(subscriberMethod.eventType, disposable);
    }


    /**
     * 用于处理订阅事件在那个线程中执行
     *
     * @param observable
     * @param subscriberMethod
     * @return
     */
    private Observable postToObservable(Observable observable, SubscriberMethod subscriberMethod) {

        switch (subscriberMethod.threadMode) {
            case MAIN:
                observable.observeOn(AndroidSchedulers.mainThread());
                break;
            case NEW_THREAD:
                observable.observeOn(Schedulers.newThread());
                break;
            case CURRENT_THREAD:
                observable.observeOn(Schedulers.trampoline());
                break;
            case IO:
                observable.observeOn(Schedulers.io());
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return observable;
    }


    /**
     * 回调到订阅者的方法中
     *
     * @param code   code
     * @param object obj
     */
    private void callEvent(int code, Object object) {
        Class eventClass = object.getClass();
        List<SubscriberMethod> methods = subscriberMethodByEventType.get(eventClass);
        if (methods != null && methods.size() > 0) {
            for (SubscriberMethod subscriberMethod : methods) {

                Subscribe sub = subscriberMethod.method.getAnnotation(Subscribe.class);
                int c = sub.code();
                if (c == code) {
                    subscriberMethod.invoke(object);
                }

            }
        }
    }


    /**
     * 取消注册
     *
     * @param subscriber
     */
    public void unRegister(Object subscriber) {
        List<Class> subscribedTypes = eventTypesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unSubscribeByEventType(eventType);
                unSubscribeMethodByEventType(subscriber, eventType);
            }
            eventTypesBySubscriber.remove(subscriber);
        }
    }


    /**
     * subscriptions unsubscribe
     *
     * @param eventType
     */
    private void unSubscribeByEventType(Class eventType) {
        List<Disposable> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            Iterator<Disposable> iterator = subscriptions.iterator();
            while (iterator.hasNext()) {
                Disposable subscription = iterator.next();
                if (subscription != null && !subscription.isDisposed()) {
                    subscription.dispose();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除subscriber对应的subscriberMethods
     *
     * @param subscriber
     * @param eventType
     */
    private void unSubscribeMethodByEventType(Object subscriber, Class eventType) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods != null) {
            Iterator<SubscriberMethod> iterator = subscriberMethods.iterator();
            while (iterator.hasNext()) {
                SubscriberMethod subscriberMethod = iterator.next();
                if (subscriberMethod.subscriber.equals(subscriber)) {
                    iterator.remove();
                }
            }
        }
    }


    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEvent) {
            return eventType.cast(stickyEvent.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (stickyEvent) {
            stickyEvent.clear();
        }
    }

}
