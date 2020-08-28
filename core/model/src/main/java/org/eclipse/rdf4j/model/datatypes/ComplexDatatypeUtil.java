/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.model.datatypes;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.complex.ComplexUtils;
import org.apache.commons.math3.util.FastMath;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.CDT;

/**
 * Provides methods for handling the cdt:complex datatype.
 *
 * @author Yana Soares de Paula
 */
public class ComplexDatatypeUtil {

	/*--------------------*
	 * RegEx declarations *
	 *--------------------*/
	
	// Groups of decimals
	final static String decimal = "\\p{Digit}+(?:\\.\\p{Digit}*)?(?:[eE][+-]?\\p{Digit}+)?";
	
	// RegEx to cartesian complex notations
	final static String cartesian = 
			// Cartesian notations accepted
			("(([+-]?" + decimal + ")" + "([+-]" + decimal + "[ij]))|" +	// ex : -7 + 4j
			"(([+-]?" + decimal + ")" + "([+-][ij]" + decimal + "))|" +	// ex : -7 + i4
			"(([+-]?[ij]" + decimal + ")" + "([+-]" + decimal + "))|" +	// ex : i4 - 7
			"(([+-]?" + decimal + "[ij])" + "([+-]" + decimal + "))|" +	// ex : 4j - 7
					
			// Pure imaginary number
			"([+-]?[ij]" + decimal + ")|" + 
			"([+-]?" + decimal + "[ij])|" + 
			
			// Pure real number
			"([+-]?" + decimal + ")"); 
	
	// RegEx to polar complex notations
	final static String polar = "\\((" + decimal + ")[;,]([+-]?" + decimal + ")((?:\\[pi\\])?\\[(?:rad|deg)\\])\\)";
	
	
	/*-------------------*
	 * Datatype checking *
	 *-------------------*/

	/**
	 * Checks whether the supplied datatype is a main cdt:ucum datatype.
	 *
	 * @param datatype
	 * @return true if the datatype is a main type
	 */
	public static boolean isMainDatatype(IRI datatype) {
		return datatype.equals(CDT.UCUM) || datatype.equals(CDT.UCUMUNIT);
	}

	/**
	 * Checks whether the supplied datatype is a specific cdt:ucum datatype. Only complex datatypes are applied in this version.
	 *
	 * @param datatype
	 * @return true if the datatype is a specific type
	 */
	public static boolean isSpecificDatatype(IRI datatype) {
		return datatype.equals(CDT.COMPLEX) || datatype.equals(CDT.COMPLEXCARTESIAN)
				|| datatype.equals(CDT.COMPLEXPOLAR) 
//				|| datatype.equals(CDT.AMOUNTOFSUBSTANCE)
//				|| datatype.equals(CDT.ANGLE) || datatype.equals(CDT.AREA)
//				|| datatype.equals(CDT.CATALYTICACTIVITY) || datatype.equals(CDT.ACCELERATION)
//				|| datatype.equals(CDT.DIMENSIONLESS) || datatype.equals(CDT.ELECTRICCAPACITANCE)
//				|| datatype.equals(CDT.ELECTRICCHARGE) || datatype.equals(CDT.ELECTRICCONDUCTANCE)
//				|| datatype.equals(CDT.ELECTRICPOTENTIAL) || datatype.equals(CDT.ELECTRICRESISTANCE)
//				|| datatype.equals(CDT.ENERGY) || datatype.equals(CDT.FORCE)
//				|| datatype.equals(CDT.FREQUENCY) || datatype.equals(CDT.ILLUMINANCE) || datatype.equals(CDT.LUMINOUSFLUX)
//				|| datatype.equals(CDT.LENGTH) || datatype.equals(CDT.LUMINOUSINTENSITY)
//				|| datatype.equals(CDT.MAGNETICFLUX) || datatype.equals(CDT.MAGNETICFLUXDENSITY)
//				|| datatype.equals(CDT.MASS) || datatype.equals(CDT.POWER)
//				|| datatype.equals(CDT.PRESSURE) || datatype.equals(CDT.RADIATIONDOSEABSORBED)
//				|| datatype.equals(CDT.RADIATIONDOSEEFFECTIVE) || datatype.equals(CDT.RADIATIONDOSEEFFECTIVE)
//				|| datatype.equals(CDT.RADIOACTIVITY) || datatype.equals(CDT.SOLIDANGLE)
//				|| datatype.equals(CDT.SPEED) || datatype.equals(CDT.TEMPERATURE)
//				|| datatype.equals(CDT.TIME) || datatype.equals(CDT.VOLUME)
				;
	}

