package cc.easyandroid.easyutils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cc.easyandroid.easymvp.view.ISimpleView;

/**
 * Created by Administrator on 2016/6/6.
 */
public class TypeUtils {

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

    boolean typeNotNull() {
        return this.mType != null;
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
