package org.apache.commons.digester3.substitution;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import org.apache.commons.digester3.Substitutor;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public final class CompoundSubstitutorTestCase {

	public static Substitutor mockSubstitutor1(final String bodyText, final String uri, final String localName,
			final String type, final String value) {
		String mockFieldVariableType;
		String mockFieldVariableLocalName;
		String mockFieldVariableValue;
		String mockFieldVariableUri;
		String mockFieldVariableNewBodyText;
		Substitutor mockInstance = spy(Substitutor.class);
		mockFieldVariableNewBodyText = bodyText;
		mockFieldVariableUri = uri;
		mockFieldVariableLocalName = localName;
		mockFieldVariableType = type;
		mockFieldVariableValue = value;
		doAnswer((stubInvo) -> {
			Attributes attributes = stubInvo.getArgument(0);
			final AttributesImpl attribs = new AttributesImpl(attributes);
			attribs.addAttribute(mockFieldVariableUri, mockFieldVariableLocalName,
					mockFieldVariableUri + ":" + mockFieldVariableLocalName, mockFieldVariableType,
					mockFieldVariableValue);
			return attribs;
		}).when(mockInstance).substitute(any(Attributes.class));
		doAnswer((stubInvo) -> {
			return mockFieldVariableNewBodyText;
		}).when(mockInstance).substitute(any(String.class));
		return mockInstance;
	}

	private Attributes attrib;

	private String bodyText;

	@Before
	public void setUp() {
		final AttributesImpl aImpl = new AttributesImpl();
		aImpl.addAttribute("", "b", ":b", "", "bcd");
		aImpl.addAttribute("", "c", ":c", "", "cde");
		aImpl.addAttribute("", "d", ":d", "", "def");

		attrib = aImpl;
		bodyText = "Amazing Body Text!";
	}

	@Test
	public void testConstructors() {
		try {
			new CompoundSubstitutor(null, null);
			fail();
		} catch (final IllegalArgumentException e) {
			// OK
		}

		final Substitutor a = CompoundSubstitutorTestCase.mockSubstitutor1("XYZ", "", "a", "", "abc");

		try {
			new CompoundSubstitutor(a, null);
			fail();
		} catch (final IllegalArgumentException e) {
			// OK
		}

		try {
			new CompoundSubstitutor(null, a);
			fail();
		} catch (final IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testChaining() {
		final Substitutor a = CompoundSubstitutorTestCase.mockSubstitutor1("XYZ", "", "a", "", "abc");
		final Substitutor b = CompoundSubstitutorTestCase.mockSubstitutor1("STU", "", "b", "", "bcd");

		final Substitutor test = new CompoundSubstitutor(a, b);

		final AttributesImpl attribFixture = new AttributesImpl(attrib);
		attribFixture.addAttribute("", "a", ":a", "", "abc");
		attribFixture.addAttribute("", "b", ":b", "", "bcd");

		assertTrue(areEqual(test.substitute(attrib), attribFixture));
		assertEquals(test.substitute(bodyText), "STU");
	}

	private boolean areEqual(final Attributes a, final Attributes b) {
		if (a.getLength() != b.getLength()) {
			return false;
		}

		boolean success = true;
		for (int i = 0; i < a.getLength() && success; i++) {
			success = a.getLocalName(i).equals(b.getLocalName(i)) && a.getQName(i).equals(b.getQName(i))
					&& a.getType(i).equals(b.getType(i)) && a.getURI(i).equals(b.getURI(i))
					&& a.getValue(i).equals(b.getValue(i));
		}

		return success;
	}

}
