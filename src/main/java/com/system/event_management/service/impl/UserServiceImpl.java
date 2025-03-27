package com.system.event_management.service.impl;

import com.system.event_management.core.UserConstants;
import com.system.event_management.entity.UserEntity;
import com.system.event_management.exception.UserException;
import com.system.event_management.jwt.JwtHelper;
import com.system.event_management.model.userbeans.login.LoginData;
import com.system.event_management.model.userbeans.login.LoginRequestBean;
import com.system.event_management.model.userbeans.login.LoginResponseBean;
import com.system.event_management.model.userbeans.user.UserDataBean;
import com.system.event_management.model.userbeans.user.UserRequestBean;
import com.system.event_management.model.userbeans.user.UserResponseBean;
import com.system.event_management.repository.UserRepository;
import com.system.event_management.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseBean<?> createUser(UserRequestBean userRequestBean) throws UserException {

        userRequestBean.setPassword(this.passwordEncoder.encode(userRequestBean.getPassword()));

        if(this.userRepository.isUserAlreadyExist(userRequestBean.getUsername())>0){
            throw new UserException(String.format(UserConstants.USER_ALREADY_EXISTS,userRequestBean.getUsername()), HttpStatus.CONFLICT);
        }

        UserEntity userEntity=this.userRepository.save(
                UserEntity.builder()
                        .fullName(userRequestBean.getFullName())
                        .username(userRequestBean.getUsername())
                        .password(userRequestBean.getPassword())
                        .build()
        );

        UserDataBean userDataBean=new UserDataBean();

        BeanUtils.copyProperties(userEntity,userDataBean);

        return UserResponseBean.builder()
                .status(true)
                .message(UserConstants.USER_REGISTER_SUCCESS)
                .data(userDataBean)
                .build();

    }

    @Override
    public LoginResponseBean<?> loginUser(LoginRequestBean loginRequestBean) {

        this.doAuthenticate(loginRequestBean.getUsername(), loginRequestBean.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestBean.getUsername());
        String token = this.helper.generateToken(userDetails);

        LoginData loginData=new LoginData(userDetails.getUsername(),token);

        return LoginResponseBean.builder()
                .status(true)
                .message(UserConstants.LOGIN_SUCCESS)
                .data(loginData)
                .build();
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(UserConstants.INVALID_CREDENTIALS);
        }

    }
}
