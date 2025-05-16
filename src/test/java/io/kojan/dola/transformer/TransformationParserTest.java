/*-
 * Copyright (c) 2025 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kojan.dola.transformer;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

public class TransformationParserTest {

    @Test
    public void testParseSingleInstructionWithoutSelector() {
        Properties props = new Properties();
        props.setProperty("dola.transformer.insn.removeParent", "com.example:parent");

        Transformer mockTransformer = mock(Transformer.class);
        Transformation mockTransformation = mock(Transformation.class);

        expect(mockTransformer.produceTransformation("com.example:parent"))
                .andReturn(mockTransformation);

        Map<String, Transformer> transformers = Map.of("removeParent", mockTransformer);

        replay(mockTransformer, mockTransformation);

        List<Transformation> result = TransformationParser.parseFromProperties(transformers, props);
        assertEquals(1, result.size());
        assertInstanceOf(SelectiveTransformation.class, result.get(0));

        verify(mockTransformer, mockTransformation);
    }

    @Test
    public void testParseInstructionWithSelector() {
        Properties props = new Properties();
        props.setProperty("dola.transformer.insn.removeParent", "com.example:parent@org.test:lib");

        Transformer mockTransformer = mock(Transformer.class);
        Transformation mockTransformation = mock(Transformation.class);

        expect(mockTransformer.produceTransformation("com.example:parent"))
                .andReturn(mockTransformation);

        Map<String, Transformer> transformers = Map.of("removeParent", mockTransformer);

        replay(mockTransformer, mockTransformation);

        List<Transformation> result = TransformationParser.parseFromProperties(transformers, props);
        assertEquals(1, result.size());

        assertNotNull(result);
        assertTrue(result.toString().contains("op=removeParent"));
        assertTrue(result.toString().contains("arg=com.example:parent"));
        assertTrue(result.toString().contains("sel=org.test:lib"));

        verify(mockTransformer, mockTransformation);
    }

    @Test
    public void testParseInstructionWithMultipleSelectors() {
        Properties props = new Properties();
        props.setProperty("dola.transformer.insn.removeParent", "val@(a:b,c:d,e:f)");

        Transformer mockTransformer = mock(Transformer.class);
        Transformation mockTransformation = mock(Transformation.class);

        expect(mockTransformer.produceTransformation("val")).andReturn(mockTransformation);

        Map<String, Transformer> transformers = Map.of("removeParent", mockTransformer);

        replay(mockTransformer, mockTransformation);

        List<Transformation> result = TransformationParser.parseFromProperties(transformers, props);
        assertEquals(1, result.size());

        assertNotNull(result);
        assertTrue(result.toString().contains("arg=val"));
        assertTrue(result.toString().contains("sel=a:b,c:d,e:f"));

        verify(mockTransformer, mockTransformation);
    }

    @Test
    public void testSkipsUnknownTransformer() {
        Properties props = new Properties();
        props.setProperty("dola.transformer.insn.unknownOp", "value");

        Map<String, Transformer> transformers = Map.of(); // empty map

        List<Transformation> result = TransformationParser.parseFromProperties(transformers, props);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIgnoresNonTransformerKeys() {
        Properties props = new Properties();
        props.setProperty("some.other.property", "ignored");
        props.setProperty("dola.transformer.insn.removeDependency", "g:a");

        Transformer mockTransformer = mock(Transformer.class);
        Transformation mockTransformation = mock(Transformation.class);

        expect(mockTransformer.produceTransformation("g:a")).andReturn(mockTransformation);

        Map<String, Transformer> transformers = Map.of("removeDependency", mockTransformer);

        replay(mockTransformer, mockTransformation);

        List<Transformation> result = TransformationParser.parseFromProperties(transformers, props);
        assertEquals(1, result.size());

        verify(mockTransformer, mockTransformation);
    }
}
