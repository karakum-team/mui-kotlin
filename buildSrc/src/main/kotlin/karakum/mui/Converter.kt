package karakum.mui

import java.io.File

internal const val JS_NAME_DEFAULT = "@JsName(\"default\")\n"

internal data class ConversionResult(
    val main: String,
    val extensions: String,
)

internal fun convertClasses(
    componentName: String,
    definitionFile: File,
): Pair<String, String?> {
    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val slots = mutableListOf<String>()

    val classesName = componentName + "Classes"
    val muiName = MUI + componentName

    val source = content.substringAfter("export interface $classesName {\n", "")
    if (source.isEmpty()) {
        check(componentName == "Container")
        return "typealias $classesName = mui.system.$classesName" to "typealias $muiName = mui.system.$muiName"
    }

    val classes = source
        .substringBefore("\n}\n")
        .trimIndent()
        .splitToSequence("\n")
        .map {
            val name = it.removeSuffix(": string;")
                .removeSuffix("?")

            if (name == it) return@map it

            if (!name.startsWith("'"))
                slots += name

            val line = "var $name: ClassName"
            if (name.startsWith("'")) "    // $line" else line
        }
        .joinToString("\n")

    val classesContent = "external interface $classesName {\n" +
            "$classes\n" +
            "}\n"

    if ("Unstyled" in componentName)
        return classesContent to null

    val muiContent = convertSealed(
        name = muiName,
        keys = slots.filter { it !in MUI_CLASSES },
        getValue = { "$muiName-$it" },
        type = "ClassName",
    )

    return classesContent to muiContent
}

internal fun convertDefinitions(
    definitionFile: File,
): ConversionResult {
    val name = definitionFile.name.substringBefore(".")
        .removeSuffix("Props")

    val (content, defaultUnions) = definitionFile.readText()
        .replace("\r\n", "\n")
        .removeInlineClasses()
        .removeDeprecated()
        .replace("(inProps: ", "(props: ")
        .replace(
            "declare type PopperProps = Omit<PopperUnstyledProps, 'direction'> &",
            "interface PopperProps extends PopperUnstyledProps",
        )
        .replace(
            "\ninterface ${name}OwnProps {\n",
            "\nexport interface ${name}OwnProps {\n",
        )
        .let { findDefaultUnions(name, it) }

    val declarations = mutableListOf<String>()

    val propsName = "${name}Props"

    findProps(name, propsName, content)
        ?.also(declarations::add)

    findMapProps(name, propsName, content)
        ?.also(declarations::add)

    val additionalInterfaces = findAdditionalProps(propsName, content)
    val functionInterfaces = additionalInterfaces
        .filter { "interface Spacing {" in it }

    declarations += (additionalInterfaces - functionInterfaces)

    val fun0Declaration = "export default function $name<"
    val fun1Declaration = "export default function $name(props: $propsName): JSX.Element;"
    val fun2Declaration = "declare function $name(props: $propsName): JSX.Element;"
    val typeDeclaration = "declare const $name: React.ComponentType<$propsName>;"
    val const1Declaration = "declare const $name: "
    val const2Declaration = "export default _default"

    declarations += listOfNotNull(
        findComponent(name, propsName, fun0Declaration, content),
        findComponent(name, propsName, fun1Declaration, content),
        findComponent(name, propsName, fun2Declaration, content),
        findComponent(name, propsName, typeDeclaration, content, "ComponentType"),
        findComponent(name, propsName, const1Declaration, content),
        findComponent(name, propsName, const2Declaration, content),
    ).take(1)

    declarations += listOfNotNull(
        findDefaultFunction(name, content)
    )

    val enums = content.splitToSequence("export type ", "export declare type ")
        .drop(1)
        .map { it.substringBefore(";") }
        .mapNotNull { convertUnion(it) }
        .plus(defaultUnions)
        .toList()

    val mainContent = fixOverrides(
        name = name,
        content = declarations.joinToString("\n\n")
    )

    return ConversionResult(
        main = mainContent,
        extensions = (enums + functionInterfaces).joinToString("\n\n"),
    )
}

private fun String.removeInlineClasses(): String =
    removeInlineClasses("  classes: ")
        .removeInlineClasses("  classes?: ")

private fun String.removeDeprecated(): String {
    if ("interface MuiMediaQuery" !in this)
        return this

    return substringAfter(substringBefore("export interface Options {"))
        .replace("Options", "UseMediaQueryOptions")
}

