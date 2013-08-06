/**
 * Copyright 2013 DuraSpace, Inc.
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

package org.fcrepo.binary;

/*
 * Note: Mainly for reporting conversion errors & future use
 * (where other exceptions are not adequate).
 */

public class PolicyTypeException extends RuntimeException {

    /**
     * 
     */
    public PolicyTypeException() {
        super();
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    public PolicyTypeException(final String arg0, final Throwable arg1,
            final boolean arg2, final boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public PolicyTypeException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public PolicyTypeException(final String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public PolicyTypeException(final Throwable arg0) {
        super(arg0);
    }

}
