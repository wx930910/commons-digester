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
package org.apache.commons.digester3.plugins;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import org.apache.commons.digester3.Rule;
import org.xml.sax.Attributes;

/**
 * Demonstrates the behavior of the Delegate interface.
 */
public class DumperRule {
	public static Rule mockRule1() throws Exception {
		Rule mockInstance = spy(Rule.class);
		doAnswer((stubInvo) -> {
			String text = stubInvo.getArgument(2);
			System.out.print(text);
			return null;
		}).when(mockInstance).body(any(), any(), any());
		doAnswer((stubInvo) -> {
			String name = stubInvo.getArgument(1);
			Attributes attributes = stubInvo.getArgument(2);
			System.out.print("<");
			System.out.print(name);
			final int nAttributes = attributes.getLength();
			for (int i = 0; i < nAttributes; ++i) {
				final String key = attributes.getQName(i);
				final String value = attributes.getValue(i);
				System.out.print(" ");
				System.out.print(key);
				System.out.print("=");
				System.out.print("'");
				System.out.print(value);
				System.out.print("'");
			}
			System.out.println(">");
			return null;
		}).when(mockInstance).begin(any(), any(), any());
		doAnswer((stubInvo) -> {
			String name = stubInvo.getArgument(1);
			System.out.print("</");
			System.out.print(name);
			System.out.println(">");
			return null;
		}).when(mockInstance).end(any(), any());
		return mockInstance;
	}
}
