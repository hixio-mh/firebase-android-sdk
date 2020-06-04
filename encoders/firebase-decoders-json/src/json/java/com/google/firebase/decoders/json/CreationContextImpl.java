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

import com.google.firebase.decoders.CreationContext;
import com.google.firebase.decoders.FieldRef;

import java.util.HashMap;
import java.util.Map;

public class CreationContextImpl<T> implements CreationContext<T> {
  Map<FieldRef<?>, Object> ctx = new HashMap<>();

  @NonNull
  public CreationContextImpl<T> put(@NonNull FieldRef<?> ref, @NonNull Object val) {
    ctx.put(ref, val);
    return this;
  }

  @NonNull
  @Override
  public <TField> TField get(@NonNull FieldRef.Boxed<TField> ref) {
    return (TField) ctx.get(ref);  }

  @NonNull
  @Override
  public Boolean getBoolean(@NonNull FieldRef.Primitive<Boolean> ref) {
    return (Boolean) ctx.get(ref);
  }

  @NonNull
  @Override
  public Integer getInteger(@NonNull FieldRef.Primitive<Integer> ref) {
    return (Integer) ctx.get(ref);
  }

}
