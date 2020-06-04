// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.decoders.json;

import androidx.annotation.NonNull;

import com.google.firebase.decoders.FieldRef;
import com.google.firebase.decoders.ObjectDecoder;
import com.google.firebase.decoders.ObjectDecoderContext;
import com.google.firebase.decoders.Safe;
import com.google.firebase.decoders.TypeCreator;
import com.google.firebase.decoders.TypeToken;
import com.google.firebase.decoders.json.JsonDataDecoderBuilderContext;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.annotations.Encodable;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.RobolectricTestRunner;
/**
 * TODO: TypeToken support TypeVariable, inorder to support decode nested Generic type
 * class Foo<T> {
 *   SubFoo<T>
 * }
 */
@RunWith(RobolectricTestRunner.class)
public class JsonDataDecoderBuilderContextTest {

  static class Foo<T> {
    int a;
    boolean b;
    T t;
    SubFoo<String> subFoo;

    public Foo(int a, boolean b, T t, SubFoo<String> subFoo) {
      this.a = a;
      this.b = b;
      this.t = t;
      this.subFoo = subFoo;
    }
  }

  static class SubFoo<T> {
    int a;
    boolean b;
    T t;

    public SubFoo(int a, boolean b, T t) {
      this.a = a;
      this.b = b;
      this.t = t;
    }
  }

  static class FooObjectDecoder<T> implements ObjectDecoder<Foo<T>> {
    @NonNull
    @Override
    public TypeCreator<Foo<T>> decode(@NonNull ObjectDecoderContext<Foo<T>> ctx) {
      FieldRef.Primitive<Integer> aField = ctx.decodeInteger(FieldDescriptor.of("a"));
      FieldRef.Primitive<Boolean> bField = ctx.decodeBoolean(FieldDescriptor.of("b"));
      FieldRef.Boxed<T> tField = ctx.decode(FieldDescriptor.of("t"), (TypeToken.ClassToken) ctx.getTypeArguments().at(0));
      FieldRef.Boxed<SubFoo<String>> subFooField = ctx.decode(FieldDescriptor.of("subFoo"), TypeToken.of(new Safe<SubFoo<String>>(){}));

      return (creationCtx ->
              new Foo<T>(
                      creationCtx.getInteger(aField),
                      creationCtx.getBoolean(bField),
                      (T) creationCtx.get(tField),
                      (SubFoo<String>) creationCtx.get(subFooField)
                      )
      );
    }
  }

  static class SubFooObjectDecoder<T> implements ObjectDecoder<SubFoo<T>> {

    @NonNull
    @Override
    public TypeCreator<SubFoo<T>> decode(@NonNull ObjectDecoderContext<SubFoo<T>> ctx) {
      FieldRef.Primitive<Integer> aField = ctx.decodeInteger(FieldDescriptor.of("a"));
      FieldRef.Primitive<Boolean> bField = ctx.decodeBoolean(FieldDescriptor.of("b"));
      FieldRef.Boxed<T> tField = ctx.decode(FieldDescriptor.of("t"), (TypeToken.ClassToken) ctx.getTypeArguments().at(0));

      return (creationCtx ->
              new SubFoo<T>(
                      creationCtx.getInteger(aField),
                      creationCtx.getBoolean(bField),
                      (T)creationCtx.get(tField))
      );
    }
  }

  // TODO: Test LinkedList like type Support -- FiledRef optional/required.
  // {\"a\":1, \"b\":true, \"t\":\"str\", \"foo\": {\"a\":1, \"b\":true, \"t\":\"str\", \"foo\": null}}

  @Test
  public void test123() throws IOException {
    Map<TypeToken.ClassToken<?>, ObjectDecoder<?>> objectDecoders = new HashMap<>();
    objectDecoders.put((TypeToken.ClassToken<?>) TypeToken.of(new Safe<Foo<String>>(){}), new FooObjectDecoder<>());
    objectDecoders.put((TypeToken.ClassToken<?>) TypeToken.of(new Safe<SubFoo<String>>(){}), new SubFooObjectDecoder<String>());
    JsonDataDecoderBuilderContext jsonDataDecoderBuilderContext
            = new JsonDataDecoderBuilderContext(objectDecoders);
    String json =
            "{\"a\":1, \"b\":true, \"t\":\"str\", \"subFoo\": {\"a\":1, \"b\":true, \"t\":\"str\"}}";
    InputStream input = new ByteArrayInputStream(json.getBytes());
    TypeToken.ClassToken<Foo<String>> typeToken = (TypeToken.ClassToken<Foo<String>>) TypeToken.of(new Safe<Foo<String>>(){});
    Foo<String> foo = jsonDataDecoderBuilderContext.decode(input, typeToken);

    assertThat(foo.a).isEqualTo(1);
    assertThat(foo.b).isEqualTo(true);
    assertThat(foo.t).isEqualTo("str");
    assertThat(foo.subFoo.a).isEqualTo(1);
    assertThat(foo.subFoo.b).isEqualTo(true);
    assertThat(foo.subFoo.t).isEqualTo("str");
  }
}
