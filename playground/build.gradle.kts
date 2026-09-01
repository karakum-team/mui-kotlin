plugins {
    alias(libs.plugins.kfc.application)
}

dependencies {
    jsMainImplementation(kotlinWrappers.emotion.react)
    jsMainImplementation(kotlinWrappers.emotion.styled)
    jsMainImplementation(kotlinWrappers.react)
    jsMainImplementation(kotlinWrappers.reactDom)

    jsMainImplementation(project(":mui-kotlin"))

    jsMainImplementation(npm("@emotion/react", "11.9.0"))
    jsMainImplementation(npm("@emotion/styled", "11.8.1"))

    // Optional peer of `@mui/x-date-pickers`: `AdapterDateFns` imports it, and every picker throws
    // without an adapter reaching it through `LocalizationProvider`. Needed only by the playground —
    // `:mui-kotlin` declares the adapters but never runs them.
    jsMainImplementation(npm("date-fns", "4.1.0"))
}
