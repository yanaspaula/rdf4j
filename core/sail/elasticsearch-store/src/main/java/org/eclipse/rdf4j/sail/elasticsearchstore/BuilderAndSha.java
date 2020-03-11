/*******************************************************************************
 * Copyright (c) 2019 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.elasticsearchstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

class BuilderAndSha {

	final static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper(new CBORFactory());
	}

	private final String sha256;
	private final ElasticsearchStatementPOJO data;

	BuilderAndSha(String sha256, ElasticsearchStatementPOJO data) {
		this.sha256 = sha256;
		this.data = data;

	}

	String getSha256() {
		return sha256;
	}

	byte[] getData() {
		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
