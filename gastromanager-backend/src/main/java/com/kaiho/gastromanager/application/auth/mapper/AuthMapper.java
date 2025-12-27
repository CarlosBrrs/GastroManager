package com.kaiho.gastromanager.application.auth.mapper;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.RestaurantCreateContactRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.SignupRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.SubscriptionRequestDto;
import com.kaiho.gastromanager.application.user.dto.request.UserRequestDto;
import com.kaiho.gastromanager.application.user.mapper.UserMapper;
import com.kaiho.gastromanager.domain.auth.model.Contact;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.auth.model.Plan;
import com.kaiho.gastromanager.domain.auth.model.Signup;
import com.kaiho.gastromanager.domain.auth.model.Subscription;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final UserMapper userMapper;

    public Login toDomain(LoginRequestDto loginRequestDto) {
        if (loginRequestDto == null) {
            return null;
        }
        return Login.builder()
                    .username(loginRequestDto.username())
                    .password(loginRequestDto.password())
                    .build();
    }

    public Signup toDomain(SignupRequestDto signupRequestDto) {
        if (signupRequestDto == null) {
            return null;
        }
        UserRequestDto userRequestDto = this.getUserRequestDto(signupRequestDto);
        User user = userMapper.toDomain(userRequestDto);
        Subscription subscription = this.toDomain(signupRequestDto.subscription());
        return Signup.builder()
                     .user(user)
                     .subscription(subscription)
                     .build();
    }

    private UserRequestDto getUserRequestDto(SignupRequestDto signupRequestDto) {
        return UserRequestDto.builder()
                             .name(signupRequestDto.name())
                             .lastname(signupRequestDto.lastname())
                             .username(signupRequestDto.username())
                             .password(signupRequestDto.password())
                             .roles(new HashSet<>())
                             .email(signupRequestDto.contact().email())
                             .phone(signupRequestDto.contact().phone())
                             .build();
    }

    private Subscription toDomain(SubscriptionRequestDto subscription) {
        if (subscription == null) {
            return null;
        }
        //TODO: REPLACE THIS FOR A PLAN FROM DATABASE
        Plan plan = new Plan(subscription.plan());
        return Subscription.builder()
                           .plan(plan)
                           .paymentToken(subscription.paymentToken())
                           .build();
    }

    private Contact toDomain(RestaurantCreateContactRequestDto contact) {
        if (contact == null) {
            return null;
        }
        return Contact.builder()
                      .website(contact.website())
                      .phone(contact.phone())
                      .email(contact.email())
                      .build();
    }
}
