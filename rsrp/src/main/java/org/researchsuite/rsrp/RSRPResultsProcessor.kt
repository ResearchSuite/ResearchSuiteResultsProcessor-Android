package org.researchsuite.rsrp

import android.content.Context
import org.researchstack.backbone.result.TaskResult
import java.util.*

public class RSRPResultsProcessor(
        frontEndTransformers: List<RSRPFrontEndTransformer>,
        val backEnds: List<RSRPBackEnd>
) {

    private val frontEndService = RSRPFrontEndService(frontEndTransformers)

    public fun processResult(context: Context, taskResult: TaskResult, taskRunUUID: UUID, resultTranforms: List<RSRPResultTransform>) {

        val intermediateResults = this.frontEndService.processResult(
                taskResult,
                taskRunUUID,
                resultTranforms
        )

        backEnds.forEach { backEnd ->
            intermediateResults.forEach {
                backEnd.add(context, it)
            }
        }
    }

}