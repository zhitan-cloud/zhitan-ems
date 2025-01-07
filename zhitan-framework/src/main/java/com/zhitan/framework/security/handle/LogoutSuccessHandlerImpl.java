package com.zhitan.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson2.JSON;
import com.zhitan.common.constant.Constants;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.utils.MessageUtils;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.framework.manager.AsyncManager;
import com.zhitan.framework.manager.factory.AsyncFactory;
import com.zhitan.framework.web.service.TokenService;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author zhitan
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Resource
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
