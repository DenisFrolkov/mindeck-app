package com.mindeck.core.ui.format

import org.junit.Assert.assertEquals
import org.junit.Test

class FileSizeTest {
    @Test
    fun `size below one kilobyte is shown in bytes`() {
        assertEquals(FileSizeUnit.BYTES, formatFileSize(1023).unit)
    }

    @Test
    fun `exactly one kilobyte is shown in kilobytes`() {
        assertEquals(FileSizeUnit.KILOBYTES, formatFileSize(1024).unit)
    }

    @Test
    fun `size that stays below 1024 kilobytes after rounding is shown in kilobytes`() {
        assertEquals(FileSizeUnit.KILOBYTES, formatFileSize(1_048_524).unit)
    }

    @Test
    fun `size that rounds up to 1024 kilobytes is shown in megabytes`() {
        assertEquals(FileSizeUnit.MEGABYTES, formatFileSize(1_048_525).unit)
    }

    @Test
    fun `size just below one megabyte is shown in megabytes`() {
        assertEquals(FileSizeUnit.MEGABYTES, formatFileSize(1_048_575).unit)
    }

    @Test
    fun `exactly one megabyte is shown in megabytes`() {
        assertEquals(FileSizeUnit.MEGABYTES, formatFileSize(1_048_576).unit)
    }
}
