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

import com.google.firebase.encoders.FieldDescriptor;

//TODO: decodeInline() and setUnknownBehavior(), implementation

public interface ObjectDecoderContext<T> {

  @NonNull
  TypeToken.ClassToken<T> getTypeToken();

  @NonNull
  TypeTokenContainer getTypeArguments();

  @NonNull
  <TField> FieldRef.Boxed<TField> decode(@NonNull FieldDescriptor fileDescriptor, @NonNull TypeToken<TField> typeToken);

  @NonNull
  FieldRef.Primitive<Boolean> decodeBoolean(@NonNull FieldDescriptor fileDescriptor);

  @NonNull
  FieldRef.Primitive<Integer> decodeInteger(@NonNull FieldDescriptor fileDescriptor);

  //TODO: `decode` other primitive types
}
