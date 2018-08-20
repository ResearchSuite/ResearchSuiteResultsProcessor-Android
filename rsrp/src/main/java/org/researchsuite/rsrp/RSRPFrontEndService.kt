package org.researchsuite.rsrp

import org.researchstack.backbone.result.StepResult
import org.researchstack.backbone.result.TaskResult
import org.researchsuite.rsrp.RSRPResultTransform
import java.util.*

open class RSRPFrontEndService(val transformers: List<RSRPFrontEndTransformer>) {

    public fun processResult(taskResult: TaskResult, taskRunUUID: UUID, resultTransforms: List<RSRPResultTransform>): List<RSRPIntermediateResult> {

        return resultTransforms.map {

            RSRPFrontEndService.processResult(
                    taskResult,
                    taskRunUUID,
                    it,
                    this.transformers
            )

        }.filterNotNull()

    }

    companion object {

        public fun processResult(taskResult: TaskResult, taskRunUUID: UUID, resultTransform: RSRPResultTransform, frontEndTransformers: List<RSRPFrontEndTransformer>): RSRPIntermediateResult? {

            val parameters: Map<String, Any> = resultTransform.inputMapping.map { mapping ->

                when(mapping.mappingType) {

                    is RSRPResultTransformInputMappingType.StepIdentifier -> {
                         Pair(mapping.parameter, taskResult.getStepResult(mapping.mappingType.stepIdentifier))
                    }

                    is RSRPResultTransformInputMappingType.StepIdentifierRegex -> {
                        val stepResults = taskResult.results.toMap().values
                        val regex = Regex(mapping.mappingType.stepIdentifierRegex)
                        val matchingStepResults = stepResults.filter { regex.containsMatchIn(it.identifier) }
                        Pair(mapping.parameter, matchingStepResults)
                    }

                    is RSRPResultTransformInputMappingType.Constant -> {
                        Pair(mapping.parameter, mapping.mappingType.value)
                    }

                }


            }.toMap()

            return transformResult(resultTransform.transform, taskResult.identifier, taskRunUUID, parameters, frontEndTransformers )
        }

        public fun transformResult(
                type: String,
                taskIdentifier: String,
                taskRunUUID: UUID,
                parameters: Map<String, Any>,
                frontEndTransformers: List<RSRPFrontEndTransformer>
        ): RSRPIntermediateResult? {

            for (transformer in frontEndTransformers) {

                if (transformer.supportsType(type)) {
                    val intermediateResult = transformer.transform(taskIdentifier, taskRunUUID, parameters)
                    if (intermediateResult != null) {
                        return intermediateResult
                    }
                }

            }

            return null
        }

    }

}