package csvfilter

class CsvFilter {
    fun filter(lines: List<String>): List<String> {

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

        if(invoice.isNullOrEmpty() ){ return listOf("") }

        val netField = fields[netFieldIndex]
        val grossField = fields[grossFieldIndex]
        val ivaField = fields[ivaFieldIndex]
        val igicField = fields[igicFieldIndex]
        val cifField = fields[cifFielIndex]
        val nifField = fields[nifFieldIndex]

        val decimalRegex = "\\d+(\\.\\d+)?".toRegex()
        val taxFieldsAreMutuallyExclusive = (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                (ivaField.isNullOrEmpty() || igicField.isNullOrEmpty())

        val idFieldsMustBeExclusive = (cifField.isNullOrEmpty() || nifField.isNullOrEmpty()) &&
                (!(cifField.isNullOrEmpty() && nifField.isNullOrEmpty()))


        if (taxFieldsAreMutuallyExclusive && idFieldsMustBeExclusive){

            if(!ivaField.isNullOrEmpty()){
                var ivaValue = (grossField.toBigDecimal() * ivaField.toBigDecimal())/100.toBigDecimal()
                var grossValueWithIva = ivaValue + netField.toBigDecimal()

                if(grossValueWithIva==grossField.toBigDecimal()){ result.add(lines[1]) }
            }

           if(!igicField.isNullOrEmpty()){
               var igicValue = (grossField.toBigDecimal() * igicField.toBigDecimal())/100.toBigDecimal()
               var grossValueWithIgic = igicValue + netField.toBigDecimal()

               if(grossValueWithIgic==grossField.toBigDecimal()){ result.add(lines[1]) }
           }

        }
        return result.toList()
    }





}
