package com.sonatype.lift.dependencyreportservice.config

import org.apache.tomcat.jni.User.username
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

//@Configuration
//class DbConfig {
//  @Bean
//  fun dataSource(): DataSource? {
//    println(driverClass + " " + url + " " + username + " " + password)
//    val source = DriverManagerDataSource()
//    source.setDriverClassName(driverClass)
//    source.url = url
//    source.username = username
//    source.password = password
//    return source
//  }
//
//  @Bean
//  fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate? {
//    return NamedParameterJdbcTemplate(dataSource())
//  }
//}