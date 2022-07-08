package com.sonatype.lift.dependencyreportservice.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sonatype.lift.dependencyreportservice.api.model.ReportModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import com.sonatype.lift.dependencyreportservice.api.APPLICATION_JSON
import com.sonatype.lift.dependencyreportservice.api.services.CycloneDx.CycloneDxService
import io.micrometer.core.annotation.Timed
import io.swagger.annotations.ApiOperation
import org.cyclonedx.model.Bom
import javax.servlet.http.HttpServletResponse

@RestController()
@RequestMapping("/api/v1/cyclone-dx")
@Timed
class CycloneDxController(
    val cycloneDxService: CycloneDxService
) {
    private val logger: Logger = LoggerFactory.getLogger(CycloneDxController::class.java)

    @PostMapping()
    fun createReport(@RequestBody cycloneJson: Bom): ReportModel {
        logger.info("handling createReport request")

        if (cycloneJson.dependencies.size > 200) {
            throw RuntimeException("Too much!!!")
        }

        return cycloneDxService.createNewReport(cycloneJson)
    }

    @GetMapping("/{reportId}/cyclone-dx-json", produces = [APPLICATION_JSON])
    @ApiOperation("get cyclone dx from an existing report by report id")
    @ResponseBody
    fun getCycloneDx(
        @PathVariable("reportId") reportId: UUID,
        httpServletResponse: HttpServletResponse
    ): Bom {
        val report = cycloneDxService.getReportById(reportId) // can throw exception

        httpServletResponse.containsHeader("application/json")
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"$reportId.json\"")

        return report
    }
}
