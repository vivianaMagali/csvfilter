package csvFilter
import csvfilter.CsvFilter
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class CsvFilterShould {
    private val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente"
    lateinit var filter: CsvFilter
    private val emptyDataFile = listOf(headerLine)
    private val emptyField = ""

    @Before
    fun setup() {
        filter = CsvFilter()
    }

    @Test
    fun correct_lines_are_not_filtered() {
        val lines = fileWithOneInvoiceLineHaving(concept = "a correct line with irrelevant data")
        val result = filter.apply(lines)

        assertThat(result).isEqualTo(lines)
    }

    @Test
    fun tax_fields_are_mutually_exclusive() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = "19", igicTax = "8"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun there_must_be_at_least_one_tax_for_the_invoice() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = emptyField, igicTax = emptyField))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun tax_fields_must_be_decimals() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = "XYZ", igicTax = emptyField))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun tax_fields_must_be_decimals_and_exclusive() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = "XYZ", igicTax = "12"))

        assertThat(result).isEqualTo(emptyDataFile)
    }


    @Test
    fun net_field_not_correct_with_iva() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(igicTax = emptyField, ivaTax = "18"))

        assertThat(result).isEqualTo(emptyDataFile)
    }


    @Test
    fun net_field_not_correct_with_igic() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(igicTax = "4", ivaTax = emptyField))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun correct_lines_must_have_hearder() {
        assertFailsWith<IllegalArgumentException> { CsvFilter().apply(listOf("")) }
    }

    @Test
    fun ids_fields_must_good_format_and_exclusive() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(cif = emptyField, nif = "5458573E"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun gross_field_must_be_decimal() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(grossAmount = "ABCD"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun net_field_must_be_decimal() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(netAmount = "ABCD"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun num_invoice_field_must_be_integer() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(invoiceId = "ABCD"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun date_field_must_be_good_format() {
        val result = filter.apply(fileWithOneInvoiceLineHaving(invoiceDate = "50-04/asd2019"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    private fun fileWithOneInvoiceLineHaving(
        ivaTax: String = "19", igicTax: String = emptyField, concept: String = "irrelevant",
        invoiceId: String = "1", invoiceDate: String = "02/05/2019", grossAmount: String = "1000",
        netAmount: String = "810", cif: String = "B76430134", nif: String = emptyField
    ): List<String> {

        val formattedLine = listOf(
            invoiceId,
            invoiceDate,
            grossAmount,
            netAmount,
            ivaTax,
            igicTax,
            concept,
            cif,
            nif
        ).joinToString(",")
        return listOf(headerLine, formattedLine)
    }
}





