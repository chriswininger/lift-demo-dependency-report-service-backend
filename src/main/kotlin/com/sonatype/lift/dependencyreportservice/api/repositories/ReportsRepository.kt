package com.sonatype.lift.dependencyreportservice.api.repositories

import com.sonatype.lift.dependencyreportservice.api.model.ReportModel
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReportsRepository : PagingAndSortingRepository <ReportModel, UUID>
{

}