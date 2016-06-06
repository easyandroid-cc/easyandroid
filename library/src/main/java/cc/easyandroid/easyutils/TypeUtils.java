package cc.easyandroid.easyutils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpContract;
import cc.easyandroid.easymvp.view.ISimpleView;

/**
 *
 */
public class TypeUtils {
    Type viewType;

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
                if (EasyHttpContract.View.class.isAssignableFrom(clazz)) {// class是否是ISimpleView.class的子类
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
}
