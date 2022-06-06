package com.cas.other;

import com.cas.bean.User;
import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:25 2021/3/17
 * @version: V1.0
 * @review:
 */

interface UserMapper {
    @Select("SELECT * from user where id = #{id} and name = #{name}")
    List<User> selectUserList(Integer id, String name);
}

public class Application {

    public static void main(String[] args) {

        UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(Application.class.getClassLoader(), new Class<?>[]{UserMapper.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Select annotation = method.getAnnotation(Select.class);
                Map<String, Object> nameMap = buildMethodArgNameMap(method, args);
                if (annotation != null) {
                    String[] value = annotation.value();
                    String sql = value[0];
                    sql = parseSQL(sql, nameMap);
                    System.out.println(sql);
                }
                return null;
            }
        });
        userMapper.selectUserList(1, "xl");

    }


    public static String parseSQL(String sql, Map<String, Object> nameArgMap) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = sql.length();
        for (int i = 0; i < length; i ++) {
            char c = sql.charAt(i);
            if (c == '#') {
                int nextIndex = i + 1;
                char nextChar = sql.charAt(nextIndex);
                if (nextChar != '{') {
                    throw new RuntimeException(String.format("这里应该为#{\nsql:%s\nindex:%d",
                            stringBuilder.toString(), nextIndex));
                }
                // 获取参数名
                StringBuilder argSB = new StringBuilder();
                i = parseSQLArg(argSB, sql, nextIndex);
                Object argValue = nameArgMap.get(argSB.toString());
                if (argValue == null) {
                    throw new RuntimeException(String.format("没有参数#{\nsql:\n参数：%s",
                            stringBuilder.toString(), argSB));
                }
                stringBuilder.append(argValue.toString());
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static int parseSQLArg(StringBuilder argSB, String sql, int nextIndex) {
        nextIndex ++;
        for (; nextIndex < sql.length(); nextIndex++) {
            char c = sql.charAt(nextIndex);
            if (c != '}') {
                argSB.append(c);
                continue;
            }
            if (c == '}') {
                return nextIndex;
            }
        }
        throw new RuntimeException(String.format("缺少右括号\nindex:%d", nextIndex));
    }


    public static Map<String, Object> buildMethodArgNameMap(Method method, Object[] args) {
        Map<String, Object> nameArgMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        int index[] = {0};
        Arrays.asList(parameters).forEach(parameter -> {
            String name = parameter.getName();
            nameArgMap.put(name, args[index[0]]);
            index[0]++;
        });
        return nameArgMap;
    }

}
