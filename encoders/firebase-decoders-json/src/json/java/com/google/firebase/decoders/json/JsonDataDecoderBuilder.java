//// Copyright 2020 Google LLC
////
//// Licensed under the Apache License, Version 2.0 (the "License");
//// you may not use this file except in compliance with the License.
//// You may obtain a copy of the License at
////
////      http://www.apache.org/licenses/LICENSE-2.0
////
//// Unless required by applicable law or agreed to in writing, software
//// distributed under the License is distributed on an "AS IS" BASIS,
//// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//// See the License for the specific language governing permissions and
//// limitations under the License.
//
//package com.google.firebase.decoders.json;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.decoders.DataDecoder;
//import com.google.firebase.decoders.ObjectDecoder;
//import com.google.firebase.decoders.TypeToken;
//
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public final class JsonDataDecoderBuilder {
//  private final Map<Class<?>, ObjectDecoder<?>> objectDecoders = new HashMap<>();
//
//
//  public JsonDataDecoderBuilder() {
//    //TODO: Register for default types: Boolean, DATE, String.
//  }
//
//
//  @NonNull
//  public <T> JsonDataDecoderBuilder registerDecoder(
//          @NonNull Class<T> clazz, @NonNull ObjectDecoder<? super T> objectDecoder) {
//    objectDecoders.put(clazz, objectDecoder);
//    return this;
//  }
//
//
//  @NonNull
//  public DataDecoder build() {
//    return new DataDecoder() {
//      @NonNull
//      @Override
//      public <T> T decode(@NonNull TypeToken<T> typeToken, @NonNull InputStream input) {
//        //TODO: move to -> delegate to JsonDataDecoderContext(json decoder logic happens here).
//        JsonDataDecoderBuilderContext jsonDataDecoderBuilderContext
//                = new JsonDataDecoderBuilderContext(objectDecoders);
//        //get T instance from jsonDataDecoderBuilderContext
//        try {
//          return jsonDataDecoderBuilderContext.decode(input, typeToken);
//        } catch (UnsupportedEncodingException e) {
//          e.printStackTrace();
//        }
//        return null;
//      }
//    };
//  }
//}
