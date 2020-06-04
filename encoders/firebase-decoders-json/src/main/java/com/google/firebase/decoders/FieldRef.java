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

package com.google.firebase.decoders;

import androidx.annotation.NonNull;

//TODO: support required, optional.
public abstract class FieldRef<T> {
  @NonNull
  protected String name;

  @NonNull
  public String getName() {
    return name;
  }

  protected FieldRef(@NonNull String name) {
    this.name = name;
  }

  public static class Primitive<T> extends FieldRef<T> {

    private Class<T> rawType;

    @NonNull
    public static <T> Primitive<T> of (@NonNull Class<T> clazz, @NonNull String name) {
      final Primitive<T> ref = new Primitive<>(name, clazz);
      return ref;
    }

    public Primitive(@NonNull String name, @NonNull Class<T> rawType) {
      super(name);
      this.rawType = rawType;
    }

    @NonNull
    public Class<T> getRawType() {
      return rawType;
    }
  }

  public static class Boxed<T> extends FieldRef<T> {
    TypeToken<T> typeToken;
    @NonNull
    public static <T> Boxed<T> of (@NonNull TypeToken<T> typeToken, @NonNull String name) {
      final Boxed<T> ref = new Boxed(name, typeToken);
      return ref;
    }

    @NonNull
    public TypeToken<T> getTypeToken() {
      return typeToken;
    }

    public Boxed(@NonNull String name, @NonNull TypeToken<T> typeToken) {
      super(name);
      this.typeToken = typeToken;
    }
  }
}
