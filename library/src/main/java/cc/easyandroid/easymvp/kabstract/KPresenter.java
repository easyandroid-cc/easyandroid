package cc.easyandroid.easymvp.kabstract;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cc.easyandroid.easymvp.view.ISimpleView;

public abstract class KPresenter<V extends ISimpleView<T>, T> implements Presenter<V> {
    private V iView;
    // private WeakReference<V> viewRef;
    protected final IController<T> mController = new IController<T>() {

        @Override
        public void start(Object tag) {
            onShowLoading(tag);
        }

        @Override
        public void completed(Object tag) {
            onHideLoading(tag);
        }

        @Override
        public void error(Object tag, Throwable e) {
            onHandleError(tag, e);
        }

        @Override
        public void deliverResult(Object tag, T results) {
            onDeliverResult(tag, results);
        }
    };

    @Override
    public void attachView(V view) {
        this.iView = view;
        initDeliverResultType(view);
        onAttachView();
    }


    protected boolean isViewAttached() {
        return iView != null;
    }

    public V getView() {
        return iView;
    }

    @Override
    public final void cancel() {
        onCancel();
    }

    /**
     * //检测type 是否已经赋值
     * type 的类型是否确定
     */
    boolean typeNotNull() {
        return this.mType != null;
    }


    @Override
    public void detachView() {
        if (iView != null) {
            iView = null;
        }
        onDetachView();
    }

    protected void onDetachView() {

    }

    protected void onAttachView() {

    }

    protected void onCancel() {

    }

    private void onShowLoading(Object tag) {
        if (isViewAttached())
            getView().onStart(tag);

    }

    private void onHideLoading(Object tag) {
        if (isViewAttached())
            getView().onCompleted(tag);
    }

    private void onHandleError(Object tag, Throwable e) {
        if (isViewAttached())
            getView().onError(tag, e);
    }

    private void onDeliverResult(Object tag, final T results) {
        if (isViewAttached())
            getView().deliverResult(tag, results);
    }

    private Type mType;

    public Type getDeliverResultType() {
        return mType;
    }

    public void setDeliverResultType(Type type) {
        this.mType = type;
    }

    private final int presenterId = hashCode();

    @Override
    public int getPresenterId() {
        return presenterId;
    }

    private void initDeliverResultType(V iView) {
        if (typeNotNull()) {//检测type 是否已经赋值
            return;
        }
        // 从本类的接口中查找
        Type[] interfacesTypes = iView.getClass().getGenericInterfaces();// 获取接口类型

        if (!idFind(interfacesTypes)) {
            // 从父类的接口中查找
            findSuperClass(iView.getClass());
        }

    }

    private void findSuperClass(Class<?> clazz) {
        Type superType = clazz.getGenericSuperclass();
        if (superType instanceof Class) {
            Class<?> superClazz = (Class<?>) superType;
            Type[] superclassinterfacesTypes = superClazz.getGenericInterfaces();// 获取父类中的接口
            if (!idFind(superclassinterfacesTypes)) {
                findSuperClass(superClazz);
            }
        } else if (superType instanceof ParameterizedType) {//如果父类是带泛型的类型，直接去这个泛型
            isFind(superType);
        }
    }


    private boolean idFind(Type[] interfacesTypes) {
        for (Type t : interfacesTypes) {
            if (isFind(t)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFind(Type type) {
        if (type != null && type instanceof ParameterizedType) {// 判断接口的类型是否是ParameterizedType类型，因为只有泛型的接口才是ParameterizedType的类型
            ParameterizedType parameterizedType = (ParameterizedType) type;// 泛型类型都放在ParameterizedType中
            Type rawType = parameterizedType.getRawType();// 获取泛型真实类型
            if (rawType instanceof Class) {
                Class<?> clazz = (Class<?>) rawType;
                if (ISimpleView.class.isAssignableFrom(clazz)) {// class是否是ISimpleView.class的子类
                    Type[] types = parameterizedType.getActualTypeArguments();// 获取当前接口所有的泛型类型
                    if (types != null && types.length > 0) {
                        Type guessType = types[0];// 取第一个
                        // 这里还可以进行其他判断，这个guessType有可能是一个泛型
//                        this.mType = guessType;// 取到结果直接返回，
                        setDeliverResultType(guessType);
                        return true;// -----------------找到就返回了
                    }
                }
            }
        }
        return false;
    }
}