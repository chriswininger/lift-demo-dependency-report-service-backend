package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto

import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.CountsBySeverity
import io.swagger.annotations.ApiModelProperty
import java.util.UUID

data class ReportSummary (
  @ApiModelProperty(required = true)
  val reportId: UUID,

  @ApiModelProperty(required = true)
  val countsBySeverity: CountsBySeverity,

  @ApiModelProperty(required = true)
  val totalVulnerabilityCount: Int,

  @ApiModelProperty(required = true)
  val totalVulnerableComponentCount: Int,

  @ApiModelProperty(required = true)
  val totalComponentCount: Int,

  @ApiModelProperty(required = true)
  val percentBySeverity: PercentBySeverity
)
