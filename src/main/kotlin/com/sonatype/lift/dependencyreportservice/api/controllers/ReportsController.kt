package com.sonatype.lift.dependencyreportservice.api.controllers

import com.sonatype.lift.dependencyreportservice.api.APPLICATION_JSON
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.ReportsService
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto.ReportComponent
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto.ReportSummary
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.ReferenceTypeMap
import io.micrometer.core.annotation.Timed
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController()
@RequestMapping("/api/v1/reports")
@Timed
class ReportsController(
  val reportsService: ReportsService
) {
  @GetMapping("/{reportId}/component-ref-types", produces = [APPLICATION_JSON])
  @ApiOperation("get cyclone dx from an existing report by report id")
  @ResponseBody
  fun getComponentReferenceTypeMap(@PathVariable("reportId") reportId: UUID): ReferenceTypeMap {
    return reportsService.getReferenceTypeMap(reportId)
  }

  @GetMapping("/{reportId}/components", produces = [APPLICATION_JSON])
  @ApiOperation("get a list of components with applied meta data for reporting via the report id")
  @ResponseBody
  fun getComponents(@PathVariable("reportId") reportId: UUID): List<ReportComponent> {
    return reportsService.getComponents(reportId)
  }

  @GetMapping("/{reportId}/summary", produces = [APPLICATION_JSON])
  @ApiOperation("get summary information about the bill of materials")
  fun getSummary(@PathVariable("reportId") reportId: UUID): ReportSummary {
    return reportsService.getSummary(reportId)
  }
}
