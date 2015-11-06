package com.itranswarp.recurring.base.context;

import java.util.ArrayList;
import java.util.List;

import com.itranswarp.recurring.base.exception.MissingContextException;
import com.itranswarp.recurring.base.model.Tenant;
import com.itranswarp.recurring.base.model.User;

/**
 * TenantContext holds current tenant/user in current thread, and can be reentrant.
 * 
 * Using try (resource) { ... } is a MUST:
 * 
 * <pre>
 * // start context A:
 * try (TenantContext ctx = new TenantContext("User-123")) {
 *     Tenant a = TenantContext.getCurrentTenant(); // Tenant-A
 *     User u = TenantContext.getCurrentUser(); // User-123
 *     // start context B:
 *     try (TenantContext ctx = new TenantContext("User-456")) {
 *         Tenant b = TenantContext.getCurrentTenant(); // Tenant-B
 *         User bu = TenantContext.getCurrentUser(); // User-456
 *     }
 *     // now restore to A:
 *     Tenant a2 = TenantContext.getCurrentTenant(); // a == a2
 *     User u2 = TenantContext.getCurrentUser(); // u == u2
 * }
 * TenantContext.getCurrentTenant(); // Exception
 * </pre>
 * 
 * @author michael
 */
public class TenantContext implements AutoCloseable {

    final class TenantAndUser {
        final Tenant tenant;
        final User user;

        public TenantAndUser(Tenant tenant, User user) {
            this.tenant = tenant;
            this.user = user;
        }
    }

    static final ThreadLocal<List<TenantAndUser>> current = new ThreadLocal<List<TenantAndUser>>();

    public static boolean isInContext() {
    	List<TenantAndUser> list = current.get();
        return list != null;
    }

    static TenantAndUser getCurrent() {
        List<TenantAndUser> list = current.get();
        if (list == null) {
            throw new MissingContextException("No TenantContext.");
        }
        return list.get(list.size() - 1);
    }

    public static Tenant getCurrentTenant() {
        return getCurrent().tenant;
    }

    public static User getCurrentUser() {
        return getCurrent().user;
    }

    public TenantContext(Tenant tenant, User user) {
    	if (tenant == null || user == null) {
    		throw new NullPointerException();
    	}
        List<TenantAndUser> list = current.get();
        if (list == null) {
            list = new ArrayList<TenantAndUser>();
            current.set(list);
        }
        list.add(new TenantAndUser(tenant, user));
    }

    @Override
    public void close() throws Exception {
        List<TenantAndUser> list = current.get();
        list.remove(list.size() - 1);
        if (list.isEmpty()) {
            current.remove();
        }
    }

}
