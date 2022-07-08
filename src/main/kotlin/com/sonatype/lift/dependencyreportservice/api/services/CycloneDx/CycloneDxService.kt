package com.sonatype.lift.dependencyreportservice.api.services.CycloneDx

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sonatype.lift.dependencyreportservice.api.model.ReportModel
import com.sonatype.lift.dependencyreportservice.api.repositories.ReportsRepository
import org.cyclonedx.model.Bom
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class CycloneDxService(val cycloneRepository: ReportsRepository) {
  val JSON = jacksonObjectMapper()

  fun createNewReport(cycloneJson: Bom): ReportModel {
    val model = ReportModel(
      cycloneDX = JSON.writeValueAsString(cycloneJson)
    )

    return cycloneRepository.save(model)
  }

  fun getReportById(id: UUID): Bom {
    val cyclone = cycloneRepository.findById(id)

    if (cyclone.isPresent) {
      return JSON.readValue(cyclone.get().cycloneDX, Bom::class.java)
    } else {
      throw ResponseStatusException(HttpStatus.NOT_FOUND, "report not found");
    }
  }
}
