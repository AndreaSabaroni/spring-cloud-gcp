/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gcp.data.firestore;

import java.util.Map;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Internal;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.Value;

import org.springframework.cloud.gcp.core.util.MapBuilder;

/**
 *
 * TBD.
 *
 * @author Dmitry Solomakha
 *
 */
public final class FirestoreClassMapper {

	private final Internal delegate;

	private static final String VALUE_FIELD_NAME = "value";

	private static final String NOT_USED_PATH = "/not/used/path";

	public FirestoreClassMapper(FirestoreOptions firestoreOptions, FirestoreRpc firestoreRpc) {
		this.delegate = new Internal(firestoreOptions, firestoreRpc);
	}

	public <T> Value convertToFirestoreValue(T entity) {
		DocumentSnapshot documentSnapshot = this.delegate.snapshotFromMap(NOT_USED_PATH,
				new MapBuilder<String, Object>().put(VALUE_FIELD_NAME, entity).build());
		return this.delegate.protoFromSnapshot(documentSnapshot).get(VALUE_FIELD_NAME);
	}

	public <T> Map<String, Value> convertToFirestoreTypes(T entity) {
		DocumentSnapshot documentSnapshot = this.delegate.snapshotFromObject(NOT_USED_PATH, entity);
		return this.delegate.protoFromSnapshot(documentSnapshot);
	}

	public <T> T convertToCustomClass(Document document, Class<T> clazz) {
		DocumentSnapshot documentSnapshot = this.delegate.snapshotFromProto(Timestamp.now(), document);
		return documentSnapshot.toObject(clazz);
	}
}
