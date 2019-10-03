package csvfilter

import org.assertj.core.api.Assertions.*
import org.junit.Test
import kotlin.test.assertFailsWith


class CsvFilterShould {
    val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF _cliente, NIF _cliente"

    @Test
    fun correct_lines_are_not_filtered(){

        val invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,"

        val result = CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine, invoiceLine))

    }

    @Test
    fun tax_fields_are_mutually_exclusive(){

        val invoiceLine = "1,02/05/2019,1000,810,XYZ,,ACER Laptop,B76430134,"

        val result = CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine))
    }

    @Test
    fun there_must_be_at_least_one_tax_for_the_invoice(){

        val invoiceLine = "1,02/05/2019,1000,810,,,ACER Laptop,B76430134,"

        val result = CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine))
    }

    @Test
    fun tax_fields_must_be_decimals_and_exclusive(){
        val invoiceLine = "1,02/05/2019,1000,810,XYZ,12,ACER Laptop,B76430134,"

        val result =  CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine))
    }

    @Test
    fun net_field_not_correct_with_iva(){
        val invoiceLine = "1,02/05/2019,1000,800,19,,ACER Laptop,B76430134,"

        val result =  CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine))
    }

    @Test
    fun net_field_not_correct_with_igic(){
        val invoiceLine = "1,02/05/2019,1000,925,,7,ACER Laptop,B76430134,"

        val result =  CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(headerLine))
    }

    @Test
    fun list_Empty_return_list_Empty(){
        val invoiceLine = ""

        val result =  CsvFilter().filter(listOf(headerLine, invoiceLine))

        assertThat(result).isEqualTo(listOf(""))
    }









}