package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import org.cyclonedx.model.Bom
import org.cyclonedx.model.Dependency

// Note: https://github.com/FasterXML/jackson-module-kotlin/issues/80#issuecomment-402432622
data class ReferenceType(
  @ApiModelProperty(required = true)
  @get:JsonProperty("isDirect")
  val isDirect: Boolean = false,

  @ApiModelProperty(required = true)
  @get:JsonProperty("isTransitive")
  val isTransitive: Boolean = false,

  @ApiModelProperty(required = true)
  @get:JsonProperty("isProject")
  val isProject: Boolean = false,

  @ApiModelProperty(required = true)
  @get:JsonProperty("isUnknown")
  val isUnknown: Boolean = false
)

typealias ReferenceTypeMap = MutableMap<String, ReferenceType>

fun getComponentReferenceTypes(
  cycloneDx: Bom
): ReferenceTypeMap {
  val modules = getModules(cycloneDx)
  val directDependencies = getDirectDependencies(cycloneDx, modules)
  val transitiveDependencies = getTransitiveDependencies(cycloneDx, modules)

  val componentRefTypeMap: ReferenceTypeMap = mutableMapOf()

  cycloneDx.dependencies.forEach {
    val ref = it.ref ?: throw RuntimeException("Missing ref in SBOM, a report is not possible")

    val isProject = modules.contains(ref)
    val isDirect = directDependencies.contains(ref)
    val isTransitive = transitiveDependencies.contains(ref)

    val isUnknown = !isProject && !isDirect &&  !isTransitive

    componentRefTypeMap[ref] = ReferenceType(
      isProject = isProject,
      isDirect = isDirect,
      isTransitive = isTransitive,
      isUnknown = isUnknown
    )
  }

  return componentRefTypeMap
}

private fun getDirectDependencies(
  cycloneDx: Bom,
  moduleRefs: Set<String>
): Set<String> {
  val allDependencies = cycloneDx.dependencies

  return moduleRefs.map { moduleRef ->
    allDependencies.find { dep -> dep.ref == moduleRef }
  }.filterNotNull()
    .flatMap { reduceDependencyArrayToListOfRefs(it.dependencies) }
    .toSet()
}

private fun getTransitiveDependencies(
  cycloneDx: Bom,
  moduleRefs: Set<String>
): Set<String> {
  val appName = findAppName(cycloneDx)

  val nonModuleDirectAndInDirectDependencies: List<Dependency> = cycloneDx.dependencies.filter {
    val ref = it.ref ?: throw  RuntimeException("bad cyclone missing ref")

    ref != appName && !moduleRefs.contains(ref)
  }

  return nonModuleDirectAndInDirectDependencies
    .flatMap { nonModule -> reduceDependencyArrayToListOfRefs(nonModule.dependencies) }
    .toSet()
}
