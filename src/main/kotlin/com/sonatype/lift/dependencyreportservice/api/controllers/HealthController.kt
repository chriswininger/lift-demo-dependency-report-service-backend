package com.sonatype.lift.dependencyreportservice.api.controllers

import com.sonatype.lift.dependencyreportservice.api.APPLICATION_JSON
import io.micrometer.core.annotation.Timed
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.management.ManagementFactory

@RestController()
@RequestMapping("/api/health")
@Timed
class HealthController {
  @GetMapping(produces = [APPLICATION_JSON])
  @ApiOperation("a basic health endpoint to determine if the service is running")
  fun getHealth(): HealthResponse {
    val rb = ManagementFactory.getRuntimeMXBean()

    return HealthResponse(upTime = rb.uptime)
  }
}

data class HealthResponse(
  @ApiModelProperty(required = true)
  val status: String = "ok",

  @ApiModelProperty(required = true)
  val upTime: Long,
)
