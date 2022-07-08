package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import com.github.packageurl.PackageURL
import org.cyclonedx.model.Component

private const val UNKNOWN_ECOSYSTEM = "Unknown";

const val SCANNED_APPLICATION = "scanned-application";

private val purlFormatToDisplayNameMap = mapOf(
    Pair("alpine", "Alpine"),
    Pair("bower", "Bower"),
    Pair("cargo", "Cargo"),
    Pair("chocolatey", "Chocolatey"),
    Pair("clojars", "Clojars"),
    Pair("cocoapods", "CocoaPods"),
    Pair("composer", "Composer"),
    Pair("conan", "Conan"),
    Pair("conda", "Conda"),
    Pair("cran", "CRAN"),
    Pair("deb", "Debian"),
    Pair("drupal", "Drupal"),
    Pair("golang", "Golang"),
    Pair("maven", "Maven"),
    Pair("npm", "npm"),
    Pair("nuget", "NuGet"),
    Pair("pypi", "PyPI"),
    Pair("rpm", "RPM"),
    Pair("gem", "RubyGems"),
    Pair("swift", "Swift"),
)

fun getDisplayName(component: Component): String {
  val purl = component.purl ?: return component.name ?: "[missing-component-name]"

  if (purl == SCANNED_APPLICATION) {
    return purl
  }

  try {
    val pkgUrl = PackageURL(purl)

    val displayType = purlFormatToDisplayNameMap[pkgUrl.type] ?: UNKNOWN_ECOSYSTEM

    val componentName = if (pkgUrl.namespace != null) {
      "$displayType : ${pkgUrl.namespace}/${pkgUrl.name}"
    } else {
      "$displayType : ${pkgUrl.name}"
    }

    return if (pkgUrl.version != null) {
      "$componentName : ${pkgUrl.version}"
    } else {
      componentName
    }

  } catch (ex: Exception) {
    // IDEA thinks this block is ureachable but PackageURL can throw exceptions when given malformed urls
    error("Error parsing purl for display name: '$purl'")

    return  purl
  }
}