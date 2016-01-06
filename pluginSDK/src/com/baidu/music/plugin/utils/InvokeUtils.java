package com.baidu.music.plugin.utils;

import com.baidu.music.plugin.clientlog.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarlene on 5/21 0021
 */
public class InvokeUtils {

    /**
     * 反射调用无参数的方法
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeMethod(Object obj, String methodName){
        return invokeMethod(obj.getClass(), obj, methodName);
    }

    /**
     * 反射调用无参数的方法
     * @param clazz
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeMethod(Class<?> clazz, Object obj, String methodName){
        return invokeMethod(clazz, obj, methodName, new Class[]{}, new Object[]{});
    }

    /**
     * 反射调用带参数方法
     * @param obj
     * @param methodName
     * @param valueType
     * @param values
     * @return
     */
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] valueType, Object[] values) {
        return invokeMethod(obj.getClass(), obj, methodName, valueType, values);
    }

    /**
     * 反射调用带参数的方法
     * @param clazz
     * @param obj
     * @param methodName
     * @param valueType
     * @param values
     * @return
     */
    public static Object invokeMethod(Class<?> clazz, Object obj, String methodName, Class<?>[] valueType, Object[] values) {
        return invokeMethodImpl(clazz, obj, methodName, valueType, values);
    }

    /**
     * 递归找方法进行invoke
     * @param clazz
     * @param obj
     * @param methodName
     * @param valueType
     * @param values
     * @return
     */
    private static Object invokeMethodImpl(Class<?> clazz, Object obj, String methodName, Class<?>[] valueType, Object[] values) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, valueType);
            method.setAccessible(true);
            return method.invoke(obj, values);
        } catch (NoSuchMethodException e) {
            if(clazz.getSuperclass() != null) {
                invokeMethodImpl(clazz.getSuperclass(), obj, methodName, valueType, values);
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射获取对象中得Field Object
     * @param clazz
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getObjectFieldValue(Class<?> clazz, Object obj, String fieldName){
		try {
			 Field field = clazz.getDeclaredField(fieldName);
			 field.setAccessible(true);
	         return field.get(obj);
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
            //如果存在父类，递归查找父类中的变量
			 if(clazz.getSuperclass() != null) {
				 getObjectFieldValue(clazz.getSuperclass(), obj, fieldName);
			 } else {
	             e1.printStackTrace();
	         }
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
    }
    
    /**
     * 通过反射获取对象中得Field int
     * @param clazz
     * @param obj
     * @param fieldName
     * @return
     */
    public static int getIntFieldValue(Class<?> clazz, Object obj, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获得类中的Field
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getObjectField(Class<?> clazz, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置Field中得对象
     * @param clazz
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setObjectField(Class<?> clazz, Object obj, String fieldName, Object value){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e) {
            //如果存在父类，递归查找父类中的变量
        	if (clazz.getSuperclass() != null) {
        		setObjectField(clazz.getSuperclass(),obj,fieldName,value);
        	} else {
        		e.printStackTrace();
        	}
        } catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * 获得Field Type
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Class<?> getObjectType(Class<?> clazz, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造新对象
     * 不带参数
     * @param clazz
     * @return
     */
    public static Object newInstanceObject(Class<?> clazz){
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造新对象
     * 带参数
     * @param clazz
     * @return
     */
    public static Object newInstanceObject(Class<?> clazz, Class<?>[] valueType, Object[] values) {
        try {
            return clazz.getConstructor(valueType).newInstance(values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定参数个数的方法。
     * @param clazz
     * @param methodName
     * @param paramCnt
     * @return
     */
    public static Method getClassMethodWithParamCnt(Class<?> clazz, String methodName, int paramCnt) {
        Method methods[] = clazz.getDeclaredMethods();
        for(Method method: methods){
            String name = method.getName();
            Class<?> types[] = method.getParameterTypes();
            int typeCnt = 0;
            if(types != null){
                typeCnt = types.length;
            }
            if(name.equalsIgnoreCase(methodName) && typeCnt == paramCnt){
                return method;
            }
        }
        return null;
    }

    /**
     * 获取方法（可能包含多个）
     * @param clazz
     * @param methodName
     * @return
     */
    public static List<Method> getClassMethod(Class<?> clazz, String methodName) {
        Method methods[] = clazz.getDeclaredMethods();
        List<Method> list= new ArrayList<Method>();
        for(Method method: methods){
            String name = method.getName();
            if (name.equalsIgnoreCase(methodName)) {
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 获取方法的参数个数
     * @param clazz
     * @param methodName
     * @return
     */
    public static int getClassMethodParamCount(Class<?> clazz, String methodName) {
    	int typeCnt = 0;
    	 Method methods[] = clazz.getDeclaredMethods();
    	 for(Method method: methods) { 
             String name = method.getName();
             if (name.equalsIgnoreCase(methodName)) {
            	 Class<?> types[] = method.getParameterTypes();
            	 typeCnt = types.length;
            	 StringBuffer buffer = new StringBuffer();
            	 buffer.append("method: " + name + "(");
            	 for(int j = 0; j < typeCnt; j++) {
            		 if (j == typeCnt-1) {
            			 buffer.append(types[j].getName());
            		 } else {
            			 buffer.append(types[j].getName() + ",");
            		 }
                 }
            	 buffer.append(")");
                 LogUtil.v(clazz.getSimpleName(), buffer.toString() + " ["+methodName+" have "+typeCnt+" params.]" );
             }
    	 }
    	 return typeCnt;
    }

    /**
     * 打印类中方法，以及方法的参数
     * @param clazz
     */
    public static void printMethod(Class<?> clazz){
        Method methods[] = clazz.getDeclaredMethods();
        for(Method method: methods){
            StringBuffer buffer = new StringBuffer();
            String name = method.getName();
            Class<?> types[] = method.getParameterTypes();
            buffer.append("method: " + name + "(");
            int typeCnt = types.length;
            for(int j = 0; j < typeCnt; j++) {
       		 if (j == typeCnt-1) {
       			 buffer.append(types[j].getName());
       		 } else {
       			 buffer.append(types[j].getName() + ",");
       		 }
            }
            buffer.append(")");
            LogUtil.v(clazz.getSimpleName(), buffer.toString());
        }
    }

    /**
     * 打印类中的成员变量。
     * @param clazz
     */
    public static void printField(Class<?> clazz) {
        Field fields[] = clazz.getDeclaredFields();
        for (Field field:fields) {
            String name = field.getName();
            LogUtil.v(clazz.getSimpleName(), "field: " + name);
        }
    }

}
