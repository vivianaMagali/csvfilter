package csvfilter

class CsvFilter {
    fun apply(lines: List<String>): List<String> {

        if (lines.size == 0) return listOf("")

        if (lines.size == 1) throw IllegalArgumentException("no tiene cabecera") as Throwable

        val result = mutableListOf<String>()
        result.add(lines[0])
        val invoice = lines[1]

        val fields = invoice.split(',')

        val numberInvoiceIndex = 0
        val dateInvoiceIndex = 1

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

        val dateField = fields[dateInvoiceIndex]

        val idField = fields[numberInvoiceIndex]

        val decimalRegex = "\\d+(\\.\\d+)?".toRegex()
        val cifRegex = "^[A-Za-z]\\d{7}([A-Za-z]|\\d)".toRegex()
        val nifRegex = "\\d{8}[A-Za-z]".toRegex()
        val idRegex = "\\d".toRegex()
        val dateRegex = "^([0-2][0-9]|3[0-1])(\\/|-)(0[1-9]|1[0-2])\\2(\\d{4})\$".toRegex()

        val taxFieldsAreMutuallyExclusive = (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                (ivaField.isNullOrEmpty() xor igicField.isNullOrEmpty())

        val idFieldsMustBeExclusive = (cifField.isNullOrEmpty() xor nifField.isNullOrEmpty())

        val idFieldsAreGoodFormat = (cifField.matches(cifRegex) || nifField.matches(nifRegex) )

        val grossFieldIsDecimal = grossField.matches(decimalRegex) 

        val netFieldIsDecimal = netField.matches(decimalRegex)

        val idFieldIsInteger = idField.matches(idRegex)

        val dateFieldIsValidated = dateField.matches(dateRegex)

        if (taxFieldsAreMutuallyExclusive && idFieldsMustBeExclusive && idFieldsAreGoodFormat &&
            grossFieldIsDecimal && netFieldIsDecimal && idFieldIsInteger && dateFieldIsValidated) {

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
