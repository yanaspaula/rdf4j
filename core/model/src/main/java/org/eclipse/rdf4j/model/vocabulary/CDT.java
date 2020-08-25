/*******************************************************************************
 * Copyright (c) 2020 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.model.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Constants for the datatypes propositions in <a href="https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#ucum">Custom Datatypes for Quantity Values</a>.
 *
 * Only the complex datatypes were implemented in this version.
 * 
 * 
 * @see <a href="https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#ucum">Custom Datatypes for Quantity Values</a>
 * @author Yana Soares de Paula
 */
public class CDT {

	/** The cdt:ucum namespace (<tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#</tt>). */
	public static final String NAMESPACE = "https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#";

	/**
	 * Recommended prefix for cdt:ucum datatypes: "cdt"
	 */
	public static final String PREFIX = "cdt";

	/**
	 * An immutable {@link Namespace} constant that represents the cdt:ucum namespace.
	 */
	public static final Namespace NS = new SimpleNamespace(PREFIX, NAMESPACE);

	/*
	 * Main datatypes
	 */
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#complex</tt> */
	public final static IRI COMPLEX = create("complex");
	
	// The following Main cdt:ucum datatypes are to be implemented :
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#ucum</tt> */
	public final static IRI UCUM = create("ucum");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#ucumunit</tt> */
	public final static IRI UCUMUNIT = create("ucumunit");

	/*
	 * Specific datatypes
	 */
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#complex</tt> */
	public final static IRI COMPLEXCARTESIAN = create("complexCartesian");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#complex</tt> */
	public final static IRI COMPLEXPOLAR = create("complexPolar");

	// The following Specific cdt:ucum datatypes are to be implemented :	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#acceleration</tt> */
	public final static IRI ACCELERATION = create("acceleration");

	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#amountOfSubstance</tt> */
	public final static IRI AMOUNTOFSUBSTANCE = create("amountOfSubstance");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#angle</tt> */
	public final static IRI ANGLE = create("angle");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#area</tt> */
	public final static IRI AREA = create("area");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#catalyticActivity</tt> */
	public final static IRI CATALYTICACTIVITY = create("catalyticActivity");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#dimensionless</tt> */
	public final static IRI DIMENSIONLESS = create("dimensionless");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricCapacitance</tt> */
	public final static IRI ELECTRICCAPACITANCE = create("electricCapacitance");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricCharge</tt> */
	public final static IRI ELECTRICCHARGE = create("electricCharge");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricConductance</tt> */
	public final static IRI ELECTRICCONDUCTANCE = create("electricConductance");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricCurrent</tt> */
	public final static IRI ELECTRICCURRENT = create("electricCurrent");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricInductance</tt> */
	public final static IRI ELECTRICINDUCTANCE = create("electricInductance");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricPotential</tt> */
	public final static IRI ELECTRICPOTENTIAL = create("electricPotential");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#electricResistance</tt> */
	public final static IRI ELECTRICRESISTANCE = create("electricResistance");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#energy</tt> */
	public final static IRI ENERGY = create("energy");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#force</tt> */
	public final static IRI FORCE = create("force");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#frequency</tt> */
	public final static IRI FREQUENCY = create("frequency");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#illuminance</tt> */
	public final static IRI ILLUMINANCE = create("illuminance");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#length</tt> */
	public final static IRI LENGTH = create("length");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#luminousFlux</tt> */
	public final static IRI LUMINOUSFLUX = create("luminousFlux");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#luminousIntensity</tt> */
	public final static IRI LUMINOUSINTENSITY = create("luminousIntensity");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#magneticFlux</tt> */
	public final static IRI MAGNETICFLUX = create("magneticFlux");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#magneticFluxDensity</tt> */
	public final static IRI MAGNETICFLUXDENSITY = create("magneticFluxDensity");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#mass</tt> */
	public final static IRI MASS = create("mass");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#power</tt> */
	public final static IRI POWER = create("power");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#pressure</tt> */
	public final static IRI PRESSURE = create("pressure");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#radiationDoseAbsorbed</tt> */
	public final static IRI RADIATIONDOSEABSORBED = create("radiationDoseAbsorbed");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#radiationDoseEffective</tt> */
	public final static IRI RADIATIONDOSEEFFECTIVE = create("radiationDoseEffective");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#radioactivity</tt> */
	public final static IRI RADIOACTIVITY = create("radioactivity");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#solidAngle</tt> */
	public final static IRI SOLIDANGLE = create("solidAngle");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#speed</tt> */
	public final static IRI SPEED = create("speed");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#temperature</tt> */
	public final static IRI TEMPERATURE = create("temperature");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#time</tt> */
	public final static IRI TIME = create("time");
	
	/** <tt>https://ci.mines-stetienne.fr/lindt/v2/custom_datatypes.html#volume</tt> */
	public final static IRI VOLUME = create("volume");

	private static IRI create(String localName) {
		return SimpleValueFactory.getInstance().createIRI(CDT.NAMESPACE, localName);
	}
}