	/**
	 * Checks whether the supplied datatype is a built-in cdt:ucum datatype.
	 *
	 * @param datatype
	 * @return true if it is a main or specific cdt:ucum type
	 */
	public static boolean isBuiltInDatatype(IRI datatype) {
		return isMainDatatype(datatype) || isSpecificDatatype(datatype);
	}

	/**
	 * Checks whether the supplied datatype is a complex datatype, including its polar or cartesian derived notations.
	 *
	 * @param datatype
	 * @return true if it is a complex datatype
	 */
	public static boolean isComplexDatatype(IRI datatype) {
		return datatype.equals(CDT.COMPLEX) || isCartesianDatatype(datatype) || isPolarDatatype(datatype);
	}

	/**
	 * Checks whether the supplied datatype is equal to the cartesian notation.
	 *
	 * @param datatype
	 * @return true if it is a complexCartesian datatype
	 */
	public static boolean isCartesianDatatype(IRI datatype) {
		return datatype.equals(CDT.COMPLEXCARTESIAN);
	}

	/**
	 * Checks whether the supplied datatype is equal to the polar notation.
	 *
	 * @param datatype
	 * @return true if it is a complexPolar type
	 */
	public static boolean isPolarDatatype(IRI datatype) {
		return datatype.equals(CDT.COMPLEXPOLAR);
	}

	/*----------------*
	 * Value checking *
	 *----------------*/

	/**
	 * Verifies if the supplied lexical value is valid for the given datatype.
	 *
	 * @param value    a lexical value
	 * @param datatype a complex datatatype.
	 * @return true if the supplied lexical value is valid, false otherwise.
	 */
	public static boolean isValidValue(String value, IRI datatype) {
		boolean result = true;

		if (datatype.equals(CDT.COMPLEX)) {
			result = isValidComplex(value);
		} else if (datatype.equals(CDT.COMPLEXCARTESIAN)) {
			result = isValidCartesian(value);
		} else if (datatype.equals(CDT.COMPLEXPOLAR)) {
			result = isValidPolar(value);
		}

		return result;
	}
	
	/**
	 * Verifies if the supplied lexical value is a valid decimal or not.
	 *
	 * @param value
	 * @return <tt>true</tt> if valid, <tt>false</tt> otherwise
	 */
	public static boolean isValidComplex(String value) {
		return isValidCartesian(value) || isValidPolar(value);
	}

