/**
 * Session-related class used by HTTP servlet controller.
 * <p>
 * To overcome certain limitations and problems related to session tracking,
 * Rivr implements its own session container and session tracking facility.
 * Session ID is encoded as part of the path in the URI used by Rivr during turn
 * execution. The {@link com.nuecho.rivr.core.servlet.session.Session Sessions}
 * are stored in the
 * {@link com.nuecho.rivr.core.servlet.session.SessionContainer} which is
 * responsible for automatic session expiration. Note that once a dialogue is
 * done, the associated session is removed automatically. The session container
 * periodically check for dead session and collect them if necessary.
 */
package com.nuecho.rivr.core.servlet.session;

