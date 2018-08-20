package org.researchsuite.rsrp

import java.lang.reflect.Type

public class PackageConstants {

    companion object {
        public val PackageGsonRegistrations: List<Pair<Type, Any>>
            get() = {

                listOf(
                        Pair(RSRPResultTransform::class.java, RSRPResultTransform.Deserializer()),
                        Pair(RSRPResultTransformInputMapping::class.java, RSRPResultTransformInputMapping.Deserializer())
                )

            }()
    }
}