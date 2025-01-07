package com.zhitan.web.controller.singlelogin;

import com.zhitan.common.annotation.Anonymous;
import com.zhitan.common.constant.Constants;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.entity.SysUser;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.framework.web.service.SysLoginService;
import com.zhitan.singlelogin.service.ISingleLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单点登录接口对接
 *
 * @author zhitan
 */
@RestController
public class SingleLoginController
{
    @Resource
    private ISingleLoginService singleLoginService;

    @Resource
    private SysLoginService loginService;
    /**
     * 登录方法
     *
     * @param token 登录信息
     * @return 结果
     */
    @Anonymous
    @GetMapping("/singleLogin")
    public AjaxResult singleLogin(@RequestParam String token)
    {
        AjaxResult ajax = AjaxResult.success();
        if(StringUtils.isEmpty(token)){
            return AjaxResult.error("token不能为空");
        }
        SysUser user = singleLoginService.singleLogin(token);
        if(null == user){
            return AjaxResult.error("登录失败");
        }
        // 生成令牌
        String newtoken = loginService.loginNoCode(user.getUserName());
        ajax.put(Constants.TOKEN, newtoken);
        return ajax;
    }

}
