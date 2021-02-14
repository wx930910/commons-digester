/* $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.digester3;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.digester3.binder.RuleProvider;
import org.xml.sax.Attributes;

/**
 * <p>
 * This rule implementation is intended to help test digester. The idea is that
 * you can test which rule matches by looking at the identifier.
 * </p>
 * 
 * @author Robert Burrell Donkin
 */

public class TestRule extends Rule {

	// ----------------------------------------------------- Instance Variables

	public static RuleProvider<TestRule> mockRuleProvider1(final String identifier, final List<Rule> callOrder) {
		List<Rule> mockFieldVariableCallOrder;
		String mockFieldVariableIdentifier;
		RuleProvider<TestRule> mockInstance = mock(RuleProvider.class);
		mockFieldVariableIdentifier = identifier;
		mockFieldVariableCallOrder = callOrder;
		when(mockInstance.get()).thenAnswer((stubInvo) -> {
			final TestRule testRule = new TestRule(mockFieldVariableIdentifier);
			testRule.setOrder(mockFieldVariableCallOrder);
			return testRule;
		});
		return mockInstance;
	}

	/** String identifing this particular <code>TestRule</code> */
	private final String identifier;

	/** Used when testing body text */
	private String bodyText;

	/** Used when testing call orders */
	private List<Rule> order;

	// ----------------------------------------------------------- Constructors

	/**
	 * Base constructor.
	 * 
	 * @param identifier Used to tell which TestRule is which
	 */
	public TestRule(final String identifier) {

		this.identifier = identifier;
	}

	/**
	 * Constructor sets namespace URI.
	 * 
	 * @param identifier   Used to tell which TestRule is which
	 * @param namespaceURI Set rule namespace
	 */
	public TestRule(final String identifier, final String namespaceURI) {

		this.identifier = identifier;
		setNamespaceURI(namespaceURI);

	}

	// ------------------------------------------------ Rule Implementation

	/**
	 * 'Begin' call.
	 */
	@Override
	public void begin(final String namespace, final String name, final Attributes attributes) throws Exception {
		appendCall();
	}

	/**
	 * 'Body' call.
	 */
	@Override
	public void body(final String namespace, final String name, final String text) throws Exception {
		this.bodyText = text;
		appendCall();
	}

	/**
	 * 'End' call.
	 */
	@Override
	public void end(final String namespace, final String name) throws Exception {
		appendCall();
	}

	// ------------------------------------------------ Methods

	/**
	 * If a list has been set, append this to the list.
	 */
	protected void appendCall() {
		if (order != null) {
			order.add(this);
		}
	}

	/**
	 * Get the body text that was set.
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * Get the identifier associated with this test.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Get call order list.
	 */
	public List<Rule> getOrder() {
		return order;
	}

	/**
	 * Set call order list
	 */
	public void setOrder(final List<Rule> order) {
		this.order = order;
	}

	/**
	 * Return the identifier.
	 */
	@Override
	public String toString() {
		return identifier;
	}

}
