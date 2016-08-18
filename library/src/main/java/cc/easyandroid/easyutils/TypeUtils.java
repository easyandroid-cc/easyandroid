package cc.easyandroid.easyutils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cc.easyandroid.easyclean.domain.easywork.EasyWorkContract;
import cc.easyandroid.easyclean.presentation.view.IEasyView;

/**
 * 通过view找出view的泛型类型
 */
public class TypeUtils {
    private Type viewType;

    public Type getViewType() {
        return viewType;
    }

    void setViewType(Type viewType) {
        this.viewType = viewType;
    }

    public <T> TypeUtils(T iView) {
        initDeliverResultType(iView);
    }

    public static <T> TypeUtils newInstance(T iView) {
        return new TypeUtils(iView);
    }

    private <T> void initDeliverResultType(T iView) {
        // 从本类的接口中查找
        Type[] interfacesTypes = iView.getClass().getGenericInterfaces();// 获取接口类型
        if (!idFind(interfacesTypes)) {
            // 从父类的接口中查找
            findSuperClass(iView.getClass());
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
                if (IEasyView.class.isAssignableFrom(clazz)) {// class是否是ISimpleView.class的子类
                    Type[] types = parameterizedType.getActualTypeArguments();// 获取当前接口所有的泛型类型
                    if (types != null && types.length > 0) {
                        Type guessType = types[0];// 取第一个
                        // 这里还可以进行其他判断，这个guessType有可能是一个泛型
                        setViewType(guessType);
                        return true;// -----------------找到就返回了
                    }
                }
            }
        }
        return false;
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

    public static void main(String[] args) {
        System.out.println("dddddddddddddddddddd");
        ChildView<List<EasyToast>> view = new ChildView<List<EasyToast>>() {
            @Override
            public void onStart(Object tag) {

            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onSuccess(Object tag, List<EasyToast> results) {

            }

        };
        System.out.println("dddddddddddddddddddd---" + TypeUtils.newInstance(view).getViewType());
    }

    public static abstract class ChildView<T> implements EasyWorkContract.View<T> {

    }
}
