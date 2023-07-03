package com.pizza.manager.persist.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
//@EnableJpaRepositories(basePackages = ["com.management.pizza.*"])
//@EntityScan(basePackages = ["com.management.pizza.*"])
@EnableJpaAuditing
@EnableTransactionManagement
class PizzaPersistenceConfig
