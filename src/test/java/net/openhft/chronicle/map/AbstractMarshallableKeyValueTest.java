/*
 * Copyright 2012-2018 Chronicle Map Contributors
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

package net.openhft.chronicle.map;

import net.openhft.chronicle.hash.serialization.impl.TypedMarshallableReaderWriter;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class AbstractMarshallableKeyValueTest {

    @Test
    public void shouldAcceptAbstractMarshallableComponents() throws Exception {
        final ChronicleMap<Key, Value> map = ChronicleMapBuilder.of(Key.class, Value.class).entries(10).
                averageKey(new Key()).averageValue(new Value()).create();

        map.put(new Key(), new Value());

        assertThat(map.get(new Key()).number, is(new Value().number));
    }

    @Test
    public void shouldAcceptAbstractMarshallableComponents2() throws Exception {
        final ChronicleMap<Key, Marshallable> map = ChronicleMapBuilder.of(Key.class, Marshallable.class).entries(10)
                .averageKey(new Key()).averageValue(new Value())
                .valueMarshaller(new TypedMarshallableReaderWriter<>(Marshallable.class))
                .create();

        map.put(new Key(), new Value());

        Value value = (Value) map.get(new Key());
        assertThat(value.number, is(new Value().number));
    }

    private static final class Key extends SelfDescribingMarshallable {
        private String k = "key";
    }

    private static final class Value extends SelfDescribingMarshallable {
        private Integer number = 17;
    }
}
