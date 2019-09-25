package csvfilter

class CsvFilter {
    fun filter(lines: List<String>): List<String> {
        val result = mutableListOf<String>()
        result.add(lines[0])
        val invoice = lines[1]
        val fields=invoice.split(',')
        val ivaFieldIndex = 4
        val igicFieldIndex = 5
        val grossFieldIndex = 2
        val netFieldIndex = 3
        val ivaField = fields[ivaFieldIndex]
        val igicField = fields[igicFieldIndex]
        val netField = fields[netFieldIndex]
        val grossField = fields[grossFieldIndex]

        val decimalRegex = "\\d+(\\.\\d+)?".toRegex()
        val taxFieldsAreMutuallyExclusive = (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                (ivaField.isNullOrEmpty() || igicField.isNullOrEmpty())


        if (taxFieldsAreMutuallyExclusive){
            var resultado = (grossField.toBigDecimal() * ivaField.toBigDecimal())/100.toBigDecimal()
            var suma = resultado + netField.toBigDecimal()
            if(suma==grossField.toBigDecimal()){
                result.add(lines[1])
            }
        }
        return result.toList()
    }

}

fun main(){
    CsvFilter().filter(listOf("Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF _cliente, NIF _cliente","1,02/05/2019,1000,800,19,,ACER Laptop,B76430134,"))

}