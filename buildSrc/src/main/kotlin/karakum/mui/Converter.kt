package karakum.mui

import java.io.File

internal data class ConversionResult(
    val main: String,
    val extensions: String,
)

internal fun convertClasses(
    classesName: String,
    definitionFile: File,
): String {
    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val source = content.substringAfter("export interface $classesName {\n", "")

    if (source.isEmpty()) {
        check(classesName == "ContainerClasses" || classesName == "StackClasses")
        return "typealias $classesName = mui.system.$classesName"
    }

    return "sealed external interface $classesName {\n${getClassesContent(source)}\n}\n" +
            "\n" +
            "external val ${classesName.replaceFirstChar(Char::lowercase)}: $classesName\n"
}

private fun getClassesContent(
    source: String,
): String = source
    .substringBefore("\n}\n")
    .trimIndent()
    .splitToSequence("\n")
    .map {
        val name = it.removeSuffix(": string;").removeSuffix("?")
        if (name == it) return@map it
        val line = "val $name: ClassName"
        if (name.startsWith("'")) "    // $line" else line
    }
    .joinToString("\n")

internal fun convertDefinitions(
    definitionFile: File,
): ConversionResult {
    val name = definitionFile.name.substringBefore(".")
        .removeSuffix("Props")

    val (content, defaultUnions) = definitionFile.readText()
        .replace("\r\n", "\n")
        .adaptRawContent()
        .removeInlineClasses()
        .removeDeprecated()
        .removeExtendsEmptyObject()
        .replace("(inProps: ", "(props: ")
        .replace(
            "interface PopperProps extends Omit<BasePopperProps, 'direction'>",
            "interface PopperProps extends BasePopperProps",
        )
        .replace(
            "StandardProps<Omit<PopoverProps, 'slots' | 'slotProps'>, 'children'>",
            "StandardProps<PopoverProps>",
        )
        .replace(
            "StandardProps<Omit<ModalProps, 'slots' | 'slotProps'>, 'children'>",
            "StandardProps<ModalProps>",
        )
        .replace(
            "\ninterface ${name}OwnProps {\n",
            "\nexport interface ${name}OwnProps {\n",
        )
        .replace(
            "extends Omit<TypographyTypeMap['props'], 'classes'>",
            "extends TypographyProps",
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
    val fun3Declaration = "declare function $name(props: $propsName): React.JSX.Element;"
    val typeDeclaration = "declare const $name: React.ComponentType<$propsName>;"
    val const1Declaration = "declare const $name: "
    val const2Declaration = "export default _default"

    declarations += listOfNotNull(
        findComponent(name, propsName, fun0Declaration, content),
        findComponent(name, propsName, fun1Declaration, content),
        findComponent(name, propsName, fun2Declaration, content),
        findComponent(name, propsName, fun3Declaration, content),
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

    return substringAfter(substringBefore("export interface UseMediaQueryOptions {"))
}

private fun String.removeExtendsEmptyObject(): String {
    // TODO: Probably need to replace not only for `TValue` but for all others
    return replace(
        "<TValue extends {}>",
        "<TValue>",
    )
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

        propsContent.startsWith("OptionValue>") || propsContent.startsWith("OptionValue extends ")
        -> "$propsName<OptionValue>"

        propsContent.startsWith("TOption>") || propsContent.startsWith("TOption extends ")
        -> "$propsName<TOption>"

        propsContent.startsWith("T = unknown>")
        -> "$propsName<T>"

        propsContent.startsWith("Value = unknown>")
        -> "$propsName<Value>"

        propsName == "AutocompleteProps"
        -> "$propsName<Value>"

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
        .let { str ->
            sequenceOf(
                // todo remove when mui migrates on DefaultComponent generic in all places
                str.substringAfter(" D extends React.ElementType = '", ""),
                str.substringAfter(" DefaultComponent extends React.ElementType = '", ""),
                str.substringAfter(" RootComponent extends React.ElementType = '", ""),
            ).maxByOrNull { it.length }!!
        }

        .substringBefore("'", "")

    if (intrinsicType.isEmpty()) {
        intrinsicType = propsContent
            .substringBefore(" {\n")
            .substringAfter(" RootComponentType extends React.ElementType = '", "")
            .substringBefore("'", "")
    }

    if (name == "AppBar")
        intrinsicType = "div"

    if (name == "Tab")
        intrinsicType = "button"

    val parentType: String? = when {
        "${name}BaseProps & {" in propsContent || "props: AdditionalProps & ${name}BaseProps;" in propsContent
        -> {
            val baseType = "${name}BaseProps"

            if (baseType == "LinkBaseProps") {
                // TODO: Also we need to provide `baseType` here too
                //  Details: But now we skip `baseType`(`LinkBaseProps`) of `LinkProps` because in truth `LinkBaseProps` = `TypographyOwnProps` but there are some inheritance problem  with this type
                INTRINSIC_TYPE_MAP[intrinsicType]
            } else {
                if (name.startsWith("List")) {
                    sequenceOf(baseType, "react.dom.html.HTMLAttributes<web.html.HTMLElement>")
                        .joinToString(",\n", "\n")
                } else baseType
            }
        }

        "props: ${name}OwnProps & AdditionalProps;" in propsContent
        -> {
            val intrinsicProps = when (propsName) {
                "InputProps" -> "react.dom.html.HTMLAttributes<web.html.HTMLInputElement>"
                else -> INTRINSIC_TYPE_MAP[intrinsicType]
            }

            sequenceOf("${name}OwnProps", intrinsicProps)
                .joinToString(",\n", "\n")
        }

        "props: AdditionalProps & ${name}OwnProps" in propsContent || "props: AdditionalProps &\n    ${name}OwnProps" in propsContent
        -> {
            val intrinsicProps = INTRINSIC_TYPE_MAP[intrinsicType]

            sequenceOf(
                "mui.material.ButtonProps".takeIf { name == "LoadingButton" },
                "${name}OwnProps",
                intrinsicProps,
            ).filterNotNull().joinToString(",\n", "\n")
        }

        "DistributiveOmit<PaperOwnProps" in propsContent
        -> "PaperOwnProps"

        "${name}TypeMap<{" in propsContent
        -> "mui.base.${name}Props"

        else -> INTRINSIC_TYPE_MAP[intrinsicType]
    }

    val membersContent = propsContent
        .let { str ->
            sequenceOf(
                // todo remove when mui migrates on AdditionalProps generic in all places
                str.substringAfter("props: P", ""),
                str.substringAfter("props: AdditionalProps", ""),
            ).maxByOrNull { it.length }!!
        }
        .substringAfter(" & {\n", "")
        .let { str ->
            sequenceOf(
                str.substringBefore(";\n    } &", ""),
                str.substringBefore(";\n    };", ""),
                str.substringBefore(";\n  } &", ""),
                str.substringBefore(";\n  };", ""),
            ).maxByOrNull { it.length }!!
        }

    val hasComponent = ": OverridableComponent<" in content
            || "&\n  OverridableComponent<" in content
            || "& {\n  component?: React.ElementType;\n};" in content

    return if (membersContent.isNotEmpty()) {
        val body = convertMembers(membersContent)
        props(
            propsName = propsName,
            parentType = parentType,
            hasChildren = CHILDREN in body,
            hasClassName = CLASS_NAME in body,
            hasSx = SX in body,
            hasComponent = hasComponent,
        ) + " {\n$body\n}"
    } else {
        props(
            propsName = propsName,
            parentType = if (propsName == "TabListProps") "mui.material.TabsProps" else parentType,
            hasComponent = hasComponent,
        )
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
            "ListItemButtonOwnProps",

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
            -> propsBody = propsBody.replace(": String", ": web.cssom.Color")
        }

        when (parentType) {
            "mui.system.ThemeOptions",
            "mui.system.Theme",
            "BaseTheme",
            -> propsBody = sequenceOf(
                "mixins",
                "components",
                "slots",
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
            -> declaration = declaration.replaceFirst(":", "<Value>:")

            "SelectOption",
            "SelectOptionDefinition",
            "UseOptionParameters",
            "AutocompleteChangeDetails",
            "UseAutocompleteRenderedOption",
            "UseAutocompleteReturnValue",
            "CreateFilterOptionsConfig",
            "FilterOptionsState",
            -> declaration += "<Value>"

            "BrowserAutofillAction",
            -> declaration += "<OptionValue>"

            "UseSelectSingleParameters",
            "UseSelectMultiParameters",
            "UseSelectSingleResult",
            "UseSelectMultiResult",
            -> declaration += "<TValue>"

            "ListState",
            -> declaration += "<ItemValue>"

            "UseListParameters",
            -> declaration += "<ItemValue, State, CustomAction, CustomActionContext>"

            "UseSelectParameters",
            -> declaration += "<OptionValue, Multiple>"

            "UseSelectReturnValue",
            -> declaration += "<Value, Multiple>"

            "SelectOwnerState",
            -> declaration = declaration.replaceFirst(
                "SelectOwnerState",
                "SelectOwnerState<TValue> : SelectOwnProps<TValue>"
            )

            "OptionOwnProps",
            "SelectOwnProps",
            -> declaration = declaration.replaceFirst(":", "<OptionValue>:")

            "OptionProps",
            -> declaration = declaration.replaceFirst(":", "<TValue>: OptionProps<TValue>")

            "SelectProps",
            -> declaration = declaration.replaceFirst(":", "<TValue>: SelectProps<TValue>")

            "ExportedClockPickerProps",
            "ExportedMonthPickerProps",
            "ExportedYearPickerProps",
            "BaseDateRangePickerProps",
            -> declaration = declaration.replaceFirst(":", "<TDate>:")

            "DateIOFormats",
            -> declaration += "<TLibFormatToken: Any>"

            "IUtils",
            -> declaration += "<TDate: Any>"

            "UseSelectResult",
            -> declaration = declaration.replaceFirst("UseSelectResult", "UseSelectResult<TValue>")

            "UseListboxParameters",
            -> declaration = declaration.replaceFirst("UseListboxParameters", "UseListboxParameters<TOption>")

            "ListboxState",
            -> declaration = declaration.replaceFirst("ListboxState", "ListboxState<TOption>")
        }

        val annotations = when (interfaceName) {
            "SliderValueLabelProps" -> "@Suppress(\"VIRTUAL_MEMBER_HIDDEN\")\n"
            else -> ""
        }

        annotations + declaration + " {\n" +
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

    if (propsName == "InputOwnProps")
        baseInterfaces += "InputBaseProps"

    if (propsName in setOf("FormLabelProps", "InputLabelProps"))
        baseInterfaces += "FormLabelOwnProps"

    if (propsName == "ModalProps" && hasClassName)
        baseInterfaces += "mui.base.ModalProps"

    if (propsName == "AppBarProps" || propsName == "AccordionProps")
        baseInterfaces += "PaperProps"

    if (propsName == "TreeViewProps")
        baseInterfaces += "TreeViewPropsBase"

    if (
    // TODO: Commented props has conflicts by intrinsic types
        propsName in setOf(
//            "AccordionSummaryProps",
            "BottomNavigationActionProps",
            "CardActionAreaProps",
            "IconButtonProps",
            "FabProps",
//            "ListItemProps",
//            "ListItemButtonProps",
//            "MenuItemProps",
            "StepButtonProps",
//            "TabProps",
//            "TableSortLabelProps",
            "ToggleButtonProps",
        )
    ) {
        baseInterfaces += "mui.material.ButtonBaseProps"
    }

    if (propsName == "PopperProps" && hasSx)
        baseInterfaces += "mui.base.PopperProps"

    if (propsName == "StepProps")
        baseInterfaces += "mui.system.StandardProps"

    if (propsName == "StepperProps")
        baseInterfaces += listOf("mui.system.StandardProps", "PaperProps")

    if (propsName == "InputLabelOwnProps")
        baseInterfaces += "react.PropsWithChildren"

    if (propsName == "AlertProps")
        baseInterfaces += "AlertSlots"

    baseInterfaces
        .tryToAddInheritanceInterfaces(propsName)

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

// TODO: This is a WA for inheritance error. Try to avoid somehow...
//  `Types of inherited var-properties do not match:
//  public abstract var classes: AppBarClasses? defined in mui.material.AppBarProps,
//  public abstract var classes: PaperClasses? defined in mui.material.PaperProps`
private fun MutableList<String>.tryToAddInheritanceInterfaces(
    propsName: String,
) {
    if (propsName == "AppBarOwnProps")
        this += "PaperProps"

    if (propsName == "BottomNavigationActionOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "CardActionAreaOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "CardOwnProps")
        this += "PaperOwnProps"

    if (propsName == "CardHeaderOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("div")

    if (propsName == "ChipOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("div")

    if (propsName == "FabOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "IconButtonOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "InputLabelOwnProps")
        this += "FormLabelOwnProps"

    if (propsName == "MenuItemOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("li")

    if (propsName == "StepButtonOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "StepperOwnProps")
        this += "PaperProps"

    if (propsName == "SvgIconOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("svg")

    if (propsName == "TabOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("button")

    if (propsName == "ToggleButtonOwnProps")
        this += "ButtonBaseProps"
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

        "MultiSelectProps",
        "OptionProps",
        -> "$propsName<*>"

        // TODO: Remove when `TreeItem` will be removed from `@mui/lab`
        "TreeItemProps",
        -> "muix.tree.view.TreeItemProps"

        "TreeViewProps",
        -> "muix.tree.view.TreeViewProps"

        else -> propsName
    }

    val jsNameDefault = optionalJsNameDefaultAnnotation(content)

    return "$comment\n" +
            jsNameDefault +
            "external val $name: react.$type<$typeParameter>"
}

private fun optionalJsNameDefaultAnnotation(
    content: String,
): String =
    if ("export default" in content || "export { default " in content) {
        "@JsName(\"default\")\n"
    } else {
        ""
    }
