package com.mobiledrivetech.external.common.base.extensions

import android.os.Parcel
import android.os.Parcelable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

class ParcelableExtensionsTest {
    enum class TestEnum {
        FIRST,
        SECOND,
        THIRD
    }

    // Example Parcelable implementation for testing
    class TestParcelable : Parcelable {
        override fun writeToParcel(dest: Parcel, flags: Int) {}
        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<TestParcelable> {
            override fun createFromParcel(parcel: Parcel): TestParcelable {
                return TestParcelable()
            }

            override fun newArray(size: Int): Array<TestParcelable?> {
                return arrayOfNulls(size)
            }
        }
    }

    private val parcel = mockk<Parcel>(relaxed = true)

    @Test
    fun `parcelableCreator creates Parcelable correctly`() {
        val testParcelable = TestParcelable()

        // Mock the create function
        val creator = parcelableCreator<TestParcelable> { testParcelable }

        // Mock the Parcel to provide necessary data for Parcelable creation
        every { parcel.readInt() } returns 0 // Example data

        // Use the creator to create an instance from the Parcel
        val createdObject = creator.createFromParcel(parcel)

        assertNotNull(createdObject)
    }

    @Test
    fun `parcelableClassLoaderCreator creates Parcelable correctly`() {
        val classLoader = TestParcelable::class.java.classLoader
        val testParcelable = TestParcelable()

        // Mock the create function
        val creator = parcelableClassLoaderCreator<TestParcelable> { _, _ -> testParcelable }

        // Mock the Parcel to provide necessary data for Parcelable creation
        every { parcel.readInt() } returns 0 // Example data

        // Use the creator to create an instance from the Parcel and ClassLoader
        val createdObject = creator.createFromParcel(parcel, classLoader)

        assertNotNull(createdObject)
    }

    // FIXME: unit test issue
//    @Test
//    fun `writeBoolean writes true as 1`() {
//        parcel.writeBoolean(true)
//        verify { parcel.writeInt(1) }
//    }
//
    // FIXME: unit test issue
//    @Test
//    fun `writeBoolean writes false as 0`() {
//        parcel.writeBoolean(false)
//        verify { parcel.writeInt(0) }
//    }

    @Test
    fun `test read Boolean get false`() {
        every { parcel.readInt() } returns 0
        val result = parcel.readBoolean()
        assertFalse(result)
    }

    // FIXME: unit test issue
//    @Test
//    fun `test read Boolean get true`() {
//        val parcel = spyk<Parcel>()
//        every { parcel.readInt() } returns 1
//        val result = parcel.readBoolean()
//        assertTrue(result)
//    }

    @Test
    fun `test read Enum`() {
        every { parcel.readString() } returns "FIRST"
        val result = parcel.readEnum<TestEnum>()
        assertEquals(TestEnum.FIRST, result)
    }

    @Test
    fun `test write Enum`() {
        parcel.writeEnum(TestEnum.FIRST)
        verify { parcel.writeString(any()) }
    }

    @Test
    fun `readNullable returns non-null value when readInt returns non-zero`() {
        val expectedValue = "Test String"
        every { parcel.readInt() } returns 1
        val reader: () -> String = { expectedValue }

        val result = parcel.readNullable(reader)
        assertEquals(expectedValue, result)
    }

    @Test
    fun `readNullable returns null when readInt returns zero`() {
        every { parcel.readInt() } returns 0
        val result = parcel.readNullable { "Should not be called" }
        assertNull(result)
    }

    @Test
    fun `writeNullable writes non-null value`() {
        val value = "Test String"
        parcel.writeNullable(value) { parcel.writeString(it) }

        verify { parcel.writeInt(1) }
        verify { parcel.writeString(value) }
    }

    @Test
    fun `writeNullable writes null value`() {
        parcel.writeNullable(null as String?) { parcel.writeString(it) }

        verify { parcel.writeInt(0) }
        verify(exactly = 0) { parcel.writeString(any()) }
    }

    @Test
    fun `test write and read BigInteger`() {
        val value = BigInteger("123456789")

        parcel.writeBigInteger(value)
        every { parcel.createByteArray() } returns value.toByteArray()
        every { parcel.readInt() } returns 1 // Indicating non-null value

        assertEquals(value, parcel.readBigInteger())
    }

    @Test
    fun `test write and read BigDecimal`() {
        val value = BigDecimal("123.456")
        parcel.writeBigDecimal(value)

        every { parcel.createByteArray() } returns value.unscaledValue().toByteArray()
        var readIntInvocationCount = 0
        every { parcel.readInt() } answers {
            if (readIntInvocationCount++ == 0) 1 // First call for null check
            else value.scale() // Second call for scale
        }

        assertEquals(value, parcel.readBigDecimal())
    }

    @Test
    fun `writeTypedObjectCompat writes Parcelable correctly`() {
        val value = mockk<Parcelable>(relaxed = true)
        val parcelableFlags = 0
        parcel.writeTypedObjectCompat(value, parcelableFlags)
        verify { value.writeToParcel(parcel, parcelableFlags) }
    }

    @Test
    fun `writeTypedObjectCompat writes null correctly`() {
        val parcelableFlags = 0
        parcel.writeTypedObjectCompat(null as Parcelable?, parcelableFlags)
        verify(exactly = 0) { parcel.writeParcelable(any(), any()) }
    }

    @Test
    fun `readTypedObjectCompat reads Parcelable correctly`() {
        val creator = mockk<Parcelable.Creator<Parcelable>>()
        val expectedValue = mockk<Parcelable>()

        every { parcel.readInt() } returns 1
        every { creator.createFromParcel(parcel) } returns expectedValue

        val result = parcel.readTypedObjectCompat(creator)
        assertEquals(expectedValue, result)
    }

    @Test
    fun `readTypedObjectCompat reads null correctly`() {
        every { parcel.readInt() } returns 0
        val result = parcel.readTypedObjectCompat(mockk<Parcelable.Creator<Parcelable>>())
        assertNull(result)
    }

}