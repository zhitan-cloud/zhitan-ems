package com.zhitan.framework.security.single;

import com.zhitan.framework.web.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SingleAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 认证逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SingleAuthenticationToken SingleAuthenticationToken = (SingleAuthenticationToken) authentication;

        String username = (String) SingleAuthenticationToken.getPrincipal();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        SingleAuthenticationToken result = new SingleAuthenticationToken(user, Collections.emptyList());
        /*
        Details 中包含了 ip地址、 sessionId 等等属性 也可以存储一些自己想要放进去的内容
        */
        result.setDetails(SingleAuthenticationToken.getDetails());
        return result;
    }

    /**
     *UserIdAuthenticationToken交给UserIdAuthenticationProvider处理
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return SingleAuthenticationToken.class.isAssignableFrom(aClass);

    }
}
