/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hugegraph.pd.util.grpc;

import java.lang.reflect.Field;

import io.grpc.Grpc;
import io.grpc.ServerCall;
import io.grpc.stub.StreamObserver;

public class StreamObserverUtil {

    static Object fieldLock = new Object();
    static Field callField;

    public static String getRemoteIP(StreamObserver observer) {
        String ip = "";
        try {
            if (callField == null) {
                synchronized (fieldLock) {
                    callField = observer.getClass().getDeclaredField("call");
                    callField.setAccessible(true);
                }
            }
            ServerCall call = (ServerCall) callField.get(observer);
            if (call != null) {
                ip = call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR).toString();
            }
        } catch (Exception e) {

        }
        return ip;
    }
}