private fun String.removeInlineClasses(
    trigger: String,
): String {
    if (trigger !in this)
        return this

    val parts = split(trigger)
    if (parts.size != 2)
        return this

    val (s, e) = parts

    val type = when {
        e.startsWith("Partial<BadgeClasses> & {")
        -> "BadgeClasses"

        e.startsWith("Partial<ButtonClasses> & {")
        -> "mui.material.ButtonClasses"

        e.startsWith("BadgeUnstyledTypeMap['props']['classes'] & {")
        -> "unknown"

        e.startsWith("SliderUnstyledTypeMap['props']['classes'] & {")
        -> "unknown"

        e.startsWith("{")
        -> "unknown"

        else -> return this
    }

    return s + "  classes?: $type;" + e.substringAfter("};")
}

private fun findProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    when (name) {
        "TextField",
        -> return "typealias $propsName = BaseTextFieldProps"

        "TreeView",

        "DateField",
        "StaticDatePicker",
        "StaticDateTimePicker",
        "StaticTimePicker",
        -> return props(propsName)
    }

    val propsContent = sequenceOf(" ", "<", "\n")
        .flatMap {
            sequenceOf(
                content.substringAfter("export default interface $propsName$it", ""),
                content.substringAfter("export interface $propsName$it", ""),
            )
        }
        .singleOrNull { it.isNotEmpty() }
        ?: return null

    val propsDeclaration = when {
        propsContent.startsWith("TDate>")
        -> "$propsName<TDate>"

        propsContent.startsWith("TValue>") || propsContent.startsWith("TValue extends ")
        -> "$propsName<TValue>"

        propsContent.startsWith("T = unknown>")
        -> "$propsName<T>"

        propsName == "AutocompleteProps"
        -> "$propsName<T>"

        else -> propsName
    }

    val parentType = findParentType(
        content.substringBefore(propsContent).substringAfterLast("\n") + propsContent,
    )

    val source = propsContent
        .substringAfter("{\n")

    val membersContent = source
        .takeIf { !it.startsWith("}\n") }
        ?.substringBefore(";\n}")
        ?: ""

    val body = convertMembers(membersContent)
    return props(
        propsName = propsDeclaration,
        parentType = parentType,
        hasChildren = CHILDREN in body,
        hasClassName = CLASS_NAME in body,
        hasSx = SX in body,
    ) + " {\n$body\n}"
}

private fun findMapProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    val propsContent = sequenceOf(
        content.substringAfter("export interface ${name}TypeMap<", "")
            .substringBefore("\n}\n"),
        content.substringAfter("export type ${name}TypeMap<", "")
            .substringBefore("\n}>;\n"),
    ).firstOrNull { it.isNotEmpty() }
        ?: return null

    var intrinsicType = propsContent
        .substringBefore(" {\n")
        .substringAfter(" D extends React.ElementType = '", "")
        .substringBefore("'", "")

    if (name == "TablePaginationUnstyledProps")
        intrinsicType = "td"

    val parentType: String? = when {
        name == "LoadingButton"
        -> "mui.material.ButtonProps"

        "${name}BaseProps & {" in propsContent || "props: P & ${name}BaseProps;" in propsContent
        -> {
            val baseType = "${name}BaseProps"
            if (name.startsWith("List")) {
                sequenceOf(baseType, "react.dom.html.HTMLAttributes<dom.html.HTMLElement>")
                    .joinToString(",\n", "\n")
            } else baseType
        }

        "props: P & ${name}OwnProps;" in propsContent
        -> {
            val intrinsicProps = when (propsName) {
                "InputUnstyledProps" -> "react.dom.html.HTMLAttributes<dom.html.HTMLInputElement>"
                else -> INTRINSIC_TYPE_MAP[intrinsicType]
            }

            sequenceOf("${name}OwnProps", intrinsicProps)
                .joinToString(",\n", "\n")
        }

        "props: P &\n    DistributiveOmit<PaperProps, " in propsContent
        -> "PaperProps"

        "${name}UnstyledTypeMap<{" in propsContent
        -> "mui.base.${name}UnstyledProps"

        else -> INTRINSIC_TYPE_MAP[intrinsicType]
    }

    val membersContent = propsContent
        .substringAfter("props: P", "")
        .substringAfter(" & {\n", "")
        .let { str ->
            sequenceOf(
                str.substringBefore(";\n    } & Breakpoints;", ""),
                str.substringBefore(";\n    };", ""),
                str.substringBefore(";\n  };", "")
            ).maxByOrNull { it.length }!!
        }

    return if (membersContent.isNotEmpty()) {
        val body = convertMembers(membersContent)
        props(
            propsName = propsName,
            parentType = parentType,
            hasChildren = CHILDREN in body,
            hasClassName = CLASS_NAME in body,
            hasSx = SX in body,
            hasComponent = ": OverridableComponent<" in content,
        ) + " {\n$body\n}"
    } else {
        props(propsName, parentType)
    }
}

