package org.researchsuite.rsrp

import java.util.*

public interface RSRPFrontEndTransformer {

    fun transform(
            taskIdentifier: String,
            taskRunUUID: UUID,
            parameters: Map<String, Any>
    ): RSRPIntermediateResult?

    fun supportsType(type: String): Boolean

}