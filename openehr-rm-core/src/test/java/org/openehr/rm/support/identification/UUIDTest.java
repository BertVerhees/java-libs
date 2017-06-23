/*
 * UUIDTest.java
 * JUnit based test
 *
 * Created on July 10, 2006, 4:27 PM
 */

package org.openehr.rm.support.identification;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 *
 * @author yinsulim
 */
public class UUIDTest  {

    @Test
    public void testConstructorTakeString() {
        assertNotNull(new UUID("128-1-1-12-15"));
    }
}
