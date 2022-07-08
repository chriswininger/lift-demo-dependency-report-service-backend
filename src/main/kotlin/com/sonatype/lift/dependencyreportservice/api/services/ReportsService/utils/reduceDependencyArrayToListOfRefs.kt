package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import org.cyclonedx.model.Dependency

fun reduceDependencyArrayToListOfRefs(dependencies: List<Dependency>): List<String> {
  return dependencies.map {
    if (it.ref === null) {
      throw Error("can't process cyclone, missing dependsOn ref")
    } else {
      it.ref
    }
  }
}