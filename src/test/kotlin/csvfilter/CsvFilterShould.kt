package csvFilter

import csvfilter.CsvFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class CsvFilterShould {
    private val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente"
    lateinit var filter: CsvFilter
    private val emptyDataFile = listOf(headerLine)

    @Before
    fun setup() {
        filter = CsvFilter()
    }

    @Test
    fun correct_lines_are_not_filtered() {

        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().concept("this invoice is good format").build().toString()))

        assertThat(result).isEqualTo(listOf(headerLine,CsvInvoice.Builder().concept("this invoice is good format").build().toString()))
    }

    @Test
    fun tax_fields_are_mutually_exclusive() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().ivaTax("18").igicTax("4").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun there_must_be_at_least_one_tax_for_the_invoice() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().ivaTax("18").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun tax_fields_must_be_decimals() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().ivaTax("ABC").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun tax_fields_must_be_decimals_and_exclusive() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().ivaTax("AVC").igicTax("GED").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }


    @Test
    fun net_field_not_correct_with_iva() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().ivaTax("18").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }


    @Test
    fun net_field_not_correct_with_igic() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().igicTax("4").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun correct_lines_must_have_hearder() {
       // val result = filter.apply(listOf("",CsvInvoice.Builder().ivaTax("18").igicTax("4").build().toString()))
        assertFailsWith<IllegalArgumentException> { filter.apply(listOf(CsvInvoice.Builder().build().toString())) }
    }

    @Test
    fun ids_fields_must_good_format_and_exclusive() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().cif("").nif("EDSA4").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun gross_field_must_be_decimal() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().grossAmount("ABVD").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun net_field_must_be_decimal() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().netAmount("ASDF").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun num_invoice_field_must_be_integer() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().invoiceId("ABVD").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun date_field_must_be_good_format() {
        val result = filter.apply(listOf(headerLine,CsvInvoice.Builder().invoiceDate("10/02-2345").build().toString()))

        assertThat(result).isEqualTo(emptyDataFile)
    }


}





