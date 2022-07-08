package com.sonatype.lift.dependencyreportservice.api.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("reports")
data class ReportModel(
    @Id
    var id: UUID? = null,

    @CreatedDate
    val createdAt: LocalDateTime? = null,

    val cycloneDX: String
)
