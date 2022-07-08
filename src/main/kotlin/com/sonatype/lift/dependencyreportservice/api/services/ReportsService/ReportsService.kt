package com.sonatype.lift.dependencyreportservice.api.services.ReportsService

import com.sonatype.lift.dependencyreportservice.api.services.CycloneDx.CycloneDxService
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto.PercentBySeverity
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto.ReportComponent
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.dto.ReportSummary
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.CountsBySeverity
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.ReferenceType
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.ReferenceTypeMap
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.getComponentReferenceTypes
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.getCountOfComponent
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.getCountsBySeverity
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.getCountsBySeverityForComponent
import com.sonatype.lift.dependencyreportservice.api.services.ReportsService.utils.getDisplayName
import org.cyclonedx.model.Bom
import org.cyclonedx.model.Component
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReportsService(val cycloneDxService: CycloneDxService) {

    fun getReferenceTypeMap(id: UUID): ReferenceTypeMap {
        val bom = cycloneDxService.getReportById(id)

        return getComponentReferenceTypes(bom)
    }

    fun getComponents(id: UUID): List<ReportComponent> {
        val bom = cycloneDxService.getReportById(id)

        val componentReferenceTypes = getComponentReferenceTypes(bom)

        return bom.components.map { component: Component ->
            val ref = component.purl ?: component.bomRef

            ReportComponent(
                purl = ref,
                displayName = getDisplayName(component),
                referenceType = getReferenceType(componentReferenceTypes, ref),
                occurrences = getCountOfComponent(ref, bom),
                vulnerabilityCount = getCountsBySeverityForComponent(ref ?: "", bom)
            )
        }
    }

    fun getSummary(id: UUID): ReportSummary {
        val bom = cycloneDxService.getReportById(id)

        val countsBySeverity = getCountsBySeverity(bom)

        val totalVulnCount = bom.vulnerabilities.size

        return ReportSummary(
            reportId = id,
            countsBySeverity = countsBySeverity,
            totalComponentCount = bom.components.size,
            totalVulnerabilityCount = totalVulnCount,
            totalVulnerableComponentCount = getNumComponentsWithIssues(bom),
            percentBySeverity = getPercentsBySeverity(totalVulnCount, countsBySeverity)
        )
    }
}

private fun getReferenceType(
    referenceTypeMap: ReferenceTypeMap,
    refVal: String?
): ReferenceType {
    return if (refVal == null) {
        ReferenceType(isUnknown = true)
    } else {
        referenceTypeMap.get(refVal) ?: ReferenceType(isUnknown = true)
    }
}

private fun getNumComponentsWithIssues(bom: Bom): Int {
    val vulnerablePurls = mutableSetOf<String>()

    bom.vulnerabilities.forEach{ vuln ->
        if (vuln.bomRef != null) {
            vulnerablePurls.add(vuln.bomRef);
        }
    }

    return vulnerablePurls.size
}

private fun getPercentsBySeverity(
    total: Int,
    countsBySeverity: CountsBySeverity
): PercentBySeverity {

    val (unknown, moderate, severe, critical) = countsBySeverity

    return PercentBySeverity(
        percentUnknown = (unknown / total.toDouble()) * 100.0,
        percentModerate = (moderate / total.toDouble()) * 100.0,
        percentSevere = (severe / total.toDouble()) * 100.0,
        percentCritical = (critical / total.toDouble()) * 100.0,
    )
}
