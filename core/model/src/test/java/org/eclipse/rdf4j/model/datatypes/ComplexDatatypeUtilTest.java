package org.eclipse.rdf4j.model.datatypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.CDT;
import org.junit.Test;

/**
 * Unit tests on {@link org.eclipse.rdf4j.model.datatypes.ComplexDatatypeUtil}
 * 
 * @author Yana Soares de Paula
 */
public class ComplexDatatypeUtilTest {

	private static final String[] VALID_CARTESIANS = { 
			"4.0+j7",
			"5 - 8j",
			"3 + i9", 
			"2.0 - 7i", 
			"-7i +		8",
			"+i2 - 6.0", 
			"-j5+0.00001", 
			"-j5+ 2e2", 
			"7j - 2",
			"+7i",
			"-34",
			"20",
			"4e5 + 1j"};
	
	private static final String[] INVALID_CARTESIANS = { 
			"4.0++j7",
			"5 -8j-",
			"3,908e3 + i9", 
			"2,0 - 7i", 
			"-7ii +		8",
			"+i2j - 6.0", 
			"-j5+0.00001e", 
			"-j5+2+e2", 
			"e7j - 2",
			"+7ib",
			"-34+0",
			"-+20",
			"aaaaaaaaa 	aaaa"};
	
	private static final String[] VALID_POLARS = { 
			"(3e5; 	90[deg])",
			"(8.6, 3.245 [pi]	[rad])",
			"(1.85	;	232 [deg])", 
			"(35,  -3[pi][rad])"};
	
	private static final String[] INVALID_POLARS = { 
			"(3e5; 	90[deg][rad])",
			"(8.6, 324.5 [pi]	[pi][rad])",
			"(1.85[rad]	;	232)", 
			"(35,  -180[pi][deg])",
			"35, [pi][rad]",
			"5-20, 50[deg]°"};

	/**
	 * Test method for
	 * {@link org.eclipse.rdf4j.model.datatypes.ComplexDatatypeUtil#isValidValue(java.lang.String, org.eclipse.rdf4j.model.IRI)}
	 * .
	 */
	@Test
	public void testIsValidValue() {

		testValidation(VALID_CARTESIANS, CDT.COMPLEXCARTESIAN, true);		
		testValidation(INVALID_CARTESIANS, CDT.COMPLEXCARTESIAN, false);
		
		testValidation(VALID_POLARS, CDT.COMPLEXPOLAR, true);		
		testValidation(INVALID_POLARS, CDT.COMPLEXPOLAR, false);
	}
	
	private void testValidation(String[] values, IRI datatype, boolean validValues) {
		for (String value : values) {
			boolean result = ComplexDatatypeUtil.isValidValue(value, datatype);
			if (validValues) {
				if (!result) {
					fail("value " + value + " should have validated for type " + datatype);
				}
			} else {
				if (result) {
					fail("value " + value + " should not have validated for type " + datatype);
				}
			}
		}
	}
	
	@Test
	public void testNormalize() {
		assertEquals("8.0 - 7.0i", ComplexDatatypeUtil.normalize("-7i +		8", CDT.COMPLEXCARTESIAN));
		assertEquals("1.0E-5 - 5.0i", ComplexDatatypeUtil.normalize("-j5+0.00001", CDT.COMPLEXCARTESIAN));
		assertEquals("-34.0 + 0.0i", ComplexDatatypeUtil.normalize("-34", CDT.COMPLEXCARTESIAN));
		assertEquals("5.0 + 3.0i", ComplexDatatypeUtil.normalize("5 + 3j", CDT.COMPLEXCARTESIAN));
		assertEquals("5.0 + 8.0E-7i", ComplexDatatypeUtil.normalize("5   + 8e-7j", CDT.COMPLEXCARTESIAN));
		assertEquals("-0.005 + 8.0i", ComplexDatatypeUtil.normalize("+ 8j - 5e-3   ", CDT.COMPLEXCARTESIAN));
		
		// There are possible problems concerning rounding of numbers because of Apacha Commons Math Complex functions :
		assertEquals("30.021011266294206 + 39.98423292435452i", ComplexDatatypeUtil.normalize("(50,53.1[deg])", CDT.COMPLEXPOLAR));
	}
	
	@Test
	public void testParseComplex() {
		for (String value : VALID_CARTESIANS) {
			ComplexDatatypeUtil.parseComplex(value);
		}
		for (String value : VALID_POLARS) {
			ComplexDatatypeUtil.parseComplex(value);
		}
	}
	
	@Test
	public void testCompareComplex() {
		ComplexDatatypeUtil.compareComplex("-j5+0.00001", "1.0e-5 - 5.0i");
		ComplexDatatypeUtil.compareComplex("(3; 	180[deg])", "(3.0 , 1 [pi][rad])");
		
	}
	
}