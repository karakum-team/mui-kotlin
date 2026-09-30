plugins {
    alias(libs.plugins.kfc.application)
}

dependencies {
    jsMainImplementation(kotlinWrappers.emotion.react)
    jsMainImplementation(kotlinWrappers.emotion.styled)
    jsMainImplementation(kotlinWrappers.react)
    jsMainImplementation(kotlinWrappers.reactDom)

    jsMainImplementation(project(":mui-kotlin"))

    jsMainImplementation(npm("@emotion/react", "11.14.0"))
    jsMainImplementation(npm("@emotion/styled", "11.14.1"))

    // Optional peer of `@mui/x-date-pickers`: `AdapterDateFns` imports it, and every picker throws
    // without an adapter reaching it through `LocalizationProvider`. Needed only by the playground —
    // `:mui-kotlin` declares the adapters but never runs them.
    jsMainImplementation(npm("date-fns", "4.4.0"))

    // Runtime implementation for the generated @date-io/core IUtils smoke test (core is types-only).
    // 3.2.1 supports the date-fns 4.x version already used by the MUI picker samples.
    jsMainImplementation(npm("@date-io/date-fns", "3.2.1"))
}
