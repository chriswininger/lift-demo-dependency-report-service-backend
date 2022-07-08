package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import org.cyclonedx.model.Bom

fun getCountOfComponent(
  purl: String,
  cycloneDx: Bom
): Int {
  return cycloneDx.dependencies.filter { dependency ->
    val defRefs = reduceDependencyArrayToListOfRefs(dependency.dependencies)

    defRefs.filter { dependsOn -> purl == dependsOn }.isNotEmpty()
  }.size
}
