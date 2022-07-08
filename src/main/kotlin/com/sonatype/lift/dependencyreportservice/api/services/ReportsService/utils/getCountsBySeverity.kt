package com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils

import com.fasterxml.jackson.annotation.JsonProperty
import org.cyclonedx.model.Bom
import org.cyclonedx.model.vulnerability.Vulnerability

data class CountsBySeverity (
  val unknown: Int = 0,
  val moderate: Int = 0,
  val severe: Int = 0,
  val critical: Int = 0
)

enum class VulnerabilitySeverity(val value: String) {
  @JsonProperty("unknown")
  UNKNOWN("unknown"),
  @JsonProperty("moderate")
  MODERATE("moderate"),
  @JsonProperty("severe")
  SEVERE("severe"),
  @JsonProperty("critical")
  CRITICAL("critical")
}

fun getCountsBySeverity(
  cyloneDx: Bom
): CountsBySeverity {
  return cyloneDx.vulnerabilities.fold(CountsBySeverity()) { acc: CountsBySeverity, nextVuln: Vulnerability ->
    incrementCountForSeverity(acc, nextVuln)
  }
}

fun getCountsBySeverityForComponent(
  purl: String,
  cycloneDx: Bom
): CountsBySeverity {
  val vulnAgainstComponent = cycloneDx.vulnerabilities.filter { vuln ->
   vuln.bomRef == purl
  }

  return vulnAgainstComponent.fold(CountsBySeverity()) { acc: CountsBySeverity, nextVuln ->
    incrementCountForSeverity(acc, nextVuln)
  }
}

private fun getSeverity(vulnerability: Vulnerability): VulnerabilitySeverity {
  val ratings: MutableList<Vulnerability.Rating> = vulnerability.ratings ?: mutableListOf()
  val score = getScore(ratings)

  return if (score <= 1) {
    VulnerabilitySeverity.UNKNOWN
  } else if (score <= 3.9) {
    VulnerabilitySeverity.MODERATE;
  } else if (score <= 6.9) {
    VulnerabilitySeverity.SEVERE;
  } else {
    VulnerabilitySeverity.CRITICAL;
  }
}

private fun getScore(ratings: MutableList<Vulnerability.Rating>): Double {
  val rating = getBestPossibleRating(ratings)

  return if (rating != null && rating.score != null) {
    rating.score
  } else {
    0.0
  }
}

private fun getBestPossibleRating(
  ratings: MutableList<Vulnerability.Rating>
): Vulnerability.Rating? {
  val cvssv3 = ratings.find { rating -> rating.method == Vulnerability.Rating.Method.CVSSV3 }
  val cvssV2 = ratings.find { rating -> rating.method == Vulnerability.Rating.Method.CVSSV2 }

  return if (cvssv3 != null) {
    cvssv3
  } else if (cvssV2 != null) {
    cvssV2
  } else {
    null
  }
}

private fun incrementCountForSeverity(currentCounts: CountsBySeverity, nextVuln: Vulnerability): CountsBySeverity {
  return when (getSeverity(nextVuln)) {
    VulnerabilitySeverity.UNKNOWN -> currentCounts.copy( unknown = currentCounts.unknown + 1)
    VulnerabilitySeverity.MODERATE -> currentCounts.copy(moderate = currentCounts.moderate + 1)
    VulnerabilitySeverity.SEVERE -> currentCounts.copy(severe = currentCounts.severe + 1)
    VulnerabilitySeverity.CRITICAL -> currentCounts.copy(critical = currentCounts.critical + 1)
  }
}