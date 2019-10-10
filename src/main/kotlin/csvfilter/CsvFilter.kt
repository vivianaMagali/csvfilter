package csvfilter

class CsvFilter {
    fun apply(lines: List<String>): List<String> {

        if (lines.size == 0) return listOf("")
        if (lines.size == 1) throw IllegalArgumentException("no tiene cabecera") as Throwable

        val result = mutableListOf<String>()
        result.add(lines[0])
        val invoice = lines[1]

        val fields = invoice.split(',')
        val grossFieldIndex = 2
        val netFieldIndex = 3
        val ivaFieldIndex = 4
        val igicFieldIndex = 5
        val cifFielIndex = 7
        val nifFieldIndex = 8

        if (invoice.isNullOrEmpty()) {
            return listOf("")
        }

        val netField = fields[netFieldIndex]
        val grossField = fields[grossFieldIndex]
        val ivaField = fields[ivaFieldIndex]
        val igicField = fields[igicFieldIndex]
        val cifField = fields[cifFielIndex]
        val nifField = fields[nifFieldIndex]

        val decimalRegex = "\\d+(\\.\\d+)?".toRegex()
        val cifRegex = "^[A-Za-z]\\d{7}([A-Za-z]|\\d)".toRegex()
        val nifRegex = "\\d{8}[A-Za-z]".toRegex()

        val taxFieldsAreMutuallyExclusive = (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                (ivaField.isNullOrEmpty() xor igicField.isNullOrEmpty())

        val idFieldsMustBeExclusive = (cifField.isNullOrEmpty() xor nifField.isNullOrEmpty())

        val idFieldsAreGoodFormat = (cifField.matches(cifRegex) || nifField.matches(nifRegex) )

        if (taxFieldsAreMutuallyExclusive && idFieldsMustBeExclusive && idFieldsAreGoodFormat) {

            val tax = if (ivaField.isNullOrEmpty()) igicField.toBigDecimal() else ivaField.toBigDecimal()

            var Value = (grossField.toBigDecimal() * tax) / 100.toBigDecimal()
            var grossValueWithIva = Value + netField.toBigDecimal()

            if (grossValueWithIva == grossField.toBigDecimal()) {
                result.add(lines[1])
            }

        }
        return result.toList()
    }

}