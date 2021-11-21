package tasks.firstFollow

fun kProduct(leftStrings: Set<TermString>, rightStrings: Set<TermString>, k: Int): Set<TermString> {
    val result = mutableSetOf<TermString>()
    for (left in leftStrings) {
        for (right in rightStrings) {
            result.add((left + right).take(k))
        }
    }
    return result
}