package karakum.mui.adapters

fun String.adaptUseSlider(): String {
    return replace(
        """export interface AxisProps<T extends Axis> {
    offset: (percent: number) => T extends 'horizontal' ? {
        left: string;
    } : T extends 'vertical' ? {
        bottom: string;
    } : T extends 'horizontal-reverse' ? {
        right: string;
    } : never;
    leap: (percent: number) => T extends 'horizontal' | 'horizontal-reverse' ? {
        width: string;
    } : T extends 'vertical' ? {
        height: string;
    } : never;
}
""",
        "",
    )
}