private val EXCLUDED_PREFIXES = setOf(
    "Map",
    "Overrides",

    // TEMP
    "Header",
)

private fun findAdditionalProps(
    propsName: String,
    content: String,
): List<String> {
    var delimiters = arrayOf("export interface ", "export default interface ")
    if ("interface BaseTheme" in content)
        delimiters += "interface "

    val bodies = content.splitToSequence(*delimiters)
        .drop(1)
        .toList()

    if (bodies.isEmpty())
        return emptyList()

    return bodies.mapNotNull { body ->
        val interfaceName = body
            .substringBefore(" ")
            .substringBefore("\n")
            .substringBefore("<")

        val propsLike = interfaceName.endsWith("Props")
        // TODO: check
        if (propsLike && interfaceName == propsName && interfaceName != "UseButtonProps")
            return@mapNotNull null

        if (interfaceName == "ValueLabelProps" || interfaceName == "UseButtonProps")
            return@mapNotNull null

        if (interfaceName == "ExtendableDateType")
            return@mapNotNull null

        if (!propsLike && EXCLUDED_PREFIXES.any { interfaceName.endsWith(it) })
            return@mapNotNull null

        val parentType = findParentType(body)

        val membersContent = when (interfaceName) {
            "InputBaseComponentProps",
            "CustomSystemProps",

            "CalendarPickerSlotsComponent",
            "CalendarPickerSlotsComponentsProps",

            "ClockPickerSlotsComponent",
            "ClockPickerSlotsComponentsProps",

            "DatePickerSlotsComponent",
            "DatePickerSlotsComponentsProps",

            "DateTimePickerSlotsComponent",
            "DateTimePickerSlotsComponentsProps",

            "DesktopDatePickerSlotsComponent",
            "DesktopDatePickerSlotsComponentsProps",

            "DesktopDateTimePickerSlotsComponent",
            "DesktopDateTimePickerSlotsComponentsProps",

            "DesktopTimePickerSlotsComponent",
            "DesktopTimePickerSlotsComponentsProps",

            "MobileDatePickerSlotsComponent",
            "MobileDatePickerSlotsComponentsProps",

            "MobileDateTimePickerSlotsComponent",
            "MobileDateTimePickerSlotsComponentsProps",

            "MobileTimePickerSlotsComponent",
            "MobileTimePickerSlotsComponentsProps",

            "StaticDatePickerSlotsComponent",
            "StaticDatePickerSlotsComponentsProps",

            "StaticDateTimePickerSlotsComponent",
            "StaticDateTimePickerSlotsComponentsProps",

            "StaticTimePickerSlotsComponents",
            "StaticTimePickerSlotsComponentsProps",

            "TimePickerSlotsComponent",
            "TimePickerSlotsComponentsProps",
            -> ""

            "Spacing",
            -> return@mapNotNull convertSpacing(
                name = interfaceName,
                body = body.substringAfter("{\n")
                    .substringBefore("\n}\n")
                    .trimIndent()
            )

            "MixinsOptions",
            -> body.substringAfter("{\n")
                .substringBefore("\n}\n")

            else
            -> body.substringAfter("{\n")
                .substringBefore(";\n}\n")
        }

        var propsBody = when {
            interfaceName.endsWith("Actions")
            -> convertMethods(membersContent)

            interfaceName == "IUtils"
            -> convertDateUtils(membersContent)

            else -> convertMembers(membersContent)
        }

        when (interfaceName) {
            "TreeViewPropsBase",
            -> propsBody = propsBody.replace("var id:", "override var id:")

            "CommonColors",
            "PaletteColor",
            "TypeText",
            "TypeAction",
            "SimplePaletteColorOptions",
            "PaletteAugmentColorOptions",
            -> propsBody = propsBody.replace(": String", ": csstype.Color")
        }

        when (parentType) {
            "mui.system.ThemeOptions",
            "mui.system.Theme",
            "BaseTheme",
            -> propsBody = sequenceOf(
                "mixins",
                "components",
                "palette",
                "shadows",
                "transitions",
                "typography",
                "zIndex",
            ).fold(propsBody) { acc, propName ->
                acc.replace("var $propName:", "override var $propName:")
            }
        }

        val hasChildren = CHILDREN in propsBody
        var declaration = when {
            propsLike || hasChildren
            -> props(
                propsName = interfaceName,
                parentType = parentType,
                hasChildren = hasChildren,
                hasClassName = CLASS_NAME in propsBody,
                hasSx = SX in propsBody,
            )

            interfaceName.endsWith("Params") || interfaceName == "UsePaginationItem"
            -> props(interfaceName, parentType, hasChildren = false)

            else -> "external interface $interfaceName" +
                    if (parentType != null) ": $parentType" else ""
        }

        when (interfaceName) {
            "UseAutocompleteProps",
            -> declaration = declaration.replaceFirst(":", "<T>:")

            "AutocompleteChangeDetails",
            "CreateFilterOptionsConfig",
            "FilterOptionsState",
            -> declaration += "<T>"

            "SelectOption",
            "UseSelectSingleParameters",
            "UseSelectMultiParameters",
            "UseSelectSingleResult",
            "UseSelectMultiResult",
            -> declaration += "<TValue>"

            "MultiSelectUnstyledOwnerState",
            -> declaration = declaration.replaceFirst(":", "<TValue>:")

            "ExportedClockPickerProps",
            "ExportedMonthPickerProps",
            "ExportedYearPickerProps",
            "BaseDateRangePickerProps",
            -> declaration = declaration.replaceFirst(":", "<TDate>:")

            "DateIOFormats",
            -> declaration += "<TLibFormatToken: Any>"

            "IUtils",
            -> declaration += "<TDate: Any>"
        }

        val anotations = when (interfaceName) {
            "SliderValueLabelProps" -> "@Suppress(\"VIRTUAL_MEMBER_HIDDEN\")\n"
            else -> ""
        }

        anotations + declaration + " {\n" +
                propsBody +
                "\n}"
    }
}

