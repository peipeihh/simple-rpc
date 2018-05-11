/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
package com.pphh.rpc.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 5/10/2018
 */
public class RpcProviderResource {

    public static Map<String, Provider<?>> PROVIDERS = new ConcurrentHashMap<>();

}
