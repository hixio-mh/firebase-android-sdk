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

import android.util.JsonReader;

import androidx.annotation.NonNull;

import com.google.firebase.decoders.DataDecoder;
import com.google.firebase.decoders.FieldRef;
import com.google.firebase.decoders.ObjectDecoder;
import com.google.firebase.decoders.TypeCreator;
import com.google.firebase.decoders.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
//TODO: remove Exception from method header
//TODO: surpass warnings
//TODO: Java Docs
public class JsonDataDecoderBuilderContext implements DataDecoder {
  private Map<TypeToken.ClassToken<?>, ObjectDecoderContextImpl<?>> objectDecodersCtx = new HashMap<>();
  private Map<TypeToken.ClassToken<?>, TypeCreator<?>> typeCreators = new HashMap<>();
  private JsonReader reader;

  public JsonDataDecoderBuilderContext(@NonNull Map<TypeToken.ClassToken<?>, ObjectDecoder<?>> objectDecoders) {
    for (Map.Entry<TypeToken.ClassToken<?>, ObjectDecoder<?>> entry : objectDecoders.entrySet()) {
      TypeToken.ClassToken<?> classToken = entry.getKey();
      ObjectDecoder objectDecoder = entry.getValue();
      ObjectDecoderContextImpl objectDecoderCtx = ObjectDecoderContextImpl.of(classToken);
      objectDecoder.decode(objectDecoderCtx);
      TypeCreator<?> creator = objectDecoder.decode(objectDecoderCtx);
      objectDecodersCtx.put(classToken, objectDecoderCtx);
      typeCreators.put(classToken, creator);
    }
  }

  @NonNull
  @Override
  public <T> T decode(@NonNull InputStream input, @NonNull TypeToken<T> typeToken) throws IOException {
    this.reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
    return decode(typeToken);
  }

  private <T> T decode(TypeToken<T> typeToken) throws IOException {
    if (typeToken instanceof TypeToken.ClassToken) {
      TypeToken.ClassToken classToken = (TypeToken.ClassToken<T>) typeToken;
      T val = (T) decodeObject(classToken);
      return val;
    } else if (typeToken instanceof TypeToken.ArrayToken) {
      //TODO: Change typeParameter T in ArrayToken<T> to represent component type.
      /**
       * reader.beginArray();
       * List<T> l = new LinkedList<>();
       * while reader.hasNext:
       *  T val = decode(arrayToken.getComponentType());
       *  l.add(val);
       * reader.endArray();
       * return l.toArray();
       */
    }
    return null;
  }

  private <T> T decodeObject(TypeToken.ClassToken<T> classToken) throws IOException {
    if(classToken.getRawType().isPrimitive() || classToken.getRawType().equals(String.class))
      return decodePrimitives(classToken.getRawType());

    CreationContextImpl<T> creationCtx = new CreationContextImpl<>();
    ObjectDecoderContextImpl<T> decoderCtx = (ObjectDecoderContextImpl<T>) objectDecodersCtx.get(classToken);
    reader.beginObject();
    while (reader.hasNext()) {
      String fieldName = reader.nextName();
      FieldRef<?> fieldRef = decoderCtx.getFieldRef(fieldName);
      // TODO: fieldRef is null (ObjectDecoder didn't handle that field)
      if (fieldRef instanceof FieldRef.Primitive) {
        creationCtx.put(fieldRef,
                decodePrimitives(((FieldRef.Primitive<?>) fieldRef).getRawType()));
      } else if (fieldRef instanceof FieldRef.Boxed) {
        creationCtx.put(fieldRef,
                decode(((FieldRef.Boxed<?>) fieldRef).getTypeToken()));
      }
    }
    reader.endObject();

    TypeCreator<T> creator = (TypeCreator<T>) typeCreators.get(classToken);
    return (T) creator.create(creationCtx);
  }

  private <T> T decodePrimitives(Class<T> clazz) throws IOException {
    //TODO: try catch
    if (clazz.equals(Boolean.class)) {
      Boolean val = reader.nextBoolean();
      return (T) val;
    }
    else if (clazz.equals(Integer.class)) {
      Integer val = reader.nextInt();
      return (T) val;
    } else if (clazz.equals(String.class)) {
      return (T) reader.nextString();
    }
    //TODO: other types
    return null;
  }
}
