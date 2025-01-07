package com.zhitan.singlelogin.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.zhitan.common.core.domain.entity.SysRole;
import com.zhitan.common.core.domain.entity.SysUser;
import com.zhitan.common.utils.SecurityUtils;
import com.zhitan.singlelogin.service.ISingleLoginService;
import com.zhitan.system.domain.SysUserRole;
import com.zhitan.system.mapper.SysUserRoleMapper;
import com.zhitan.system.service.ISysRoleService;
import com.zhitan.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-11-15 17:10
 */
@Component
public class SingleLoginServiceImpl implements ISingleLoginService {

    private static final Logger log = LoggerFactory.getLogger(SingleLoginServiceImpl.class);

    @Value("${singleLoginUrl}")
    private String singleLoginUrl;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Override
    public SysUser singleLogin(String token) {
        JSONObject jsonObject = checkToken(token);
        log.info("请求综合治理" + jsonObject.toString());
        if(jsonObject.containsKey("code") && jsonObject.getInt("code") == 200) {

            final SysUser user = JSONUtil.toBean(jsonObject.getJSONObject("user"), SysUser.class);

            user.setSingleUser(String.valueOf(user.getUserId()));
            user.setUserId(null);
            //检测一下用户存在不存在
            Long count = userService.checkThirdUserExist(user);
            if (count > 0 ) {
                return user;
            } else {
                //用户不存在时，将用户添加到数据库中
                //密码统一都是hrd123456
                user.setPassword(SecurityUtils.encryptPassword("hrd123456"));
                //创建者，标识J平台过来的用户
                user.setCreateBy("thirdPartGetAuth");
                //创建日期
                user.setCreateTime(new Date());

                int rows = userService.insertUser(user);
                if (rows > 0) {
                    SysRole queryRole = new SysRole();
                    queryRole.setRoleKey("zonghezhili");
                    SysRole sysRoles = roleService.getRoleByKey(queryRole);
                    if(null != sysRoles){
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(user.getUserId());
                        sysUserRole.setRoleId(sysRoles.getRoleId());
                        userRoleMapper.batchUserRole(Lists.newArrayList(sysUserRole));
                    }
                }
                return user;
            }
        }else {
            return null;
        }
    }

    /**
     * 通过第三方平台接口，鉴定token合法性，并返回userName等登录信息
     * 这个方法需要根据实际需要进行修改
     * @param token
     * @return
     */
    public JSONObject checkToken(String token) {

        String result = HttpRequest.get(singleLoginUrl)
                .header("Authorization","Bearer " + token)
                .execute()
                .body();
        JSONObject parseObj = JSONUtil.parseObj(result);
        return parseObj;
    }
}