private fun props(
    propsName: String,
    parentType: String? = null,
    hasChildren: Boolean = false,
    hasClassName: Boolean = false,
    hasSx: Boolean = false,
    hasComponent: Boolean = false,
): String {
    val baseInterfaces = mutableListOf<String>()
    if (hasChildren)
        baseInterfaces += "react.PropsWithChildren"
    if (hasClassName)
        baseInterfaces += "react.PropsWithClassName"
    if (hasSx)
        baseInterfaces += "mui.system.PropsWithSx"
    if (hasComponent)
        baseInterfaces += "mui.types.PropsWithComponent"

    val parentTypes = when {
        parentType == null
        -> if (baseInterfaces.size > 1) {
            baseInterfaces.joinToString(",\n", "\n")
        } else baseInterfaces.firstOrNull() ?: "react.Props"

        baseInterfaces.isNotEmpty()
        -> sequenceOf(parentType.removePrefix("\n"))
            .plus(baseInterfaces)
            .joinToString(",\n", "\n")

        "\n" in parentType
        -> parentType

        else -> "\n" + parentType
    }

    return "external interface $propsName: $parentTypes"
}

private fun findComponent(
    name: String,
    propsName: String,
    declaration: String,
    content: String,
    type: String = "FC",
): String? {
    if (declaration !in content)
        return null

    if (name.startsWith("use") || name.startsWith("create") || name.startsWith("z"))
        return null

    var comment = content.substringBefore("\n$declaration")
    comment = when {
        "\n\n" in comment
        -> comment.substringAfterLast("\n\n")

        ";\n/**" in comment
        -> comment.substringAfterLast(";\n")

        "}\n/**" in comment
        -> comment.substringAfterLast("}\n")

        else -> comment.substringAfterLast("};\n")
    }

    if (comment.startsWith("export "))
        comment = comment
            .substringAfterLast(";\n")
            .substringAfterLast("}\n")

    if (name == "PickersActionBar")
        comment = ""

    val typeParameter = when (propsName) {
        "CalendarPickerProps",
        "ClockPickerProps",
        "DateRangePickerProps",
        "DateRangePickerDayProps",
        "MonthPickerProps",
        "PickersDayProps",
        "YearPickerProps",

        "AutocompleteProps",
        "SelectProps",

        "MultiSelectUnstyledProps",
        "OptionUnstyledProps",
        "SelectUnstyledProps",
        -> "$propsName<*>"

        else -> propsName
    }

    return "$comment\n" +
            JS_NAME_DEFAULT +
            "external val $name: react.$type<$typeParameter>"
}
