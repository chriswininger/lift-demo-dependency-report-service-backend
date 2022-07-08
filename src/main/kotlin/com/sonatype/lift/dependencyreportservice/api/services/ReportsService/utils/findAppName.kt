package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import org.cyclonedx.model.Bom

fun findAppName(cycloneDx: Bom): String {
  return cycloneDx.metadata.component.bomRef
}
