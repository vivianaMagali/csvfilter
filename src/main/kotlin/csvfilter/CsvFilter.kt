package csvfilter

class CsvFilter {

    private val numberInvoiceIndex = 0
    private val dateInvoiceIndex = 1
    private val grossFieldIndex = 2
    private val netFieldIndex = 3
    private val ivaFieldIndex = 4
    private val igicFieldIndex = 5
    private val cifFielIndex = 7
    private val nifFieldIndex = 8



    fun apply(lines: List<String>): List<String> {

        if (lines.size == 0) return listOf("")
        if (lines.size == 1) throw IllegalArgumentException("no tiene cabecera") as Throwable

        val result = mutableListOf<String>()
        result.add(lines[0])
        val invoice = lines[1]

        if (invoice.isNullOrEmpty()) return listOf("")

        if (correctFormatOfFields(invoice) && taxGoodCalculated(invoice)) {
            result.add(lines[1])
        }
        return result.toList()
    }

    fun taxGoodCalculated(invoice: String): Boolean{
        val fields = invoice.split(',')
        val netField = fields[netFieldIndex]
        val grossField = fields[grossFieldIndex]
        val ivaField = fields[ivaFieldIndex]
        val igicField = fields[igicFieldIndex]

        val tax = if ( ivaField.isNullOrEmpty() ) igicField.toBigDecimal() else ivaField.toBigDecimal()

        var Value = (grossField.toBigDecimal() * tax) / 100.toBigDecimal()
        var grossValue = Value + netField.toBigDecimal()
        if ( grossValue == grossField.toBigDecimal() )  return true

        return false
    }


    fun correctFormatOfFields(invoice: String) : Boolean {
        val fields = invoice.split(',')

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

         return (taxFieldsAreMutuallyExclusive && idFieldsMustBeExclusive && idFieldsAreGoodFormat &&
                grossFieldIsDecimal && netFieldIsDecimal && idFieldIsInteger && dateFieldIsValidated)

    }

}
