package com.zhitan.framework.config;


import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.utils.ReflectionUtils;
import com.zhitan.common.utils.SecurityUtils;
import com.zhitan.framework.constant.SQLFieldConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * mybatis 拦截器
 *
 * @author Silence
 * @version 1.0
 */
@Slf4j
@Component
@Intercepts(
        {@Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )})
public class MyBatisInterceptor implements Interceptor {
    /**
     * 拦截器处理
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("sqlId:{}", sqlId);
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];

        if (parameter == null) {
            return invocation.proceed();
        }

        LoginUser user = getLoginUser();
        log.debug("sqlCommandType:{}", commandType);
        switch (commandType) {
            case INSERT:
                setFieldValue(parameter, SQLFieldConst.CREATE_BY, user == null ? "" : user.getUsername());
                setFieldValue(parameter, SQLFieldConst.CREATE_TIME, new Date());
                break;
            case UPDATE:
                if (parameter instanceof MapperMethod.ParamMap) {
                    MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
                    String et = "et";
                    if (p.containsKey(et)) {
                        parameter = p.get(et);
                    } else {
                        parameter = p.get("param1");
                    }
                    if (parameter == null) {
                        return invocation.proceed();
                    }
                }
                setFieldValue(parameter, SQLFieldConst.UPDATE_BY, user == null ? "" : user.getUsername());
                setFieldValue(parameter, SQLFieldConst.UPDATE_TIME, new Date());
                break;
            case UNKNOWN:
            default:
                break;
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置注入的属性值
     *
     * @param parameter SQL拦截的参数
     * @param filter    过滤条件
     * @param value     需要设置的值
     */
    private void setFieldValue(Object parameter, @NotNull String filter, Object value) {
        Field[] fields = ReflectionUtils.getAllFields(parameter);
        Optional<Field> fieldOptional = Arrays.stream(fields)
                .filter(x -> filter.equals(x.getName()))
                .findFirst();
        if (fieldOptional.isPresent()) {
            try {
                Field field = fieldOptional.get();
                field.setAccessible(true);
                Object localValue = field.get(parameter);
                field.setAccessible(false);
                if (localValue == null || "".equals(localValue)) {
                    field.setAccessible(true);
                    field.set(parameter, value);
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException exception) {
                log.error("SQL注入失败:filter:{},value:{}", filter, value);
            }
        }
    }

    /**
     * 获取登录用户
     */
    private LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }
}
