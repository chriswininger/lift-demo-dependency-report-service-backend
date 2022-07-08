package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto

import io.swagger.annotations.ApiModelProperty

data class PercentBySeverity (
  @ApiModelProperty(required = true)
  val percentUnknown: Double,

  @ApiModelProperty(required = true)
  val percentModerate: Double,

  @ApiModelProperty(required = true)
  val percentSevere:  Double,

  @ApiModelProperty(required = true)
  val percentCritical: Double,
)
