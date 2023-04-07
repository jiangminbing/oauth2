CREATE TABLE `authorities` (
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `authority` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`),
  CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `registered_client_id` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `principal_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `authorization_grant_type` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `attributes` text COLLATE utf8_unicode_ci,
  `state` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `authorization_code_value` text COLLATE utf8_unicode_ci,
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
  `authorization_code_metadata` text COLLATE utf8_unicode_ci,
  `access_token_value` text COLLATE utf8_unicode_ci,
  `access_token_issued_at` timestamp NULL DEFAULT NULL,
  `access_token_expires_at` timestamp NULL DEFAULT NULL,
  `access_token_metadata` text COLLATE utf8_unicode_ci,
  `access_token_type` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `access_token_scopes` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `oidc_id_token_value` text COLLATE utf8_unicode_ci,
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_metadata` text COLLATE utf8_unicode_ci,
  `refresh_token_value` text COLLATE utf8_unicode_ci,
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
  `refresh_token_metadata` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `principal_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `authorities` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `oauth2_registered_client` (
  `id` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `client_id` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_secret` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_secret_expires_at` timestamp NULL DEFAULT NULL,
  `client_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `client_authentication_methods` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `authorization_grant_types` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `redirect_uris` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `scopes` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `client_settings` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `token_settings` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `users` (
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

