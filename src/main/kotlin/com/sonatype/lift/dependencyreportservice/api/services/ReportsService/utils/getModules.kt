package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import org.cyclonedx.model.Bom
import org.cyclonedx.model.Dependency

fun getModules(bom: Bom): Set<String> {
  val appDependencies = findAppDependencyList(bom)
    ?: throw RuntimeException("could not find the application level dependency list in the cyclone dx")

  val dependsOnValues: List<String> = (appDependencies.dependencies).map { it.ref ?: "bad-ref" }

  return dependsOnValues.toSet()
}

private fun findAppDependencyList(
  cycloneDx: Bom
): Dependency? {
  val appName = findAppName(cycloneDx)

  return cycloneDx.dependencies.find { dependency -> dependency.ref == appName }
}
