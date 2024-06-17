package org.safehouse.authservice.service.util;

import org.safehouse.authservice.model.RequestMetaInfo;

import java.util.UUID;

public class RequestMetaInfoHolder {

	private static ThreadLocal<RequestMetaInfo> metaInfoThreadLocal = new ThreadLocal<>();

	public static void setRequestMetaInfo(RequestMetaInfo requestMetaInfo) {
		metaInfoThreadLocal.set(requestMetaInfo);
	}

	public static RequestMetaInfo getCurrentClientRequestMetaInfo() {
		return metaInfoThreadLocal.get();
	}

	/**
	 * Generates a unique correlation ID to be used for tracking requests across different components
	 * and services in a distributed system.
	 *
	 * @return a unique correlation ID as a {@code String}
	 */
	public static String generateCorrelationId() {
		return UUID.randomUUID().toString();
	}
}

