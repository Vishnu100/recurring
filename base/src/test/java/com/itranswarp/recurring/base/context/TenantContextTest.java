package com.itranswarp.recurring.base.context;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.recurring.base.context.TenantContext;
import com.itranswarp.recurring.base.exception.MissingContextException;
import com.itranswarp.recurring.base.model.Tenant;
import com.itranswarp.recurring.base.model.User;

public class TenantContextTest {

    static final String TENENT_A_ID = "aaaaaaaaaaffffffffff0000000000ffffffffff0000000000";
    static final String TENANT_A_NAME = "Gene Tech";

    static final String USER_A_ID = "aaaaaaaaaaffffffffff0000000000aaaaaaaaaa0000000000";
    static final String USER_A_NAME = "Mr Gene";

    static final String TENENT_B_ID = "bbbbbbbbbbffffffffff0000000000ffffffffff0000000000";
    static final String TENANT_B_NAME = "Baby Care";

    static final String USER_B_ID = "bbbbbbbbbbffffffffff0000000000bbbbbbbbbb0000000000";
    static final String USER_B_NAME = "Mr Baby";

    static final Tenant TENANT_A;
    static final Tenant TENANT_B;
    static final User USER_A;
    static final User USER_B;

    static {
        TENANT_A = new Tenant();
        TENANT_A.setId(TENENT_A_ID);
        TENANT_A.setName(TENANT_A_NAME);

        TENANT_B = new Tenant();
        TENANT_B.setId(TENENT_B_ID);
        TENANT_B.setName(TENANT_B_NAME);

        USER_A = new User();
        USER_A.setTenantId(TENENT_A_ID);
        USER_A.setId(USER_A_ID);
        USER_A.setName(USER_A_NAME);

        USER_B = new User();
        USER_B.setTenantId(TENENT_B_ID);
        USER_B.setId(USER_B_ID);
        USER_B.setName(USER_B_NAME);
    }

    void assertNoContext() {
        try {
            TenantContext.getCurrentTenant();
        } catch (MissingContextException e) {
        } catch (Throwable t) {
            fail("should not reach here.");
        }
        assertFalse(TenantContext.isInContext());
    }

    @Test(expected = MissingContextException.class)
    public void testGetCurrentTenantFailed() {
        TenantContext.getCurrentTenant();
    }

    @Test(expected = MissingContextException.class)
    public void testGetCurrentUserFailed() {
        TenantContext.getCurrentUser();
    }

    @Test
    public void testGetCurrentTenant() throws Exception {
        assertNoContext();
        try (TenantContext context = new TenantContext(TENANT_A, USER_A)) {
            assertTrue(TenantContext.isInContext());
            Tenant ta = TenantContext.getCurrentTenant();
            User ua = TenantContext.getCurrentUser();
            assertNotNull(ta);
            assertNotNull(ua);
            assertSame(TENANT_A, ta);
            assertSame(USER_A, ua);
        }
        assertNoContext();
    }

    @Test
    public void testNestedGetCurrentTenant() throws Exception {
        assertNoContext();
        // start context A:
        try (TenantContext contextA = new TenantContext(TENANT_A, USER_A)) {
            assertTrue(TenantContext.isInContext());
            Tenant ta = TenantContext.getCurrentTenant();
            User ua = TenantContext.getCurrentUser();
            assertNotNull(ta);
            assertNotNull(ua);
            // start context B:
            try (TenantContext contextB = new TenantContext(TENANT_B, USER_B)) {
                assertTrue(TenantContext.isInContext());
                Tenant tb = TenantContext.getCurrentTenant();
                User ub = TenantContext.getCurrentUser();
                assertNotNull(tb);
                assertNotNull(ub);
                assertSame(TENANT_B, tb);
                assertSame(USER_B, ub);
            }
            // Now should restore context A:
            assertSame(ta, TenantContext.getCurrentTenant());
            assertSame(ua, TenantContext.getCurrentUser());
        }
        assertNoContext();
    }

    @Test
    public void testGetCurrentTenantButFailedInNested() throws Exception {
        assertNoContext();
        // start context A:
        try (TenantContext context = new TenantContext(TENANT_A, USER_A)) {
            assertTrue(TenantContext.isInContext());
            Tenant ta = TenantContext.getCurrentTenant();
            User ua = TenantContext.getCurrentUser();
            assertNotNull(ta);
            assertNotNull(ua);
            assertSame(TENANT_A, ta);
            assertSame(USER_A, ua);
            // try start context B but failed:
            try (TenantContext context2 = new TenantContext(null, null)) {
                fail("should not reach inside.");
            } catch (NullPointerException e) {
                // still have context A:
                assertTrue(TenantContext.isInContext());
                assertSame(ta, TenantContext.getCurrentTenant());
                assertSame(ua, TenantContext.getCurrentUser());
            } catch (Throwable e) {
                fail("should not catch other exception.");
            }
            // still have context A:
            assertSame(ta, TenantContext.getCurrentTenant());
            assertSame(ua, TenantContext.getCurrentUser());
        }
        assertNoContext();
    }

}
