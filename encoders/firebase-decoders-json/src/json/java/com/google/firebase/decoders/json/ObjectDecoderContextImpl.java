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
import com.google.firebase.decoders.ObjectDecoderContext;
import com.google.firebase.decoders.TypeToken;
import com.google.firebase.decoders.TypeTokenContainer;
import com.google.firebase.encoders.FieldDescriptor;

import java.util.HashMap;
import java.util.Map;

public class ObjectDecoderContextImpl<T> implements ObjectDecoderContext<T> {
  private Map<String, FieldRef<?>> refs = new HashMap<>();
  private TypeToken.ClassToken<T> classToken;

  @NonNull
  public static <T> ObjectDecoderContextImpl<T> of(@NonNull TypeToken.ClassToken<T> classToken) {
    return new ObjectDecoderContextImpl<T>(classToken);
  }

  private ObjectDecoderContextImpl(TypeToken.ClassToken<T> classToken) {
    this.classToken = classToken;
  }

  @NonNull
  public FieldRef<?> getFieldRef(@NonNull String fieldName){
    return refs.get(fieldName);
  }

  @NonNull
  @Override
  public TypeToken.ClassToken<T> getTypeToken() {
    return classToken;
  }

  @NonNull
  @Override
  public TypeTokenContainer getTypeArguments() {
    return classToken.getTypeArguments();
  }

  @NonNull
  @Override
  public <TField> FieldRef.Boxed<TField> decode(@NonNull FieldDescriptor fileDescriptor, @NonNull TypeToken<TField> typeToken) {
    FieldRef.Boxed<TField> ref = FieldRef.Boxed.of(typeToken, fileDescriptor.getName());
    refs.put(fileDescriptor.getName(), ref);
    return ref;
  }

  @NonNull
  @Override
  public FieldRef.Primitive<Boolean> decodeBoolean(@NonNull FieldDescriptor fileDescriptor) {
    FieldRef.Primitive<Boolean> ref = FieldRef.Primitive.of(Boolean.class, fileDescriptor.getName());
    refs.put(fileDescriptor.getName(), ref);
    return ref;
  }

  @NonNull
  @Override
  public FieldRef.Primitive<Integer> decodeInteger(@NonNull FieldDescriptor fileDescriptor) {
    FieldRef.Primitive<Integer> ref = FieldRef.Primitive.of(Integer.class, fileDescriptor.getName());
    refs.put(fileDescriptor.getName(), ref);
    return ref;
  }
}
