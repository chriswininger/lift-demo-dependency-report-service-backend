package com.sonatype.lift.dependencyreportservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.sonatype.lift.dependencyreportservice"])
class DependencyReportServiceApplication

fun main(args: Array<String>) {
	runApplication<DependencyReportServiceApplication>(*args)
}
