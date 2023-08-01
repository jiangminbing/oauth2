/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oauth.sample.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2019/2/1 删除token端点
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class PigTokenEndpoint {


	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("ftl/login");
		modelAndView.addObject("error", error);
		return modelAndView;
	}
	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
//	@GetMapping("/confirm_access")
//	public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
//								@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
//								@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
//								@RequestParam(OAuth2ParameterNames.STATE) String state) {
//
//		modelAndView.addObject("clientId", clientId);
//		modelAndView.addObject("state", state);
//		modelAndView.addObject("scopeList", "12312");
//		modelAndView.addObject("principalName", principal.getName());
//		modelAndView.setViewName("ftl/confirm");
//		return modelAndView;
//	}
	@GetMapping("/confirm_access")
	public ModelAndView confirm(Principal principal, ModelAndView modelAndView) {
		List<String> elements = Arrays.asList("1231", "12312");
		Set<String> authorizedScopes = new HashSet<>(elements);
		modelAndView.addObject("clientId", "2342");
		modelAndView.addObject("state", "2342");
		modelAndView.addObject("scopeList", authorizedScopes);
		modelAndView.addObject("principalName","12312");
		modelAndView.setViewName("ftl/confirm");
		return modelAndView;
	}
}
