package com.example.demo.Config;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.UserRepo;




@Component
public class SpringSecurityAuditorAware implements AuditorAware<UsersEntity>{


	private final UserRepo userRepository;

	@Autowired
	public SpringSecurityAuditorAware(UserRepo userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<UsersEntity> getCurrentAuditor() {
		if (isNoAuditingEnabled()) {
			return Optional.empty();
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
		String username = authentication.getName();
		return userRepository.getByEmail(username);
	}

	private boolean isNoAuditingEnabled() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTraceElements) {
			try {
				Class<?> clazz = Class.forName(element.getClassName());
				Method method = clazz.getMethod(element.getMethodName());
				if (method.isAnnotationPresent(NoAuditing.class)) {
					return true;
				}
			} catch (ClassNotFoundException | NoSuchMethodException ignored) {
			}
		}
		return false;
	}
}