package csvfilter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CsvFilterShould {

    @Test
    fun correct_lines_are_not_filtered(){
        val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF _cliente, NIF _cliente"
        val invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134"

        val result = CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine, invoiceLine))

    }

}