package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto

import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.CountsBySeverity
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.ReferenceType
import io.swagger.annotations.ApiModelProperty

data class ReportComponent (
  val purl: String?,

  @ApiModelProperty(required = true)
  val displayName: String,

  @ApiModelProperty(required = true)
  val referenceType: ReferenceType,

  @ApiModelProperty(required = true)
  val occurrences: Int = 0,

  @ApiModelProperty(required = true)
  val vulnerabilityCount: CountsBySeverity = CountsBySeverity()
)