	/**
	 * Verifies if the supplied lexical value is a valid cartesian notation or not.
	 *
	 * @param value
	 * @return <tt>true</tt> if valid, <tt>false</tt> otherwise
	 */
	public static boolean isValidCartesian(String value) {
		try {
			parseCartesian(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
		
	/**
	 * Verifies if the supplied lexical value is a valid polar notation or not.
	 *
	 * @param value
	 * @return <tt>true</tt> if valid, <tt>false</tt> otherwise
	 */
	public static boolean isValidPolar(String value) {
		try {
			parsePolar(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	
	/*---------------------*
	 * Value normalization *
	 *---------------------*/

	/**
	 * Normalizes the supplied value according to the normalization rules for the supplied datatype.
	 *
	 * @param value    The value to normalize.
	 * @param datatype The value's datatype.
	 * @return The normalized value if there are any (supported) normalization rules for the supplied datatype, or the
	 *         original supplied value otherwise.
	 * @throws IllegalArgumentException If the supplied value is illegal considering the supplied datatype.
	 */
	public static String normalize(String value, IRI datatype) {
		String result = value;

		if (datatype.equals(CDT.COMPLEX)) {
			result = normalizeComplex(value);
		} else if (datatype.equals(CDT.COMPLEXCARTESIAN)) {
			result = normalizeCartesian(value);
		} else if (datatype.equals(CDT.COMPLEXPOLAR)) {
			result = normalizePolar(value);
		} 
		
		return result;
	}

	/**
	 * Normalizes a complex to its polar or cartesian representations.
	 *
	 * @param polar The complex to normalize.
	 * @return The normalized notation of a complex.
	 * @throws IllegalArgumentException If one of the supplied strings is not a legal complex.
	 */
	public static String normalizeComplex(String complex) {
			
		if(isValidCartesian(complex)) {
			return normalizeCartesian(complex);
		} else if(isValidPolar(complex)) {
			return normalizePolar(complex);
		} else {
			throw new IllegalArgumentException("Not a legal complex value:" + complex);
		}
	}
	

	/**
	 * Normalizes a complex to its cartesian representation according to the 
	 * org.apache.commons.math4.complex.Complex library.
	 *
	 * @param polar The cartesian to normalize.
	 * @return String (real, imaginary)
	 * @throws IllegalArgumentException If one of the supplied strings is not a legal cartesian.
	 */
	private static String normalizeCartesian(String value) {
		Double real = 0.0;
		Double imaginary = 0.0;
		String result = "";
		
		value = value.replaceAll("\\p{Space}+", "");
		
		Pattern pattern = Pattern.compile(cartesian);
		Matcher matcher = pattern.matcher(value);
		
		// Separates real and imaginary parts of the string into doubles
		if (Pattern.matches(cartesian, value)) {
			while (matcher.find()) {
				// Group 1:  [+-] {real} [+-] {imaginary}
				if(matcher.group(1) != null) {
					real = Double.parseDouble(matcher.group(2));
					imaginary = Double.parseDouble(matcher.group(3).replaceAll("[ij]", ""));
				}
				// Group 4:  [+-] {real} [+-] {imaginary}
				if(matcher.group(4) != null) {
					real = Double.parseDouble(matcher.group(5));
					imaginary = Double.parseDouble(matcher.group(6).replaceAll("[ij]", ""));
				}
				// Group 7:  [+-] {imaginary} [+-] {real} 
				if(matcher.group(7) != null) {
					imaginary = Double.parseDouble(matcher.group(8).replaceAll("[ij]", ""));
					real = Double.parseDouble(matcher.group(9));
				}
				// Group 10:  [+-] {imaginary} [+-] {real} 
				if(matcher.group(10) != null) {
					imaginary = Double.parseDouble(matcher.group(11).replaceAll("[ij]", ""));
					real = Double.parseDouble(matcher.group(12));
				}
				// Group 13: =  [+-] {imaginary}
				if(matcher.group(13) != null) 
					imaginary = Double.parseDouble(matcher.group(13).replaceAll("[ij]", ""));
				
				// Group 14:  [+-] {imaginary}
				if(matcher.group(14) != null) 
					imaginary = Double.parseDouble(matcher.group(14).replaceAll("[ij]", ""));
				
				// Group 15:  [+-] {real} 
				if(matcher.group(15) != null) 
					real = Double.parseDouble(matcher.group(15));
			  }
		  }	else 
			  throwIAE(value + " is NOT a valid cartesian notation !");
		
		// Turns doubles into normalizes string
		// Apache Commons Math Complex only accepts doubles with commas and the major "e" decimal notation
		if(imaginary.toString().startsWith("-")) {
			result = (real.toString() + " " + imaginary.toString().replaceFirst("-", "- ") + "i");
		} else {
			result = (real.toString()+ " + " +  imaginary.toString().replace("+", "") + "i");
		}
				
		// Output example : "1.0E5 - 7.555i"
		return result;
		  
	}
	
	/**
	 * Normalizes a complex to its polar representation, concerning its pattern proposed in P_POLAR.
	 *
	 * @param polar The polar to normalize.
	 * @return The polar notation of a complex.
	 * @throws IllegalArgumentException If one of the supplied strings is not a legal polar.
	 */
	public static String normalizePolar(String value) {
		double r = 0.0;
		double theta = 0.0; // in radians
		String result = "";
		
		value = value.replaceAll("\\p{Space}+", "");
		
		Pattern pattern = Pattern.compile(polar);
		Matcher matcher = pattern.matcher(value);
		
		// Separates module and angle parts of the string into doubles
		if (Pattern.matches(polar, value)) {
			while (matcher.find()) {
				r = Double.parseDouble(matcher.group(1));
			
			if(matcher.group(3).contains("[rad]") && matcher.group(3).contains("[pi]")) {
				theta = Double.parseDouble(matcher.group(2));
				theta = theta * FastMath.PI;
			} else if (matcher.group(3).contains("[rad]") && !matcher.group(3).contains("[pi]")) {
				theta = Double.parseDouble(matcher.group(2));
			} else if(matcher.group(3).contains("[deg]") && !matcher.group(3).contains("[pi]")) {
				// Transforms degrees in radians
				theta = Double.parseDouble(matcher.group(2));
				theta = Math.toRadians(theta);
			} else {
				throwIAE(value + " is NOT a valid cartesian notation !");
				}
			}
		} else 
			throwIAE(value + " is NOT a valid cartesian notation !");
		
		Complex complex = ComplexUtils.polar2Complex(r, theta);
		
		String real = Double.toString(complex.getReal());
		String imaginary = Double.toString(complex.getImaginary());
		
		// Turns doubles into normalizes string
		// Apache Commons Math Complex only accepts doubles with commas and the major "e" decimal notation
		if(imaginary.toString().startsWith("-")) {
			result = (real.toString() + " " + imaginary.toString().replaceFirst("-", "- ") + "i");
		} else {
			result = (real.toString()+ " + " +  imaginary.toString().replace("+", "") + "i");
		}
		
		return result;
	}
	
	/*---------------*
	 * Value parsing 
	 *---------------*/
	
	/**
	 * Parses the supplied complex string and returns its value.
	 *
	 * @param s A string representation of a complex value.
	 * @return The <tt>complex</tt> value represented by the supplied string argument.
	 * @throws NumberFormatException If the supplied string is not a valid complex value.
	 */
	public static Complex parseComplex(String s) {
		Complex complex = new Complex(0.0);
		
		if(isValidCartesian(s)) {
			complex = parseCartesian(s);
		} else if(isValidPolar(s)) {
			complex = parsePolar(s);		
		} else {
			throwIAE(s + " is NOT a valid complex notation !");
		}
		
		return complex;
	}
	
	/**
	 * Parses the supplied complex cartesian string and returns its value.
	 *
	 * @param s A string representation of an xsd:boolean value.
	 * @return The <tt>boolean</tt> value represented by the supplied string argument.
	 * @throws NumberFormatException If the supplied string is not a valid complex cartesian notation.
	 */
	public static Complex parseCartesian(String value) {
		ComplexFormat format = ComplexFormat.getInstance(new Locale("en"));
		Complex complex = format.parse(normalizeCartesian(value));
		
		return complex;
	}
	
	/**
	 * Parses the supplied complex polar string and returns its value.
	 *
	 * @param s A string representation of an polar complex value.
	 * @return The <tt>complex</tt> value represented by the supplied string argument.
	 * @throws NumberFormatException If the supplied string is not a valid complex polar notation.
	 */
	public static Complex parsePolar(String value) {
		ComplexFormat format = ComplexFormat.getInstance(new Locale("en"));
		Complex complex = format.parse(normalizePolar(value));
		
		return complex;
	}


	/*------------------*
	 * Value comparison *
	 *------------------*/
	
	public static boolean compare(String value1, String value2, IRI datatype) {
		if (datatype.equals(CDT.COMPLEX) || datatype.equals(CDT.COMPLEXCARTESIAN) || datatype.equals(CDT.COMPLEXPOLAR))
			return compareComplex(value1, value2);
		else
			throw new IllegalArgumentException("datatype is not ordered");
	}

	/**
	 * Compares two complexes to each other.
	 *
	 * @param complex1
	 * @param complex2
	 * @return Returns true if <tt>complex1</tt> is equal to <tt>complex2</tt> or return false
	 *         if <tt>complex1</tt> differs from <tt>complex2</tt>.
	 * @throws IllegalArgumentException If one of the supplied strings is not a legal complex.
	 */
	public static boolean compareComplex(String complex1, String complex2) {
		Complex comp1 = parseComplex(complex1);
		Complex comp2 = parseComplex(complex2);

		return Complex.equals(comp1, comp2);
	}
	

	/*-----------------*
	 * Utility methods *
	 *-----------------*/
	
	/**
	 * Throws an IllegalArgumentException that contains the supplied message.
	 */
	private static final void throwIAE(String msg) {
		throw new IllegalArgumentException(msg);
	}
}
