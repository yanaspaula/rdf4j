/*******************************************************************************
 * Copyright (c) 2020 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.elasticsearchstore;

class ElasticsearchStatementPOJO {

	ElasticsearchStatementPOJO() {
	}

	public String subject;
	public String predicate;
	public String object;
	public int object_Hash;

	public String context;
	public boolean context_IRI;
	public boolean context_BNode;
	public boolean subject_IRI;
	public boolean subject_BNode;
	public boolean object_IRI;
	public boolean object_BNode;
	public String object_Datatype;
	public String object_Lang;

	public String getSubject() {
		return subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getObject() {
		return object;
	}

	public int getObject_Hash() {
		return object_Hash;
	}

	public String getContext() {
		return context;
	}

	public boolean isContext_IRI() {
		return context_IRI;
	}

	public boolean isContext_BNode() {
		return context_BNode;
	}

	public boolean isSubject_IRI() {
		return subject_IRI;
	}

	public boolean isSubject_BNode() {
		return subject_BNode;
	}

	public boolean isObject_IRI() {
		return object_IRI;
	}

	public boolean isObject_BNode() {
		return object_BNode;
	}

	public String getObject_Datatype() {
		return object_Datatype;
	}

	public String getObject_Lang() {
		return object_Lang;
	}
}
